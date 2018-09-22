package com.spellchain.passbook.service;

import com.spellchain.passbook.vo.Pass;
import com.spellchain.passbook.vo.Response;

/**
 * <p>获取用户个人优惠券信息</p>
 * @author young
 * @version 1.0 2018年09月22日
 * @since JDK1.8
 */
public interface IUserPassService {

    /**
     * 获取用户个人优惠券信息， 即我的优惠券功能
     * @param userId 用户 id
     * @return {@link Response}
     * @throws Exception
     */
    Response getUserPassInfo(Long userId) throws Exception;

    /**
     * 获取用户已经消费了的优惠券， 即已使用的优惠券功能实现
     * @param userId 用户 id
     * @return {@link Response}
     * @throws Exception
     */
    Response getUserUsedPassInfo(Long userId) throws Exception;

    /**
     * 获取用户所有的优惠券
     * @param userId 用户 id
     * @return {@link Response}
     * @throws Exception
     */
    Response getUserAllPassInfo(Long userId) throws Exception;

    /**
     * 用户使用优惠券
     * @param pass {@link Pass}
     * @return {@link Response}
     */
    Response userUsePass(Pass pass);
}
