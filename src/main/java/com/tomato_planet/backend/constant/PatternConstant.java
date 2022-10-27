package com.tomato_planet.backend.constant;

/**
 * 正则表达式
 *
 * @author jianping5
 * @create 2022/10/13 19:34
 */
public class PatternConstant {

    /**
     * 账号密码正则
     */
    public static final String WORD_NUMBER_PATTERN = "[a-zA-Z0-9_]{1,35}";

    /**
     * 手机号正则
     */
    public static final String PHONE_NUMBER_PATTERN = "^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\\d{8}$";

    /**
     * 邮箱正则
     */
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

}
