package com.tomato_planet.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tomato_planet.backend.mapper")
public class TomatoPlanetBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TomatoPlanetBackendApplication.class, args);
    }

}
