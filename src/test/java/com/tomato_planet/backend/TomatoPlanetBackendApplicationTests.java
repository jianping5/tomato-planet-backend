package com.tomato_planet.backend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tomato_planet.backend.model.entity.Topic;
import com.tomato_planet.backend.service.TopicService;
import com.tomato_planet.backend.util.TimeUtils;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;

@SpringBootTest
class TomatoPlanetBackendApplicationTests {

    @Resource
    private TopicService topicService;

    @Test
    void contextLoads() {
    }

    @Test
    void test01() {
        Gson gson = new Gson();
        int i = 1;
        String s1 = gson.toJson(i);
        System.out.println(gson.fromJson(s1, String.class));
        
        String ss = "123";
        String s = gson.toJson(ss);
        System.out.println(gson.fromJson(ss, Integer.class));
        System.out.println(ss);

        // java.lang.ClassCastException: com.tomato_planet.backend.TomatoPlanetBackendApplicationTests$A cannot be cast to com.google.gson.JsonElement
        /*String s2 = gson.toJson(new A());
        System.out.println(gson.toJson(new A(), JsonObject.class).getClass());

        System.out.println(s2);*/
        //java.util.ArrayList cannot be cast to com.google.gson.JsonElement
        /*ArrayList<String> strings = new ArrayList<>();
        strings.add("12");
        strings.add("123");
        System.out.println(gson.toJson(strings, JsonArray.class).getClass());*/



        // ArrayList ->  json格式的字符串   -> ArrayList/HashSet
        // Gson gson = new Gson();
        // List<String> strings = new ArrayList<>();
        // System.out.println(strings.getClass());
        // System.out.println(strings instanceof List);
        // strings.add("打篮球");
        // strings.add("踢足球");
        // System.out.println(gson.toJson(strings).getClass());
        // String s = gson.toJson(strings);
        // HashSet set = gson.fromJson(s, HashSet.class);
        //
        // System.out.println(set);
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

    @Test
    void test04() {
        // QueryWrapper<Topic> topicQueryWrapper = new QueryWrapper<>();
        // topicQueryWrapper.eq("id", 1);
        // List<Topic> list = topicService.list(topicQueryWrapper);
        // // 使用list查询，若数据库中不存在对应的topic，则会返回 []，不会返回null
        // System.out.println(list);
        //
        // Topic topic = topicService.getById(1);
        // // 若数据库中不存在对应的topic，则会返回null
        // System.out.println(topic);
    }

    @Test
    void test05() {
        WeekFields weekFields = WeekFields.ISO;
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now.get(weekFields.weekOfWeekBasedYear()));  // 按年第几周
        System.out.println(now.get(weekFields.weekOfMonth()));  // 按月第几周

        System.out.println(TimeUtils.isSameMonth(LocalDateTime.now(), LocalDateTime.now()));
    }

    @Test
    void test06() {
        Map<String, Integer> map = new HashMap<>();
        map.put("学习", 12);
        if (map.containsKey("学习")) {
            Integer study = map.get("学习");
            study += 15;
            map.put("学习", study);
        }
        System.out.println(map);
    }

    @Test
    void test07() {
    }

}
