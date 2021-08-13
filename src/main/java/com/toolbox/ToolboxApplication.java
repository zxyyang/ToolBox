package com.toolbox;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Version 1.0
 * @Author zixuan.yang
 * @Created 2021/8/13 17:35
 * @Description <p>
 * @Modification <p>
 * Date Author Version Description
 * <p>
 * 2021/8/13     zixuan.yang@hirain.com   1.0   create file
 */
@MapperScan
@SpringBootConfiguration
@SpringBootApplication

public class ToolboxApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToolboxApplication.class, args);
        System.out.println("  _______          _        ____            \n" +
                " |__   __|        | |      |  _ \\           \n" +
                "    | | ___   ___ | |______| |_) | _____  __\n" +
                "    | |/ _ \\ / _ \\| |______|  _ < / _ \\ \\/ /\n" +
                "    | | (_) | (_) | |      | |_) | (_) >  < \n" +
                "    |_|\\___/ \\___/|_|      |____/ \\___/_/\\_\\\n" +
                "                                            \n" +
                "                                            ");
    }

}
