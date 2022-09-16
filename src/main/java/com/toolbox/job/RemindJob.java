package com.toolbox.job;

import com.alibaba.fastjson.JSONObject;
import com.toolbox.service.WxSendService;
import com.toolbox.vo.wx.RemindVo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/7 19:03
 */
@Slf4j
@Component
public class RemindJob implements Job {

    @Autowired
    private WxSendService wxSendService;

    @Autowired
    private Scheduler scheduler;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("备忘录提醒开始执行.....");
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        RemindVo params = JSONObject.parseObject(JSONObject.toJSONString(jobDataMap.get("params")), RemindVo.class);
        wxSendService.sendCorpWxNoteMsg(params.getContent(), params.getType());
        try {
            scheduler.deleteJob(jobExecutionContext.getJobDetail().getKey());
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        log.info("备忘录提醒执行完成！");
    }
}
