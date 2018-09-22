package com.spellchain.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>库存请求响应</p>
 * @author young
 * @version 1.0 2018年09月22日
 * @since JDK1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {

    /** 用户 id */
    private Long userId;

    /** 优惠券模板信息 */
    private List<PassTemplateInfo> passTemplateInfos;
}
