package com.spellchain.passbook.service.impl;

import com.alibaba.fastjson.JSON;
import com.spellchain.passbook.constant.Constants;
import com.spellchain.passbook.constant.PassStatus;
import com.spellchain.passbook.dao.MerchantsDao;
import com.spellchain.passbook.entity.Merchants;
import com.spellchain.passbook.mapper.PassRowMapper;
import com.spellchain.passbook.service.IUserPassService;
import com.spellchain.passbook.vo.Pass;
import com.spellchain.passbook.vo.PassInfo;
import com.spellchain.passbook.vo.PassTemplate;
import com.spellchain.passbook.vo.Response;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>用户优惠券相关功能实现</p>
 * @author young
 * @version 1.0 2018年09月24日
 * @since JDK8
 */
@Slf4j
@Service
public class UserPassServiceImpl implements IUserPassService {

    /** HBase Client */
    private final HbaseTemplate hbaseTemplate;

    /** Merchants Dao */
    private final MerchantsDao merchantsDao;

    @Autowired
    public UserPassServiceImpl(HbaseTemplate hbaseTemplate, MerchantsDao merchantsDao) {
        this.hbaseTemplate = hbaseTemplate;
        this.merchantsDao = merchantsDao;
    }

    @Override
    public Response getUserPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId, PassStatus.UNUSED);
    }

    @Override
    public Response getUserUsedPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId, PassStatus.USED);
    }

    @Override
    public Response getUserAllPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId, PassStatus.ALL);
    }

    @Override
    public Response userUsePass(Pass pass) {

        // 根据 userId 构造行键前缀
        byte[] rowPrefix = Bytes.toBytes(new StringBuilder(String.valueOf(pass.getUserId())).reverse().toString());

        Scan scan = new Scan();
        List<Filter> filters = new ArrayList<>();
        filters.add(new PrefixFilter(rowPrefix));
        filters.add(new SingleColumnValueFilter(
                Constants.PassTable.FAMILY_I.getBytes(),
                Constants.PassTable.TEMPLATE_ID.getBytes(),
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes(pass.getTemplateId())
        ));
        filters.add(new SingleColumnValueFilter(
                Constants.PassTable.FAMILY_I.getBytes(),
                Constants.PassTable.CON_DATE.getBytes(),
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes("-1")
        ));

        scan.setFilter(new FilterList(filters));

        List<Pass> passes = hbaseTemplate.find(Constants.PassTable.TABLE_NAME,
                scan, new PassRowMapper());

        if (null == passes || passes.size() != 1) {
            log.error("UserUsePass Error : {}", JSON.toJSONString(pass));
            return Response.failure("UserUsePass Error");
        }

        byte[] FAMILY_I = Constants.PassTable.FAMILY_I.getBytes();
        byte[] CON_DATE = Constants.PassTable.CON_DATE.getBytes();

        List<Mutation> datas = new ArrayList<>();
        Put put = new Put(passes.get(0).getRowKey().getBytes());
        put.addColumn(FAMILY_I, CON_DATE, Bytes.toBytes(DateFormatUtils.ISO_DATE_FORMAT.format(new Date())));

        datas.add(put);

        hbaseTemplate.saveOrUpdates(Constants.PassTable.TABLE_NAME, datas);

        return Response.success();
    }

    /**
     * 根据优惠券状态获取优惠券信息
     * @param userId 用户 id
     * @param status {@link PassStatus}
     * @return {@link Response}
     * @throws Exception 可能抛出的异常
     */
    private Response getPassInfoByStatus(Long userId, PassStatus status) throws Exception {

        // 根据 userId 构造行键前缀
        byte[] rowPrefix = Bytes.toBytes(new StringBuilder(String.valueOf(userId)).reverse().toString());

        // 构造过滤器比较器
        CompareFilter.CompareOp compareOp = status == PassStatus.UNUSED ?
                CompareFilter.CompareOp.EQUAL : CompareFilter.CompareOp.NOT_EQUAL;

        // 构造扫描器
        Scan scan = new Scan();

        List<Filter> filters = new ArrayList<>();

        // 1. 行键前缀过滤器，找到特定用户的优惠券
        filters.add(new PrefixFilter(rowPrefix));
        // 2. 基于列单元值的过滤器，找到为使用的优惠券
        if (status != PassStatus.ALL) {
            filters.add(new SingleColumnValueFilter(
                    Constants.PassTable.FAMILY_I.getBytes(),
                    Constants.PassTable.CON_DATE.getBytes(), compareOp,
                    Bytes.toBytes(-1)));
        }

        scan.setFilter(new FilterList(filters));

        List<Pass> passes = hbaseTemplate.find(Constants.PassTable.TABLE_NAME, scan, new PassRowMapper());
        Map<String, PassTemplate> passTemplateMap = buildPassTemplateMap(passes);
        Map<Integer, Merchants> merchantsMap = buildMerchantsMap(
                new ArrayList<>(passTemplateMap.values())
        );

        List<PassInfo> result = new ArrayList<>();
        for (Pass pass :
                passes) {
            PassTemplate passTemplate = passTemplateMap.getOrDefault(pass.getTemplateId(), null);

            if (null == passTemplate) {
                log.error("PassTemplate Null : {}", pass.getTemplateId());
                continue;
            }

            Merchants merchants = merchantsMap.getOrDefault(passTemplate.getId(), null);
            if (null == merchants) {
                log.error("Merchants Null : {}", passTemplate.getId());
                continue;
            }

            result.add(new PassInfo(pass, passTemplate, merchants));
        }
        return new Response(result);
    }

    /**
     * 通过获取的 PassList 对象构造 Map 映射
     * @param passList {@link Pass}
     * @return Map {@link PassTemplate}
     * @throws Exception 可能抛出的异常
     */
    private Map<String, PassTemplate> buildPassTemplateMap(List<Pass> passList) throws Exception {

        String[] patterns = new String[] {"yyyy-MM-dd"};

        byte[] FAMILY_B = Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B);
        byte[] ID = Bytes.toBytes(Constants.PassTemplateTable.ID);
        byte[] TITLE = Bytes.toBytes(Constants.PassTemplateTable.TITLE);
        byte[] SUMMARY = Bytes.toBytes(Constants.PassTemplateTable.SUMMARY);
        byte[] DESC = Bytes.toBytes(Constants.PassTemplateTable.DESC);
        byte[] HAS_TOKEN = Bytes.toBytes(Constants.PassTemplateTable.HAS_TOKEN);
        byte[] BACKGROUND = Bytes.toBytes(Constants.PassTemplateTable.BACKGROUND);

        byte[] FAMILY_C = Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C);
        byte[] LIMIT = Bytes.toBytes(Constants.PassTemplateTable.LIMIT);
        byte[] START = Bytes.toBytes(Constants.PassTemplateTable.START);
        byte[] END = Bytes.toBytes(Constants.PassTemplateTable.END);

        List<String> templateIds = passList.stream().map(
                Pass::getTemplateId
        ).collect(Collectors.toList());

        List<Get> templateGets = new ArrayList<>(templateIds.size());
        templateIds.forEach(t -> templateGets.add(new Get(Bytes.toBytes(t))));
        // HBase 客户端通过 templateId 集合获取结果集
        Result[] templateResults = hbaseTemplate.getConnection().getTable(TableName.valueOf(Constants.PassTemplateTable.TABLE_NAME)).get(templateGets);

        // 构造 PassTemplateId -> PassTemplate Object 的 Map， 用于构造 PassInfo
        Map<String, PassTemplate> templateId2Object = new HashMap<>();
        for (Result result :
             templateResults) {
            PassTemplate passTemplate = new PassTemplate();

            passTemplate.setId(Bytes.toInt(result.getValue(FAMILY_B, ID)));
            passTemplate.setTitle(Bytes.toString(result.getValue(FAMILY_B, TITLE)));
            passTemplate.setSummary(Bytes.toString(result.getValue(FAMILY_B, SUMMARY)));
            passTemplate.setDesc(Bytes.toString(result.getValue(FAMILY_B, DESC)));
            passTemplate.setHasToken(Bytes.toBoolean(result.getValue(FAMILY_B, HAS_TOKEN)));
            passTemplate.setBackground(Bytes.toInt(result.getValue(FAMILY_B, BACKGROUND)));

            passTemplate.setLimit(Bytes.toLong(result.getValue(FAMILY_C, LIMIT)));
            passTemplate.setStart(DateUtils.parseDate(Bytes.toString(result.getValue(FAMILY_C, START)), patterns));
            passTemplate.setEnd(DateUtils.parseDate(Bytes.toString(result.getValue(FAMILY_C, END)), patterns));
            templateId2Object.put(Bytes.toString(result.getRow()), passTemplate);
        }

        return templateId2Object;
    }

    /**
     * 通过获取的 PassTemplate 对象构造 Merchants Map
     * @param passTemplates {@link PassTemplate}
     * @return Map {@link Merchants}
     */
    private Map<Integer, Merchants> buildMerchantsMap(List<PassTemplate> passTemplates) {

        Map<Integer, Merchants> merchantsMap = new HashMap<>();
        List<Integer> merchantsIds = passTemplates.stream().map(
                PassTemplate::getId
        ).collect(Collectors.toList());
        List<Merchants> merchants = merchantsDao.findByIdIn(merchantsIds);

        merchants.forEach(m -> merchantsMap.put(m.getId(), m));

        return merchantsMap;
    }
}
