package com.spellchain.passbook.service;

import com.spellchain.passbook.vo.Response;

/**
 * <p>获取库存信息： 只返回用户没有领取的优惠券， 即优惠券库存功能接口定义</p>
 * @author young
 * @version 1.0 2018年09月22日
 * @since JDK1.8
 */
public interface IInventoryService {

    /**
     * 获取库存信息
     * @param userId 用户 id
     * @return {@link Response}
     * @throws Exception
     */
    Response getInventoryInfo(Long userId) throws Exception;
}
