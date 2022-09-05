package com.toolbox.vo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/5 14:42
 */
@Component
@ConfigurationProperties(prefix = "wx.config")
@Data
public class ConfigConstant {
    @Value("${wx-corp.corpId}")
    private String corpId;
    @Value("${wx-corp.agentId}")
    private String agentId;
    @Value("${wx-corp.secret}")
    private String secret;

    @Value("${weather.config.appid}")
    public String weatherAppId;

    @Value("${weather.config.appSecret}")
    public String weatherAppSecret;
    @Value("${weather.config.city}")
    public String city;
    @Value("${message.config.togetherDate}")
    public String togetherDate;
    @Value("${message.config.birthday1}")
    public  String birthday1;
    @Value("${message.config.birthday2}")
    public  String birthday2;

    @Value("${ApiSpace.token}")
    public String token;
}