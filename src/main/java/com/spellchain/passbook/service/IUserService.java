package com.spellchain.passbook.service;

import com.spellchain.passbook.vo.Response;
import com.spellchain.passbook.vo.User;

/**
 * <p>用户服务： 创建 User 服务</p>
 * @author young
 * @version 1.0 2018年09月22日
 * @since JDK1.8
 */
public interface IUserService {

    /**
     * 创建用户
     * @param user {@link User}
     * @return {@link Response}
     * @throws Exception 可能抛出的异常
     */
    Response createUser(User user) throws Exception;
}
