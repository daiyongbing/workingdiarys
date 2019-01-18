package com.iscas.workingdiarys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.iscas.workingdiarys.mapper")
public class WorkingdiarysApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkingdiarysApplication.class, args);
    }

}

