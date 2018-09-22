package com.spellchain.passbook.service;

import com.spellchain.passbook.vo.Feedback;
import com.spellchain.passbook.vo.Response;

/**
 * <p>评论服务： 用户评论相关服务</p>
 * @author young
 * @version 1.0 2018年09月22日
 * @since JDK1.8
 */
public interface IFeedbackService {

    /**
     * 创建评论
     * @param feedback {@link Feedback}
     * @return {@link Response}
     */
    Response createFeedback(Feedback feedback);

    /**
     * 获取用户评论
     * @param userId 用户 id
     * @return {@link Response}
     */
    Response getFeedback(Long userId);
}
