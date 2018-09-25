package com.spellchain.passbook.service.impl;

import com.spellchain.passbook.constant.Constants;
import com.spellchain.passbook.dao.MerchantsDao;
import com.spellchain.passbook.entity.Merchants;
import com.spellchain.passbook.mapper.PassTemplateRowMapper;
import com.spellchain.passbook.service.IInventoryService;
import com.spellchain.passbook.service.IUserPassService;
import com.spellchain.passbook.utils.RowKeyGenUtil;
import com.spellchain.passbook.vo.*;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.LongComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>获取库存信息： 返回用户没有领取的</p>
 * @author young
 * @version 1.0 2018年09月25日
 * @since JDK8
 */
@Slf4j
@Service
public class InventoryServiceImpl implements IInventoryService {

    /** HBase 客户端 */
    private final HbaseTemplate hbaseTemplate;

    /** Merchants Dao */
    private final MerchantsDao merchantsDao;

    /** 用户服务 */
    private final IUserPassService userPassService;

    @Autowired
    public InventoryServiceImpl(HbaseTemplate hbaseTemplate, MerchantsDao merchantsDao, IUserPassService userPassService) {
        this.hbaseTemplate = hbaseTemplate;
        this.merchantsDao = merchantsDao;
        this.userPassService = userPassService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Response getInventoryInfo(Long userId) throws Exception {

        Response allUserPass = userPassService.getUserAllPassInfo(userId);
        List<PassInfo> passInfos = (List<PassInfo>) allUserPass.getData();

        List<PassTemplate> excludeObject = passInfos.stream().map(PassInfo::getPassTemplate)
                .collect(Collectors.toList());

        List<String> excludeIds = new ArrayList<>();

        excludeObject.forEach(e -> excludeIds.add(
                RowKeyGenUtil.genPassTemplateRowKey(e)
        ));

        return new Response(new InventoryResponse(userId,
                buildPassTemplateInfo(getAvilablePassTemplate(excludeIds))));
    }

    /**
     * 获取系统中可用的优惠券
     * @param excludeIds 需要排除的优惠券 ids
     * @return {@link PassTemplate}
     */
    private List<PassTemplate> getAvilablePassTemplate(List<String> excludeIds) {

        // or 关系过滤
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);

        filterList.addFilter(
                new SingleColumnValueFilter(
                        Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C),
                        Bytes.toBytes(Constants.PassTemplateTable.LIMIT),
                        CompareFilter.CompareOp.GREATER,
                        new LongComparator(0L)
                )
        );
        filterList.addFilter(
                new SingleColumnValueFilter(
                        Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C),
                        Bytes.toBytes(Constants.PassTemplateTable.LIMIT),
                        CompareFilter.CompareOp.GREATER,
                        new LongComparator(-1)
                )
        );

        Scan scan = new Scan();
        scan.setFilter(filterList);
        
        List<PassTemplate> validTemplates = hbaseTemplate.find(Constants.PassTemplateTable.TABLE_NAME,
                scan, new PassTemplateRowMapper());
        List<PassTemplate> availablePassTemplates = new ArrayList<>();

        Date cur = new Date();

        for (PassTemplate validTemplate :
                validTemplates) {
            if (excludeIds.contains(RowKeyGenUtil.genPassTemplateRowKey(validTemplate))) {
                continue;
            }

            if (cur.getTime() >= validTemplate.getStart().getTime()
                    && cur.getTime() <= validTemplate.getEnd().getTime()) {
                availablePassTemplates.add(validTemplate);
            }
        }
        return availablePassTemplates;
    }

    /**
     * 构造优惠券信息
     * @param passTemplates {@link PassTemplate}
     * @return {@link PassTemplate}
     */
    private List<PassTemplateInfo> buildPassTemplateInfo(List<PassTemplate> passTemplates) {

        Map<Integer, Merchants> merchantsMap = new HashMap<>();

        List<Integer> merchantsIds = passTemplates.stream().map(
                PassTemplate::getId
        ).collect(Collectors.toList());
        List<Merchants> merchants = merchantsDao.findByIdIn(merchantsIds);
        merchants.forEach(m -> merchantsMap.put(m.getId(), m));

        List<PassTemplateInfo> result = new ArrayList<>(passTemplates.size());

        for (PassTemplate pa :
                passTemplates) {
            Merchants mc = merchantsMap.getOrDefault(pa.getId(),null);
            if (null == mc) {
                log.error("Merchants Error: {}", pa.getId());
                continue;
            }
            result.add(new PassTemplateInfo(pa, mc));
        }
        return result;
    }
}
