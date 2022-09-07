package com.toolbox.job;

import com.toolbox.service.WxSendService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/7 19:03
 */
public class MemoJob implements Job {

    @Autowired
    private WxSendService wxSendService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String param = (String) jobDataMap.get("params");
        System.err.println("job执行,参数："+param);
    }
}
