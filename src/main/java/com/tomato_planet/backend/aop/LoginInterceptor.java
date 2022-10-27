package com.tomato_planet.backend.aop;

import com.tomato_planet.backend.model.entity.User;
import com.tomato_planet.backend.util.UserHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.tomato_planet.backend.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 登录拦截器
 *
 * @author jianping5
 * @createDate 2022/10/26 23:34
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取session中的user对象
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        // 若不存在，说明当前用户未登录，则返回444
        if (userObj == null) {
            response.setStatus(444);
            return false;
        }
        // 存在，则存入 UserHolder 中，方便获取
        User user = (User) userObj;
        UserHolder.setUser(user);
        // 放行
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 一次请求完全结束后，清空UserHolder中的数据
        UserHolder.clear();
    }
}
