package com.toolbox.config.quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * 应用模块名称:
 * 代码描述:
 * Copyright: Copyright (C) 2021, Inc. All rights reserved.
 *
 *
 * @author: zixuan.yang
 * @since: 2021/11/18 19:14
 */
@Component
public class JobFactory extends AdaptableJobFactory {

	@Autowired
	private AutowireCapableBeanFactory capableBeanFactory;

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		Object jobInstance = super.createJobInstance(bundle);
		// 使用AutowireCapableBeanFactory完成对IOC外Bean的注入操作
		capableBeanFactory.autowireBean(jobInstance);
		return jobInstance;
	}
}
