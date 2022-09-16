package com.toolbox.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.toolbox.enums.CronFormate;
import org.quartz.CronExpression;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/8/31 20:32
 */
@Component
public class DateToCron {
    public static String DateToCron(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(CronFormate.DATEFORMAT_YEAR);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }


    public static String DateToCron(String date) {
        DateTime dateTime = DateUtil.parse(date);
        Date parse = DateUtil.parse(dateTime.toString(), "yyyy-MM-dd HH:mm:ss");
        return DateToCron(parse);

    }

    /**
     * 将日期转换为cron时间
     *
     * @param date
     * @return
     */
    public static String parseCron(Date date) {
        String format="ss mm HH dd MM ? yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 获取下一次的执行时间
     *
     * @param cron
     * @return
     * @throws ParseException
     */
    public static Date nextTime(String cron, Date date) throws ParseException {
        // 加载包之后直接引用这个方法
        CronExpression cronExpression = new CronExpression(cron);
        // 转换 new Date 是为了给最近一次执行时间一个初始时间，这里给当前时间
        Date nextTime = cronExpression.getNextValidTimeAfter(date);
        return nextTime;
    }

    /**
     * 获取10个下次执行时间,如果不足10次,则返回实际次数
     * @param cron
     * @param date
     * @return
     * @throws ParseException
     */
    public static List<Date> next10Times(String cron, Date date) throws ParseException {
        return nextTimes(cron,date,10);
    }

    /**
     * 获取n个下次执行时间,如果不足n次,则返回实际次数
     * @param cron
     * @param date
     * @param n
     * @return
     * @throws ParseException
     */
    public static List<Date> nextTimes(String cron, Date date, int n) throws ParseException {
        List<Date> nextTimes = new ArrayList<>();
        Date nextTime = date;
        for (int i = 0; i < n; i++) {
            Date date1 = nextTime(cron, nextTime);
            if (null == date1) {
                break;
            }
            nextTimes.add(date1);
            nextTime = date1;
        }
        return nextTimes;
    }

    /**
     * 时间转字符串
     * @param date
     * @return
     */
    public static String parseDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateFormat = sdf.format(date);
        return dateFormat;
    }

    /**
     * 字符串转时间
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date toDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date dateFormatParse = sdf.parse(dateString);
        return dateFormatParse;
    }

}
