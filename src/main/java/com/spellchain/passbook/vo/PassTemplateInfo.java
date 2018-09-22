package com.spellchain.passbook.vo;

import com.spellchain.passbook.entity.Merchants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>优惠券模板信息</p>
 * @author young
 * @version 1.0 2018年09月22日
 * @since JDK1.8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassTemplateInfo extends PassTemplate {

    /** 优惠券模板 */
    private PassTemplate passTemplate;

    /** 优惠券对应商户 */
    private Merchants merchants;
}
