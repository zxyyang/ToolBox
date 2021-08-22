package com.toolbox.valueobject;

import lombok.Data;

/***
 *
 * 文件上传对象
 *
 */
@Data
public class UploadFile {
    private String fileName;
    private String url;

    public UploadFile(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }

}
