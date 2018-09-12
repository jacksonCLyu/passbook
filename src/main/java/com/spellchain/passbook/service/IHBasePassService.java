package com.spellchain.passbook.service;

import com.spellchain.passbook.vo.PassTemplate;

/**
 * <p>Pass HBase 服务</p>
 * @author young
 * @version 1.0 2018年9月12日
 * @since JDK1.8
 */
public interface IHBasePassService {

    /**
     * 将 PassTemplate 写入 HBase 对象
     * @param passTemplate {@link PassTemplate}
     * @return true/false
     */
    boolean dropPassTemplateToHBase(PassTemplate passTemplate);
}
