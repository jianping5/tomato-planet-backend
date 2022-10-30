package com.tomato_planet.backend.controller;

import com.tomato_planet.backend.common.BaseResponse;
import com.tomato_planet.backend.common.ResultUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jianping5
 * @createDate 2022/10/30 15:36
 */
@Deprecated
@RestController
@RequestMapping("/admin/user")
public class UserAdminController {

    @PostMapping("/login")
    public BaseResponse<Map<String, Object>> login() {
        Map<String, Object> map = new HashMap<>();
        map.put("token","admin-token");
        return ResultUtils.success(map);
    }
    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/info")
    public BaseResponse<Map<String, Object>> info() {
        Map<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("introduction", "I am a super administrator");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("name","Super Admin");
        return ResultUtils.success(map);
    }
    /**
     * 退出
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> logout(){
        return ResultUtils.success(true);
    }
}
