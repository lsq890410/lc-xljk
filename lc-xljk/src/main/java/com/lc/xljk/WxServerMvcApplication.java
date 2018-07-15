package com.lc.xljk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@ImportResource("classpath:transaction.xml")
@MapperScan("com.lc.xljk")
@Controller
public class WxServerMvcApplication {
	public static void main(String[] args) {
		SpringApplication.run(WxServerMvcApplication.class, args);
	}
}
