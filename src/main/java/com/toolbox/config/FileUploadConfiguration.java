package com.toolbox.config;

import com.toolbox.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

/***
 * 为了在测试时获得干净的测试数据，同时也为了在应用启动后分配好上传文件存储地址，
 * 我们需要在config包下创建一个配置类，
 * 在应用启动时调用FileStorageService中的clear()方法和init()方法。
 * 实现该功能，最快的方式是配置类实现CommandLineRunner接口类的run()方法
 */
public class FileUploadConfiguration implements CommandLineRunner {

    @Autowired
    FileStorageService fileStorageService;

    @Override
    public void run(String... args) throws Exception {
        fileStorageService.clear();
        fileStorageService.init();
    }
}
