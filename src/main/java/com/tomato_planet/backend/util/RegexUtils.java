package com.tomato_planet.backend.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tomato_planet.backend.constant.PatternConstant.*;

/**
 * @author jianping5
 * @create 2022/10/13 21:11
 */
public class RegexUtils {

    /**
     * 判断用户账号是否合法
     * @param userAccount
     * @return
     */
    public static boolean isUserAccountValid(String userAccount) {
        return match(userAccount, WORD_NUMBER_PATTERN);
    }

    /**
     * 判断用户密码是否合法
     * @param userPassword
     * @return
     */
    public static boolean isUserPasswordValid(String userPassword) {
        return match(userPassword, WORD_NUMBER_PATTERN);
    }

    /**
     * 判断用户手机号是否合法
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        return match(phoneNumber, PHONE_NUMBER_PATTERN);
    }

    /**
     * 判断用户邮箱是否合法
     * @param userEmail
     * @return
     */
    public static boolean isEmailValid(String userEmail) {
        return match(userEmail, EMAIL_PATTERN);
    }


    /**
     * 判断字符串是否符合正则表达式的规则
     * @param str
     * @param pattern
     * @return
     */
    private static boolean match(String str, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(str);
        return matcher.matches();
    }
}
