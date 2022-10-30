package com.tomato_planet.backend;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
class TomatoPlanetBackendApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void test01() {
        // ArrayList ->  json格式的字符串   -> ArrayList/HashSet
        Gson gson = new Gson();
        List<String> strings = new ArrayList<>();
        System.out.println(strings.getClass());
        System.out.println(strings instanceof List);
        strings.add("打篮球");
        strings.add("踢足球");
        System.out.println(gson.toJson(strings).getClass());
        String s = gson.toJson(strings);
        HashSet set = gson.fromJson(s, HashSet.class);

        System.out.println(set);
        // ### jsonArray - list
        // String s1 = gson.toJson(strings, JsonArray.class);
        // JsonArray jsonElements = new JsonArray();
        // jsonElements.add("1");
        // jsonElements.add("2");
        // System.out.println("###@@@"+jsonElements);   ###@@@["1","2"]
        // System.out.println(jsonElements.getClass()); class com.google.gson.JsonArray
        // List list1 = gson.fromJson(jsonElements, List.class);
        // System.out.println(list1.get(0).getClass()); class java.lang.String
        // System.out.println(list1);   [1, 2]
        // System.out.println("###"+s1);


        // String s = gson.toJson(strings);
        // System.out.println(s);
        // System.out.println(s.getClass());
        // // ["打篮球","踢足球"]
        // // 会出错
        // // String s1 = gson.fromJson(s, String.class);
        // // System.out.println(s1);
        // Set<String> o = gson.fromJson(s, new TypeToken<Set<String>>() {}.getType());
        // System.out.println(o);
        // // [打篮球, 踢足球]
        // List list = gson.fromJson(s, List.class);
        // System.out.println(list);
        // [打篮球, 踢足球]
    }

    @Test
    void test02() {
        String s = "nihao.com";
        System.out.println(s.lastIndexOf("."));
        System.out.println(s.substring(s.lastIndexOf(".")));
    }

    @Test
    void test03() {
        A a = new A();
        System.out.println(a.a == a.b);
    }

    class A {
        private Integer a = 10000;
        private Integer b = 10000;
    }

}
