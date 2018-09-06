package com.spellchain.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <h1>用户领取的优惠券</h1>
 * @author jackson.yu
 * @version 1.0 2018年09月06日
 * @since 1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pass {

    /** 用户 id */
    private Long id;

    /** pass 在 HBase 中的 rowKey */
    private String rowKey;

    /** PassTemplate 在 HBase 中的 rowKey */
    private String templateId;

    /** 优惠券 token, 有可能是 null, 则填充 -1 */
    private String token;

    /** 领取日期 */
    private Date assignedDate;

    /** 消费日期,不为空则代表已经被消费 */
    private Date conDate;
}
