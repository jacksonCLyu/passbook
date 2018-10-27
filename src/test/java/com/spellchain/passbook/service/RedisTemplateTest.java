package com.spellchain.passbook.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>Redis 客户端测试</p>
 * @author young
 * @version 1.0 2018年10月26日
 * @since JDK8
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTemplateTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testRedisTemplate() {

        // redis flush all
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.flushAll();
            return null;
        });

        assert redisTemplate.opsForValue().get("name") == null;

        redisTemplate.opsForValue().set("name", "spell");

        assert redisTemplate.opsForValue().get("name") != null;

        System.out.println(redisTemplate.opsForValue().get("name"));

        // 输出 spell
    }
}
