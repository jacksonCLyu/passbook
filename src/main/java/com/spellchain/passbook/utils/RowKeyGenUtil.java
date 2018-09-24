package com.spellchain.passbook.utils;

import com.spellchain.passbook.vo.Feedback;
import com.spellchain.passbook.vo.GainPassTemplateRequest;
import com.spellchain.passbook.vo.PassTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * <h1>RowKey 生成器工具类</h1>
 * @author jackson.yu
 * @version 1.0 2018年09月08日
 * @since 1.8
 */
@Slf4j
public class RowKeyGenUtil {

    /**
     * <h2>根据提供的 PassTemplate 对象创建 RowKey</h2>
     * @param passTemplate {@link PassTemplate}优惠券模板
     * @return PassTemplate RowKey String
     */
    public static String genPassTemplateRowKey(PassTemplate passTemplate){
        String passInfo = String.valueOf(passTemplate.getId()) + "_" + passTemplate.getTitle();
        // HBase 本身为集群, 利用 rowKey 存储, rowKey 相近的值存储在一起, md5 处理后可负载均衡
        String rowKey = DigestUtils.md5Hex(passInfo);
        log.info("GenPassTemplateRowKey:{},{}",passInfo, rowKey);

        return rowKey;
    }

    /**
     * 根据提供的领取优惠券请求生成 Rowkey， 只可以在领取优惠券的时候使用
     * Pass RowKey = reversed(userId) + inverse(timestamp) + PassTemplate RowKey
     * @param request {@link GainPassTemplateRequest}
     * @return String RowKey
     */
    public static String genPassRowKey(GainPassTemplateRequest request){

        return new StringBuilder(String.valueOf(request.getUserId())).reverse().toString()
                + (Long.MAX_VALUE - System.currentTimeMillis())
                + genPassTemplateRowKey(request.getPassTemplate());
    }

    /**
     * <h2>根据 Feedback 构造 RowKey</h2>
     * @param feedback {@link Feedback}
     * @return String Feedback RowKey
     */
    public static String genFeedbackRowKey(Feedback feedback){
        // 用户 id 翻转后为随机数, 利用时间戳方便存储时将后存储的 rowKey 值比先存储的小, 便于查询时筛选
        return new StringBuilder(String.valueOf(feedback.getUserId())).reverse().toString() +
                (Long.MAX_VALUE - System.currentTimeMillis());
    }
}
