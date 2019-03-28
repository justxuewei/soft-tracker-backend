package com.niuxuewei.lucius;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.niuxuewei.lucius.mapper")
public class LuciusApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuciusApplication.class, args);
    }

}

