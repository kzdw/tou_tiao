package com.zhousheng.tou_tiao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
//@PropertySource(value = "classpath:application.yml",encoding = "utf-8")
public class TouTiaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TouTiaoApplication.class, args);
	}

}
