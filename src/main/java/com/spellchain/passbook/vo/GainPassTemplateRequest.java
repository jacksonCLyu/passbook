package com.spellchain.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>用户领取优惠前的请求对象</p>
 * @author young
 * @version 1.0 2018年09月22日
 * @since JDK1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GainPassTemplateRequest {

    /** 用户 id */
    private Long userId;

    /** PassTemplate 对象 */
    private PassTemplate passTemplate;
}
