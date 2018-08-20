package com.spellchain.passbook.constant;

/**
 * <h1>基本常量定义</h1>
 * @author jackson.yu
 * @version 1.0 2018年08月20日
 * @since 1.8
 */
public class Constants {

    /** 商户优惠券 Kafka Topic */
    public static final String TEMPLATE_TOPIC = "merchants-template";

    /** token 文件存储目录 */
    public static final String TOKEN_DIR = "/tmp/token";

    /** 已使用的 token 文件名后缀 */
    public static final String USED_TOKEN_SUFFIX = "_";

    /** 用户数的 redis key */
    public static final String USE_COUNT_REDIS_KEY = "spellchain-user-count";

    /**
     * <h2>User Hbase Table</h2>
     **/
    public class UserTable{

        /** User Hbase 表名 */
        public static final String TABLE_NAME = "pb:user";
        /** 基本信息列族 */
        public static final String FAMILY_B = "b";
        /** 用户名 */
        public static final String NAME = "name";
        /** 年龄 */
        public static final String AGE = "age";
        /** 性别 */
        public static final String SEX = "sex";

        /** 额外信息族 */
        public static final String FAMILY_O = "o";
        /** 电话号码 */
        public static final String PHONE = "phone";
        /** 住址 */
        public static final String ADDRESS = "address";
    }
}
