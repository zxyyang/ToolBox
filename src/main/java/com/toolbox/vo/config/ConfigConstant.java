package com.toolbox.vo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/5 14:42
 */
@Component
@Data
public class ConfigConstant {
    @Value("${wx-corp.corpId}")
    private String corpId;
    @Value("${wx-corp.weatherId}")
    private String weatherId;
    @Value("${wx-corp.remindId}")
    private String remindId;
    @Value("${wx-corp.weatherSecret}")
    private String weatherSecret;
    @Value("${wx-corp.remindSecret}")
    private String remindSecret;

    @Value("${weather.config.appid}")
    private String weatherAppId;

    @Value("${weather.config.appSecret}")
    private String weatherAppSecret;
    @Value("${weather.config.city}")
    private String city;
    @Value("${message.config.togetherDate}")
    private String togetherDate;
    @Value("${message.config.birthday1}")
    private  String birthday1;
    @Value("${message.config.birthday2}")
    private  String birthday2;

    @Value("${ApiSpace.token}")
    private String token;

    @Value("${wx-corp.token}")
    private String HdToken;

    @Value("${wx-corp.EncodingAESKey}")
    private String encodingAESKey;

    @Value("${wx-corp.man}")
    private String man;
    @Value("${wx-corp.woman}")
    private String woman;
}
