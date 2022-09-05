package com.toolbox.controller;

import com.toolbox.service.WxSendService;
import com.toolbox.valueobject.RequestBean;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/5 17:45
 */
@Slf4j
@RestController
@Api(value = "/wx", tags = { "企业微信推送" }, description = "企业微信推送")
@RequestMapping("/wx")
public class WxCorpController {

    @Autowired
    private WxSendService wxSendService;

    /**
     * 获取Token
     * 每天早上7：30执行推送
     * @return
     */
    @Scheduled(cron = "0 30 7 * * ?")
    @GetMapping("/sendMorning")
    public RequestBean<String> sendMorning() {
        log.info("早晨推送启动！");
        try {
            String s = wxSendService.sendCorpWxMorningMsg();
            return  RequestBean.Success(s);
        }
        catch (Exception e){
            return RequestBean.Error(e.getMessage());
         }
    }

    /**
     * 获取Token
     * 每天早上7：30执行推送
     * @return
     */
    @Scheduled(cron = "0 0 21 * * ?")
    @GetMapping("/sendNight")
    public RequestBean<String> sendNight() {
        log.info("晚上推送启动！");
        try {
            String s = wxSendService.sendCorpWxNightMsg();
            return  RequestBean.Success(s);
        }
        catch (Exception e){
            return RequestBean.Error(e.getMessage());
        }
    }
}
