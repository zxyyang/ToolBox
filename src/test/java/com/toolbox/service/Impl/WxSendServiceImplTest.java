package com.toolbox.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.toolbox.util.QuartzUtil;
import com.toolbox.vo.wx.RemindVo;
import org.junit.jupiter.api.Test;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/5 15:37
 */
@SpringBootTest
class WxSendServiceImplTest {

    @Autowired
    WxSendServiceImpl wxSendService;

    @Autowired
    Scheduler scheduler;

    @Test
    void testSend(){
        RemindVo remindVo = new RemindVo();
        remindVo.setTime("2022-9-16 16:56");
        remindVo.setContent("定时任务测试");
        remindVo.setType(1);
        QuartzUtil.startJobs(scheduler);
        List<RemindVo> remindList = wxSendService.getRemindList();
        System.err.println(JSONObject.toJSONString(remindList));

    }
}