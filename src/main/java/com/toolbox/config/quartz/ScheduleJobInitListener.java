package com.toolbox.config.quartz;

import com.toolbox.util.QiNiuUtil;
import com.toolbox.util.QuartzUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 应用模块名称:
 * 代码描述:
 * Copyright: Copyright (C) 2021, Inc. All rights reserved.
 * 
 *
 * @author: zixuan.yang
 * @since: 2021/11/18 19:16
 */
@Component
@Order(1)
@Slf4j
public class ScheduleJobInitListener implements ApplicationRunner {

	@Autowired
	private Scheduler scheduler;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
			scheduler.start();
			log.info("\n\n\n===========================启动所有定时任务===========================\n\n\n");
		}catch (Exception e){
			log.error("定时任务启动失败：{}",e.getMessage());
		}

	}
}
