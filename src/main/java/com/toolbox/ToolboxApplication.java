package com.toolbox;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@MapperScan("com.toolbox.mapper")
@SpringBootConfiguration()
@SpringBootApplication
@EnableSwagger2
public class ToolboxApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToolboxApplication.class, args);
        System.out.println("███████╗██╗  ██╗██╗   ██╗ █████╗ ███╗   ██╗ ██████╗     ██████╗███╗   ██╗\n" +
                "╚══███╔╝╚██╗██╔╝╚██╗ ██╔╝██╔══██╗████╗  ██║██╔════╝    ██╔════╝████╗  ██║\n" +
                "  ███╔╝  ╚███╔╝  ╚████╔╝ ███████║██╔██╗ ██║██║  ███╗   ██║     ██╔██╗ ██║\n" +
                " ███╔╝   ██╔██╗   ╚██╔╝  ██╔══██║██║╚██╗██║██║   ██║   ██║     ██║╚██╗██║\n" +
                "███████╗██╔╝ ██╗   ██║   ██║  ██║██║ ╚████║╚██████╔╝██╗╚██████╗██║ ╚████║\n" +
                "╚══════╝╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═══╝ ╚═════╝ ╚═╝ ╚═════╝╚═╝  ╚═══╝\n" +
                "                                                                         ");
    }

}
