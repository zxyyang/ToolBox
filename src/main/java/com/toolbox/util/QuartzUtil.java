package com.toolbox.util;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/7 19:10
 */
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import com.alibaba.fastjson.JSONObject;
import com.toolbox.vo.quartz.QuartzJobsVO;
import com.toolbox.vo.wx.RemindVo;
import org.quartz.*;
import org.quartz.impl.JobExecutionContextImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;


/**
 * Quartz调度管理器
 *
 */
@Component
public class QuartzUtil {
    private static String JOB_GROUP_NAME = "默认任务分组";
    private static String TRIGGER_GROUP_NAME = "默认触发器分组";


    /**
     * @param sched   调度器
     * @param jobName 任务名
     * @param cls     任务
     * @param params  任务参数
     * @param time    时间设置，参考quartz说明文档
     * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
     * @Title: QuartzManager.java
     */
    public static void addJob(Scheduler sched, String jobName, @SuppressWarnings("rawtypes") Class cls, Object params,
                              String time) {
        try {
            JobKey jobKey = new JobKey(jobName, JOB_GROUP_NAME);// 任务名，任务组，任务执行类
            @SuppressWarnings("unchecked")

            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("params", params);
            JobDetail jobDetail = newJob(cls).withIdentity(jobKey).setJobData(jobDataMap).build();
            TriggerKey triggerKey = new TriggerKey(jobName, TRIGGER_GROUP_NAME);// 触发器

            Trigger trigger = newTrigger().withIdentity(triggerKey).withSchedule(cronSchedule(time)).build();// 触发器时间设定
            sched.scheduleJob(jobDetail, trigger);
            if (!sched.isShutdown()) {
                sched.start();// 启动
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param sched            调度器
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass         任务
     * @param params           任务参数
     * @param time             时间设置，参考quartz说明文档
     * @Description: 添加一个定时任务
     * @Title: QuartzManager.java
     */
    public static void addJob(Scheduler sched, String jobName, String jobGroupName, String triggerName,
                              String triggerGroupName, @SuppressWarnings("rawtypes") Class jobClass, Object params, String time) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroupName);
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("params", params);
            @SuppressWarnings("unchecked")
            JobDetail jobDetail = newJob(jobClass).withIdentity(jobKey).setJobData(jobDataMap).build();
            // 触发器
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
            Trigger trigger = newTrigger().withIdentity(triggerKey).withSchedule(cronSchedule(time)).build();
            sched.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param sched   调度器
     * @param jobName
     * @param time
     * @Description: 修改一个任务的触发时间(使用默认的任务组名 ， 触发器名 ， 触发器组名)
     * @Title: QuartzManager.java
     */
    @SuppressWarnings("rawtypes")
    public static void modifyJobTime(Scheduler sched, String jobName, String time) {
        try {
            TriggerKey triggerKey = new TriggerKey(jobName, TRIGGER_GROUP_NAME);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                JobKey jobKey = new JobKey(jobName, JOB_GROUP_NAME);
                JobDetail jobDetail = sched.getJobDetail(jobKey);
                Class objJobClass = jobDetail.getJobClass();
                Object params = jobDetail.getJobDataMap().get("params");
                removeJob(sched, jobName);
                System.out.println("修改任务：" + jobName);
                addJob(sched, jobName, objJobClass, params, time);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param sched            调度器 *
     * @param sched            调度器
     * @param triggerName
     * @param triggerGroupName
     * @param time
     * @Description: 修改一个任务的触发时间
     * @Title: QuartzManager.java
     */
    public static void modifyJobTime(Scheduler sched, String triggerName, String triggerGroupName, String time) {
        try {
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                // 修改时间
                trigger.getTriggerBuilder().withSchedule(cronSchedule(time));
                // 重启触发器
                sched.resumeTrigger(triggerKey);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param sched   调度器
     * @param jobName
     * @Description: 移除一个任务(使用默认的任务组名 ， 触发器名 ， 触发器组名)
     * @Title: QuartzManager.java
     */
    public static void removeJob(Scheduler sched, String jobName) {
        try {
            TriggerKey triggerKey = new TriggerKey(jobName, TRIGGER_GROUP_NAME);
            sched.pauseTrigger(triggerKey);// 停止触发器
            sched.unscheduleJob(triggerKey);// 移除触发器
            JobKey jobKey = new JobKey(jobName, JOB_GROUP_NAME);
            sched.deleteJob(jobKey);// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param sched            调度器
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     * @Description: 移除一个任务
     * @Title: QuartzManager.java
     */
    public static void removeJob(Scheduler sched, String jobName, String jobGroupName, String triggerName,
                                 String triggerGroupName) {
        try {
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
            sched.pauseTrigger(triggerKey);// 停止触发器
            sched.unscheduleJob(triggerKey);// 移除触发器
            JobKey jobKey = new JobKey(jobName, jobGroupName);
            sched.deleteJob(jobKey);// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param sched 调度器
     * @Description:启动所有定时任务
     * @Title: QuartzManager.java
     */
    public static void startJobs(Scheduler sched) {
        try {
            sched.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param sched 调度器
     * @Description:关闭所有定时任务
     */
    public static void shutdownJobs(Scheduler sched) {
        try {
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<QuartzJobsVO> getAllJobs(Scheduler scheduler){
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = null;
        List<QuartzJobsVO> jobList = new ArrayList();
        try {
            jobKeys = scheduler.getJobKeys(matcher);
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    Object params = scheduler.getJobDetail(jobKey).getJobDataMap().get("params");
                    QuartzJobsVO job = new QuartzJobsVO();
                    job.setParams(params);
                    job.setJobDetailName(jobKey.getName());
                    job.setGroupName(jobKey.getGroup());
                    job.setJobCronExpression("触发器:" + trigger.getKey());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    job.setStatus(triggerState.name());
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        job.setJobCronExpression(cronExpression);
                        job.setTime(DateToCron.parseDate(DateToCron.nextTime(cronExpression,new Date())));
                    }
                    jobList.add(job);

                }
            }

        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return jobList;

    }
}



