package com.tomato_planet.backend.util;


import com.tomato_planet.backend.model.entity.User;

/**
 * @author jianping5
 * @create 2022/10/10 21:56
 */

public class UserHolder {

    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user){
        userHolder.set(user);
    }

    public static User getUser(){
        return userHolder.get();
    }

    public static void clear(){
        userHolder.remove();
    }

}
