package com.spellchain.passbook.service;

import com.alibaba.fastjson.JSON;
import com.spellchain.passbook.vo.GainPassTemplateRequest;
import com.spellchain.passbook.vo.PassTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>用户领取优惠券功能测试</p>
 * @author young
 * @version 1.0 2018年10月27日
 * @since JDK8
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GainPasstemplateServTest extends AbstractServiceTest {

    @Autowired
    private IGainPassTemplateService gainPassTemplateService;

    @Test
    public void testGainPasstemplate() throws Exception {

        PassTemplate passTemplate = new PassTemplate();
        passTemplate.setId(1);
        passTemplate.setTitle("title: 朝阳小酌-3");
        passTemplate.setHasToken(true);

        System.out.println(JSON.toJSONString(
                gainPassTemplateService.gainPassTemplate(
                        new GainPassTemplateRequest(userId, passTemplate)
                )
        ));
    }
}
