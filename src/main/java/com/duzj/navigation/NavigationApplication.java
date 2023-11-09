package com.duzj.navigation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.duzj.navigation.mapper")
public class NavigationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NavigationApplication.class, args);
    }

}
