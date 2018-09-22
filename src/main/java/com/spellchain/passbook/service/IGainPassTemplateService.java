package com.spellchain.passbook.service;

import com.spellchain.passbook.vo.GainPassTemplateRequest;
import com.spellchain.passbook.vo.Response;

/**
 * <p>用户领取优惠券接口定义</p>
 * @author young
 * @version 1.0 2018年09月22日
 * @since JDK1.8
 */
public interface IGainPassTemplateService {

    /**
     * 用户领取优惠券
     * @param request {@link GainPassTemplateRequest}
     * @return {@link Response}
     * @throws Exception
     */
    Response gainPassTemplate(GainPassTemplateRequest request) throws Exception;
}
