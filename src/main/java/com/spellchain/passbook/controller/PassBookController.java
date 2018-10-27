package com.spellchain.passbook.controller;

import com.spellchain.passbook.log.LogConstants;
import com.spellchain.passbook.log.LogGenerator;
import com.spellchain.passbook.service.IFeedbackService;
import com.spellchain.passbook.service.IGainPassTemplateService;
import com.spellchain.passbook.service.IInventoryService;
import com.spellchain.passbook.service.IUserPassService;
import com.spellchain.passbook.vo.Feedback;
import com.spellchain.passbook.vo.GainPassTemplateRequest;
import com.spellchain.passbook.vo.Pass;
import com.spellchain.passbook.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>PassBook Rest Controller</p>
 * @author young
 * @version 1.0 2018年10月26日
 * @since JDK8
 */
@Slf4j
@RestController
@RequestMapping("/passbook")
public class PassBookController {

    /** 用户优惠券服务 */
    private final IUserPassService passService;

    /** 优惠券库存服务 */
    private final IInventoryService inventoryService;

    /** 领取优惠券服务 */
    private final IGainPassTemplateService gainPassTemplateService;

    /** 反馈服务 */
    private final IFeedbackService feedbackService;

    /** HttpServletRequest */
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public PassBookController(IUserPassService passService, IInventoryService inventoryService, IGainPassTemplateService gainPassTemplateService, IFeedbackService feedbackService, HttpServletRequest httpServletRequest) {
        this.passService = passService;
        this.inventoryService = inventoryService;
        this.gainPassTemplateService = gainPassTemplateService;
        this.feedbackService = feedbackService;
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * 领取用户个人的优惠券信息
     * @param userId 用户 id
     * @return {@link Response}
     */
    @GetMapping("/userpassinfo")
    @ResponseBody
    Response userPassInfo(Long userId) throws Exception {

        LogGenerator.genLog(
                httpServletRequest,
                userId, LogConstants.ActionName.USER_PASS_INFO,
                null
        );
        return passService.getUserPassInfo(userId);
    }

    /**
     * 获取用户使用了的优惠券信息
     * @param userId 用户 id
     * @return {@link Response}
     */
    @ResponseBody
    @GetMapping("/userusedpassinfo")
    Response userUsedPassInfo(Long userId) throws Exception {

        LogGenerator.genLog(
                httpServletRequest,
                userId,
                LogConstants.ActionName.USER_USED_PASS_INFO,
                null
        );
        return passService.getUserUsedPassInfo(userId);
    }

    /**
     * 用户使用优惠券
     * @param pass {@link Pass}
     * @return {@link Response}
     */
    @ResponseBody
    @PostMapping("/userusepass")
    Response userUsePass(Pass pass) {

        LogGenerator.genLog(
                httpServletRequest,
                pass.getUserId(),
                LogConstants.ActionName.USER_USE_PASS,
                pass
        );
        return passService.userUsePass(pass);
    }

    /**
     * 获取库存信息
     * @param userId 用户 id
     * @return {@link Response}
     */
    @ResponseBody
    @GetMapping("/inventoryinfo")
    Response inventoryInfo(Long userId) throws Exception {

        LogGenerator.genLog(
                httpServletRequest,
                userId,
                LogConstants.ActionName.INVENTORY_INFO,
                null
        );
        return inventoryService.getInventoryInfo(userId);
    }

    /**
     * 用户领取优惠券
     * @param request {@link GainPassTemplateRequest}
     * @return {@link RequestBody}
     */
    @ResponseBody
    @PostMapping("/gainpasstemplate")
    Response gainPassTemplate(@RequestBody GainPassTemplateRequest request) throws Exception {

        LogGenerator.genLog(
                httpServletRequest,
                request.getUserId(),
                LogConstants.ActionName.GAIN_PASS_TEMPLATE,
                request
        );
        return gainPassTemplateService.gainPassTemplate(request);
    }

    /**
     * 用户创建评论
     * @param feedback {@link Feedback}
     * @return {@link Response}
     */
    @ResponseBody
    @PostMapping("/createfeedback")
    Response createFeedback(Feedback feedback) {

        LogGenerator.genLog(
                httpServletRequest,
                feedback.getUserId(),
                LogConstants.ActionName.CREATE_FEEDBACK,
                feedback
        );
        return feedbackService.createFeedback(feedback);
    }

    /**
     * 用户获取评论信息
     * @param userId 用户 id
     * @return {@link Response}
     */
    @ResponseBody
    @GetMapping("/getfeedback")
    Response getFeedback(Long userId) {

        LogGenerator.genLog(
                httpServletRequest,
                userId,
                LogConstants.ActionName.GET_FEEDBACK,
                null
        );
        return feedbackService.getFeedback(userId);
    }

    /**
     * 异常演示接口
     * @return {@link Response}
     */
    @ResponseBody
    @GetMapping("/exception")
    Response exception() throws Exception {
        throw new Exception("Hello Exception!");
    }
}

