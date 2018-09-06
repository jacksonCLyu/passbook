package com.spellchain.passbook.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>日志对象</h1>
 * @author jackson.yu
 * @version 1.0 2018年09月06日
 * @since 1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class LogObject {

    /** 日志类型 */
    private String action;

    /** 用户 id */
    private Long userId;

    /** 当前时间戳 */
    private Long timestamp;

    /** 客户端 ip 地址 */
    private String remoteIp;

    /** 日志信息 */
    private Object info;
}
