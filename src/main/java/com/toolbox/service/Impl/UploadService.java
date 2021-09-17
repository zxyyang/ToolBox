package com.toolbox.service.Impl;


import com.toolbox.util.Uploader;
import org.springframework.stereotype.Service;

@Service
public class UploadService extends Uploader {
    public UploadService() {
        super("E:/AAAAAAAAAAAAAA/", "file");
    }
}