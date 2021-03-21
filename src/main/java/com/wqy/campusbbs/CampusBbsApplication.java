package com.wqy.campusbbs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @SpringBootApplication 来标注一个主程序类，说明这是一个Spring Boot应用
 */
@SpringBootApplication
@MapperScan("com.wqy.campusbbs.mapper")
public class CampusBbsApplication {

    public static void main(String[] args) {
        //Spring应用启动起来
        SpringApplication.run(CampusBbsApplication.class, args);
    }
}
