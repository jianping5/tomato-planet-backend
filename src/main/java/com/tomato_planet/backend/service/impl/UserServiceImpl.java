package com.tomato_planet.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tomato_planet.backend.common.StatusCode;
import com.tomato_planet.backend.exception.BusinessException;
import com.tomato_planet.backend.model.entity.User;
import com.tomato_planet.backend.service.UserService;
import com.tomato_planet.backend.mapper.UserMapper;
import com.tomato_planet.backend.util.RegexUtils;
import com.tomato_planet.backend.util.UserHolder;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.tomato_planet.backend.constant.EmailConstant.EMAIL_SENDER;
import static com.tomato_planet.backend.constant.NumberConstant.*;
import static com.tomato_planet.backend.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author jianping5
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2022-10-24 11:52:01
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    JavaMailSender mailSender;

    /**
     * 加密盐值
     */
    public static final String SALT = "tomato_planet";

    @Override
    public Long userRegister(String userAccount, String userEmail, String emailVerifyCode, HttpServletRequest request) {
        // 检验账号格式
        if (userAccount.length() < USER_ACCOUNT_LENGTH_LOW ) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户账号长度过短");
        }
        if (userAccount.length() > USER_ACCOUNT_LENGTH_HIGH) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户账号长度过长");
        }
        if (!RegexUtils.isUserAccountValid(userAccount)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户账号包含违规字符");
        }

        // 检验邮箱格式
        if (!RegexUtils.isEmailValid(userEmail)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户邮箱格式不规范");
        }

        // 校验验证码是否一致
        HttpSession session = request.getSession();
        String checkCode = (String) session.getAttribute(userEmail);
        if (!emailVerifyCode.equals(checkCode)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "验证码错误");
        }
        // 成功匹配，立马删除session中保存的验证码
        session.removeAttribute(userEmail);

        // 判断用户账户是否重复
        QueryWrapper<User> queryWrapperAccount = new QueryWrapper();
        queryWrapperAccount.eq("user_account", userAccount);
        long countAccount = this.count(queryWrapperAccount);
        if (countAccount != 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户账号不能重复");
        }

        // 判断邮箱是否重复
        QueryWrapper<User> queryWrapperEmail = new QueryWrapper<>();
        queryWrapperEmail.eq("user_email", userEmail);
        long countEmail = this.count(queryWrapperEmail);
        if (countEmail != 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户邮箱不能重复");
        }

        // 3. 插入数据（并初始化一些数据）
        User user = getOriginUser(userAccount, userEmail);
        boolean result = this.save(user);
        if (!result) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "注册失败");
        }
        return user.getId();
    }

    @Override
    public boolean sendVerifyCode(String userEmail, HttpServletRequest request) {
        // 生成验证码
        String emailServiceCode = RandomStringUtils.random(6, false, true);
        // 获取session，并将验证码保存到session作用域中
        HttpSession session = request.getSession();
        Object emailConfirmCodeObj = session.getAttribute(userEmail);
        // 将之前存在的验证码去除掉
        if (emailConfirmCodeObj != null) {
            session.removeAttribute(userEmail);
        }
        session.setAttribute(userEmail, emailServiceCode);
        // 发送邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("番茄星球注册");
        message.setText("注册验证码是：" + emailServiceCode);
        message.setFrom(EMAIL_SENDER);
        message.setTo(userEmail);
        try {
            mailSender.send(message);
            return true;
        } catch (MailException e) {
            log.error("sendEmailException: ", e);
            return false;
        }
    }

    @Override
    public User userLogin(int loginType, String userIdentification, String pwdOrVerifyCode, HttpServletRequest request) {
        User user = null;
        HttpSession session = request.getSession();
        // 免密登录
        if (loginType == 0) {
            String userEmail = userIdentification;
            String verifyCode = pwdOrVerifyCode;

            // 校验验证码是否一致
            String checkCode = (String) session.getAttribute(userEmail);
            if (!verifyCode.equals(checkCode)) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "验证码错误");
            }
            // 成功匹配，立马删除session中保存的验证码
            session.removeAttribute(userEmail);

            // 检验邮箱格式
            if (!RegexUtils.isEmailValid(userEmail)) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "用户邮箱格式不规范");
            }

            // 根据用户邮箱查询用户
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("user_email", userEmail);
            user = this.getOne(userQueryWrapper);
        }

        // 密码登录
        if (loginType == 1) {
            String userAccount = userIdentification;
            String userPassword = pwdOrVerifyCode;

            // 检验账号格式
            if (userAccount.length() < USER_ACCOUNT_LENGTH_LOW ) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "用户账号长度过短");
            }
            if (userAccount.length() > USER_ACCOUNT_LENGTH_HIGH) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "用户账号长度过长");
            }
            if (!RegexUtils.isUserAccountValid(userAccount)) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "用户账号包含违规字符");
            }

            // 检验密码格式
            if (userPassword.length() < USER_PASSWORD_LENGTH_LOW ) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "用户密码长度过短");
            }
            if (userPassword.length() > USER_PASSWORD_LENGTH_HIGH ) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "用户密码长度过长");
            }
            if (!RegexUtils.isUserPasswordValid(userPassword)) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "用户密码包含违规字符");
            }

            // 加密
            String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

            // 查询
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_account", userAccount);
            user = this.getOne(queryWrapper);
            if (user.getUserPassword() == null) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "未设置密码");
            }
            if (!user.getUserPassword().equals(encryptedPassword)) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "账号密码不匹配");
            }
        }

        // 对用户信息脱敏
        User safetyUser = getSafetyUser(user);

        // 记录用户的登录态
        session.setAttribute(USER_LOGIN_STATE, safetyUser);

        // 返回safetyUser
        return safetyUser;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public boolean recoverPassword(String userEmail, String verifyCode, String newPassword, HttpServletRequest request) {
        // 检验邮箱格式
        if (!RegexUtils.isEmailValid(userEmail)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户邮箱格式不规范");
        }

        // 校验验证码是否一致
        HttpSession session = request.getSession();
        String checkCode = (String) session.getAttribute(userEmail);
        if (!verifyCode.equals(checkCode)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "验证码错误");
        }
        // 成功匹配，立马删除session中保存的验证码
        session.removeAttribute(userEmail);

        // 检验密码格式
        if (newPassword.length() < USER_PASSWORD_LENGTH_LOW ) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户密码长度过短");
        }
        if (newPassword.length() > USER_PASSWORD_LENGTH_HIGH ) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户密码长度过长");
        }
        if (!RegexUtils.isUserPasswordValid(newPassword)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户密码包含违规字符");
        }

        // 加密
        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + newPassword).getBytes());

        // 修改对应邮箱用户的密码
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("user_email", userEmail);
        userUpdateWrapper.set("user_password", encryptedPassword);
        boolean result = this.update(userUpdateWrapper);
        if (!result) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "更新用户密码失败");
        }
        return true;
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword, String newConfirmPassword) {

        // 检验密码格式
        if (newPassword.length() < USER_PASSWORD_LENGTH_LOW) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户密码长度过短");
        }
        if (newPassword.length() > USER_PASSWORD_LENGTH_HIGH ) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户密码长度过长");
        }
        if (!RegexUtils.isUserPasswordValid(newPassword)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户密码包含违规字符");
        }

        // 判断两次密码是否一致
        if (!newConfirmPassword.equals(newPassword)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "两次密码输入不一致");
        }

        // 判断旧与新密码是否重复
        if (oldPassword.equals(newPassword)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "新密码与旧密码重复");
        }

        // 获取当前登录用户
        User currentUser = UserHolder.getUser();
        Long id = currentUser.getId();
        User user = this.getById(id);
        // 判断是否设置过密码
        if (user.getUserPassword() == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "未设置密码");
        }

        // 加密
        String encryptedOldPassword = DigestUtils.md5DigestAsHex((SALT + oldPassword).getBytes());
        // 判断旧密码是否为当前登录用户的
        if (!user.getUserPassword().equals(encryptedOldPassword)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "旧密码输入错误");
        }

        // 对新密码进行加密，并修改数据库中的密码
        String encryptedNewPassword = DigestUtils.md5DigestAsHex((SALT + newPassword).getBytes());
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.set("user_password", encryptedNewPassword);
        boolean result = this.update(userUpdateWrapper);
        if (!result) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "密码修改失败");
        }
        return true;
    }

    @Override
    public boolean setPassword(String password, String confirmPassword) {

        // 检验密码格式
        if (password.length() < USER_PASSWORD_LENGTH_LOW) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户密码长度过短");
        }
        if (password.length() > USER_PASSWORD_LENGTH_HIGH ) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户密码长度过长");
        }
        if (!RegexUtils.isUserPasswordValid(password)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户密码包含违规字符");
        }

        // 判断两次密码是否一致
        if (!confirmPassword.equals(password)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "两次密码输入不一致");
        }

        // 获取当前登录用户
        User currentUser = UserHolder.getUser();
        Long id = currentUser.getId();
        User user = this.getById(id);

        if (user.getUserPassword() != null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "已设密码");
        }

        // 加密
        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        // 设置用户密码（在数据库中）
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.set("user_password", encryptedPassword);
        boolean result = this.update(userUpdateWrapper);
        if (!result) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "设置密码错误");
        }
        return true;
    }

    /**
     * 注册时初始化User
     * @param userAccount
     * @param userEmail
     * @return
     */
    private User getOriginUser(String userAccount, String userEmail) {
        User user = new User();
        user.setUserAccount(userAccount);
        String random = "tomato_" + RandomStringUtils.random(7, true, true);
        user.setUserName(random);
        user.setAvatarUrl("https://img-blog.csdnimg.cn/img_convert/b573b00bed7126db2c209ed01eb35189.png");
        user.setUserEmail(userEmail);
        return user;
    }

    /**
     * 登录时获取脱敏后的User
     */
    private User getSafetyUser(User user) {
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setUserName(user.getUserName());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setUserEmail(user.getUserEmail());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setUserCreateTime(user.getUserCreateTime());
        safetyUser.setIsdelete(user.getIsdelete());
        return safetyUser;
    }
}




