package com.tomato_planet.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tomato_planet.backend.model.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author jianping5
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2022-10-24 11:52:01
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount
     * @param userEmail
     * @param emailVerifyCode
     * @return
     */
    Long userRegister(String userAccount, String userEmail, String emailVerifyCode, HttpServletRequest request);

    /**
     * 发送验证码到指定邮箱
     * @param userEmail
     * @param request
     * @return
     */
    boolean sendVerifyCode(String userEmail, HttpServletRequest request);

    /**
     * 用户登录
     *
     * @param loginType
     * @param userIdentification
     * @param pwdOrVerifyCode
     * @return
     */
    User userLogin(int loginType, String userIdentification, String pwdOrVerifyCode, HttpServletRequest request);

    /**
     * 用户退出
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 找回密码
     * @param userEmail
     * @param verifyCode
     * @param newPassword
     */
    boolean recoverPassword(String userEmail, String verifyCode, String newPassword, HttpServletRequest request);

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @param newConfirmPassword
     */
    boolean changePassword(String oldPassword, String newPassword, String newConfirmPassword);

    /**
     * 设置密码
     * @param password
     * @param confirmPassword
     * @return
     */
    boolean setPassword(String password, String confirmPassword);
}
