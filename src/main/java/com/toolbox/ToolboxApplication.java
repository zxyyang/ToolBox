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
		System.out.println("  __  __                   ____             \n" + " |  \\/  |                 / __ \\            \n"
				+ " | \\  / | ___  _ __ ___  | |  | |_ __   ___ \n" + " | |\\/| |/ _ \\| '__/ _ \\ | |  | | '_ \\ / _ \\\n"
				+ " | |  | | (_) | | |  __/ | |__| | | | |  __/\n" + " |_|  |_|\\___/|_|  \\___|  \\____/|_| |_|\\___|\n"
				+ "                                            \n" + "                                            ");
	}

}
