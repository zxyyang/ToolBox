package com.toolbox.vo.quartz;

import lombok.Data;

import java.io.Serializable;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/16 17:05
 */
@Data
public class QuartzJobsVO implements Serializable {
    private String jobDetailName;
    private String jobCronExpression;
    private String groupName;
    private String status;
    private String time;
    private Object params;
}
