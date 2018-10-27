package com.spellchain.passbook.controller;

import com.spellchain.passbook.log.LogConstants;
import com.spellchain.passbook.log.LogGenerator;
import com.spellchain.passbook.service.IUserService;
import com.spellchain.passbook.vo.Response;
import com.spellchain.passbook.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>创建用户服务</p>
 * @author young
 * @version 1.0 2018年10月26日
 * @since JDK8
 */
@Slf4j
@RestController
@RequestMapping("/passbook")
public class UserController {

    /** 创建用户服务 */
    private final IUserService userService;

    /** HttpServletRequest */
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public UserController(IUserService userService, HttpServletRequest httpServletRequest) {
        this.userService = userService;
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * 创建用户
     * @param user {@link User}
     * @return {@link Response}
     */
    @ResponseBody
    @PostMapping("/createuser")
    Response createUser(@RequestBody User user) throws Exception {

        LogGenerator.genLog(
                httpServletRequest,
                -1L,
                LogConstants.ActionName.CREATE_USER,
                user
        );
        return userService.createUser(user);
    }
}
