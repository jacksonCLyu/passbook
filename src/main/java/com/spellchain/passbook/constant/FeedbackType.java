package com.spellchain.passbook.constant;

/**
 * <h1>评论类型枚举</h1>
 * @author jackson.yu
 * @version 1.0 2018年08月20日
 * @since 1.8
 */
public enum FeedbackType {

    PASS(1, "针对优惠券的评论"),
    APP(1, "针对卡包 App 的评论");

    /** 评论类型编码 */
    private Integer code;

    /** 评论类型描述 */
    private String desc;

    FeedbackType(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode(){
        return this.code;
    }

    public String getDesc(){
        return this.desc;
    }
}
