package com.spellchain.passbook.service;

import com.alibaba.fastjson.JSON;
import com.spellchain.passbook.constant.Constants;
import com.spellchain.passbook.vo.PassTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * <p>消费 kafka 中的 PassTemplate</p>
 * @author young
 * @version 1.0 2018年09月12日
 * @since JDK1.8
 */
@Slf4j
@Component
public class ConsumeTemplateService {

    /** pass 相关的 HBase 服务 */
    private final IHBasePassService passService;


    @Autowired
    public ConsumeTemplateService(IHBasePassService passService) {
        this.passService = passService;
    }

    @KafkaListener(topics = {Constants.TEMPLATE_TOPIC})
    public void receive(@Payload String passTemplate,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        log.info("Consumer Receive PassTemplate: {}", passTemplate);

        PassTemplate pt;

        try{
            pt = JSON.parseObject(passTemplate, PassTemplate.class);
        } catch (Exception e) {
            log.error("Pass PassTemplate Error: {}", e.getMessage());
            return;
        }

        log.info("DropPassTemplateToHBase: {}", passService.dropPassTemplateToHBase(pt));
    }
}
