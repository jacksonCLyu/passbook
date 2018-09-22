package com.spellchain.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Controller 统一的响应</p>
 * @author young
 * @version 1.0 2018年09月22日
 * @since JDK1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    /** 错误码： 正确返回 0 */
    private Integer errorCode = 0;

    /** 错误信息： 正确返回空字符串 */
    private String errorMsg = "";

    /** 返回值对象 */
    private Object data;

    /**
     * 正确的响应构造函数
     * @param data 返回值对象
     */
    public Response(Object data) {
        this.data = data;
    }

    /**
     * 空响应
     * @return Response
     */
    public static Response success() {
        return new Response();
    }

    /**
     * 错误响应
     * @param errorMsg 错误信息
     * @return Response
     */
    public static Response failure(String errorMsg) {
        return new Response(-1, errorMsg, null);
    }
}
