package com.spellchain.passbook.service;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>服务测试抽象基类</p>
 * @author young
 * @version 1.0 2018年10月27日
 * @since JDK8
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class AbstractServiceTest {

    Long userId;

    @Before
    public void init() {

        userId = 193831L;
    }
}
