package com.spellchain.passbook.service;

import com.alibaba.fastjson.JSON;
import com.spellchain.passbook.vo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>用户服务测试</p>
 * @author young
 * @version 1.0 2018年10月27日
 * @since JDK8
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private IUserService userService;

    @Test
    public void testCreateUser() throws Exception {

        User user = new User();
        user.setBaseInfo(new User.BaseInfo("young", 18, "m")
        );
        user.setOtherInfo(new User.OtherInfo("123456", "北京市朝阳区")
        );

        // {"data":{"baseInfo":{"age":18,"name":"young","sex":"m"},
        // "id":193831,
        // "otherInfo":{"address":"北京市朝阳区","phone":"123456"}},
        // "errorCode":0,"errorMsg":""}
        System.out.println(JSON.toJSONString(userService.createUser(user)));
    }
}
