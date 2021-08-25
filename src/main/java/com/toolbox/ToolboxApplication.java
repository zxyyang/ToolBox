package com.toolbox;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.toolbox")
@MapperScan("com.toolbox.mapper")
@SpringBootConfiguration()
@EnableSwagger2
public class ToolboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToolboxApplication.class, args);
		System.out.println(" __________  ________ ________  _______  ____  _  __\n" + "/_  __/ __ \\/_  __/ // / __/  |/  / __ \\/ __ \\/ |/ /\n"
				+ " / / / /_/ / / / / _  / _// /|_/ / /_/ / /_/ /    / \n" + "/_/  \\____/ /_/ /_//_/___/_/  /_/\\____/\\____/_/|_/  \n"
				+ "                                                    ");
	}

}
