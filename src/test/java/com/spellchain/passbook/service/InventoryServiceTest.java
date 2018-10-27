package com.spellchain.passbook.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>库存服务测试</p>
 * @author young
 * @version 1.0 2018年10月27日
 * @since JDK8
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InventoryServiceTest extends AbstractServiceTest {

    @Autowired
    private IInventoryService inventoryService;

    @Test
    public void testGetInventoryInfo () throws Exception {

        System.out.println(JSON.toJSONString(
                inventoryService.getInventoryInfo(userId),
                SerializerFeature.DisableCircularReferenceDetect //禁止 fastjson 级联循环引用
        ));
    }
}
