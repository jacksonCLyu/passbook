package com.spellchain.passbook.mapper;

import com.spellchain.passbook.constant.Constants;
import com.spellchain.passbook.vo.Feedback;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * <h1>HBase Feedback Row To Feedback Object</h1>
 * @author jackson.yu
 * @version 1.0 2018年09月08日
 * @since 1.8
 */
public class FeedbackRowMapper implements RowMapper<Feedback> {

    private static byte[] FAMILY_I = Constants.FeedbackTable.FAMILY_I.getBytes();
    private static byte[] USER_ID = Constants.FeedbackTable.USER_ID.getBytes();
    private static byte[] TYPE = Constants.FeedbackTable.TYPE.getBytes();
    private static byte[] TEMPLATE_ID = Constants.FeedbackTable.TEMPLATE_ID.getBytes();
    private static byte[] COMMENT = Constants.FeedbackTable.COMMENT.getBytes();

    @Override
    public Feedback mapRow(Result result, int i) throws Exception {

        Feedback feedback = new Feedback();

        feedback.setUserId(Bytes.toLong(result.getValue(FAMILY_I, USER_ID)));
        feedback.setType(Bytes.toString(result.getValue(FAMILY_I, TYPE)));
        feedback.setTemplateId(Bytes.toString(result.getValue(FAMILY_I, TEMPLATE_ID)));
        feedback.setComment(Bytes.toString(result.getValue(FAMILY_I, COMMENT)));

        return feedback;
    }
}
