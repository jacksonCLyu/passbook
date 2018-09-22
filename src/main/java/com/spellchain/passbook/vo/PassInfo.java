package com.spellchain.passbook.vo;

import com.spellchain.passbook.entity.Merchants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>用户领取的优惠券信息</p>
 * @author young
 * @version 1.0 2018年09月22日
 * @since JDK1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassInfo {

    /** 优惠券 */
    private Pass pass;

    /** 优惠券模板 */
    private PassTemplate passTemplate;

    /** 优惠券对应的商户 */
    private Merchants merchants;
}
