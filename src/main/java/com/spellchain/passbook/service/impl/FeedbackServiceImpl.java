package com.spellchain.passbook.service.impl;

import com.alibaba.fastjson.JSON;
import com.spellchain.passbook.constant.Constants;
import com.spellchain.passbook.mapper.FeedbackRowMapper;
import com.spellchain.passbook.service.IFeedbackService;
import com.spellchain.passbook.utils.RowKeyGenUtil;
import com.spellchain.passbook.vo.Feedback;
import com.spellchain.passbook.vo.Response;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>评论功能实现</p>
 * @author young
 * @version 1.0 2018年09月22日
 * @since JDK1.8
 */
@Slf4j
@Service
public class FeedbackServiceImpl implements IFeedbackService {

    /** HBase 客户端 */
    private final HbaseTemplate hbaseTemplate;

    @Autowired
    public FeedbackServiceImpl(HbaseTemplate hbaseTemplate) {
        this.hbaseTemplate = hbaseTemplate;
    }

    @Override
    public Response createFeedback(Feedback feedback) {

        if (!feedback.validate()) {
            log.error("Feedback Error: {}", JSON.toJSONString(feedback));
            return Response.failure("Feedback Error");
        }

        Put put = new Put(Bytes.toBytes(RowKeyGenUtil.genFeedbackRowKey(feedback)));

        put.addColumn(
                Bytes.toBytes(Constants.FeedbackTable.FAMILY_I),
                Bytes.toBytes(Constants.FeedbackTable.USER_ID),
                Bytes.toBytes(feedback.getUserId())
        );
        put.addColumn(
                Bytes.toBytes(Constants.FeedbackTable.FAMILY_I),
                Bytes.toBytes(Constants.FeedbackTable.TYPE),
                Bytes.toBytes(feedback.getType())
        );
        put.addColumn(
                Bytes.toBytes(Constants.FeedbackTable.FAMILY_I),
                Bytes.toBytes(Constants.FeedbackTable.TEMPLATE_ID),
                Bytes.toBytes(feedback.getTemplateId())
        );
        put.addColumn(
                Bytes.toBytes(Constants.FeedbackTable.FAMILY_I),
                Bytes.toBytes(Constants.FeedbackTable.COMMENT),
                Bytes.toBytes(feedback.getComment())
        );

        hbaseTemplate.saveOrUpdate(Constants.FeedbackTable.TABLE_NAME, put);

        return Response.success();

    }

    @Override
    public Response getFeedback(Long userId) {

        byte[] reverseUserId = new StringBuilder(String.valueOf(userId)).reverse().toString().getBytes();
        Scan scan = new Scan();
        scan.setFilter(new PrefixFilter(reverseUserId));

        // find method will return muti result
        List<Feedback> feedbacks = hbaseTemplate.find(Constants.FeedbackTable.TABLE_NAME, scan, new FeedbackRowMapper());

        return new Response(feedbacks);
    }
}
