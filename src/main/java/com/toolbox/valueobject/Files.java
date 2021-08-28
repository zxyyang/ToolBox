package com.toolbox.valueobject;

import lombok.Data;

@Data
public class Files {

    private String name;

    private Long size;

    private String mimeType;

    private String putTime;

    private String endUser;

}