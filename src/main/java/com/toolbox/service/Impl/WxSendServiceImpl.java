package com.toolbox.service.Impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.job.RemindJob;
import com.toolbox.service.ProverbService;
import com.toolbox.service.TianQiService;
import com.toolbox.service.WxSendService;
import com.toolbox.util.DateToCron;
import com.toolbox.util.DateUtils;
import com.toolbox.util.HttpUtil;
import com.toolbox.util.QuartzUtil;
import com.toolbox.vo.config.ConfigConstant;
import com.toolbox.vo.quartz.QuartzJobsVO;
import com.toolbox.vo.wx.RemindVo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/5 15:01
 */
@Service
@Slf4j
public class WxSendServiceImpl implements WxSendService {
    @Autowired
    private ConfigConstant configConstant;

    @Autowired
    private TianQiService tianQiService;

    @Autowired
    private DateUtils dateUtil;

    @Autowired
    private ProverbService proverbService;

    @Autowired
    Scheduler scheduler;


    private String getCorpToken(String secret){
        String param  = "corpid="+configConstant.getCorpId()+"&corpsecret="+secret;
        String token = HttpUtil.sendGet(" https://qyapi.weixin.qq.com/cgi-bin/gettoken", param);
        JSONObject object = JSONObject.parseObject(token);
        String access_token = object.getString("access_token");
        return access_token;
    }
    @Override
    public String sendCorpWxMorningMsg() {
        String corpToken = getCorpToken(configConstant.getWeatherSecret());
        Map<String, Object> map = new HashMap<>(5);
        map.put("touser", "@all");
        map.put("totag", "早上好！");
        map.put("msgtype", "news");
        map.put("agentid", configConstant.getWeatherId());
        StringBuilder content = new StringBuilder();
        //天气
        JSONObject weatherResult = tianQiService.getWeatherByCity();
        //时间
        String format = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm");
        content.append("\uD83D\uDDD3️").append("时间：").append(format).append(" ").append(DateUtil.dayOfWeekEnum(new Date()).toChinese()).append("\n");
        //城市
        content.append("\n");
        String city = weatherResult.getString("city");
        content.append("🏡").append("城市：").append(city).append("\n");
        //天气
        String wea = weatherResult.getString("wea");
        String weaLog = "\uD83C\uDF24️";
        if (wea.contains("晴")){
            weaLog="☀️";

        } else if (wea.contains("阴")) {
            weaLog="⛅️";
        }else if (wea.contains("云")){
            weaLog="☁️";
        }
        else if (wea.contains("雪")){
            weaLog="☃️";
        }
        else if (wea.contains("雨")){
            weaLog="\uD83C\uDF27️";
        }
        else if (wea.contains("雾")){
            weaLog="\uD83C\uDF2B️";
        }
        else if (wea.contains("沙")){
            weaLog="\uD83C\uDFDC️";
        }
        else if (wea.contains("尘")){
            weaLog="\uD83C\uDFDC️";
        }else if (wea.contains("雷")){
            weaLog="⛈️";
        }
        content.append("☁").append("天气：").append(weaLog).append(wea).append("\n");
        //最低温度
        String tem_night = weatherResult.getString("tem_night");
        //最高温度
        String tem_day = weatherResult.getString("tem_day");
        content.append("🌡").append("温度：").append(tem_night).append("~").append(tem_day).append("\n");
        //风
        String wind = weatherResult.getString("win") + "," + weatherResult.getString("win_speed");
        content.append("💨").append("风量：").append(wind).append("\n");
        //湿度
        String humidity = weatherResult.getString("humidity");
        content.append("💧").append("湿度：").append(humidity).append("\n");
        //未来三天天气
        Map<String, String> threeMap = tianQiService.getTheNextThreeDaysWeather();
        content.append("💫").append("未来三日：").append(threeMap.get("今")).append("|").append(threeMap.get("明")).append("|").append(threeMap.get("后")).append("\n");
        //生日和在一起天数
        content.append("\n");
        content.append("🎂").append("杨梓轩的生日：").append(getBirthday(configConstant.getBirthday1(), format)).append("\n");
        content.append("🎂").append("周艺的生日: ").append(getBirthday(configConstant.getBirthday2(), format)).append("\n");
        content.append("💕").append("在一起：").append(togetherDay(format)).append("\n");
        //每日一句,中文
        content.append("\n");
        String noteZh = proverbService.getOneProverbRandom();
        content.append(noteZh).append("\n");
        //每日一句，英文
        content.append("\n");
        String noteEn = proverbService.translateToEnglish(noteZh);
        content.append(noteEn).append("\n");
        Map<String, Object> contentMap = new HashMap<>();
        List<Object> articles = new ArrayList<>();
        Map<String, Object> articleMap = new HashMap<>();
        articleMap.put("title","\uD83C\uDF08早安，宝贝！");
        articleMap.put("description",content.toString());
        articleMap.put("url","URL");
        articleMap.put("picurl","https://pics5.baidu.com/feed/a71ea8d3fd1f4134017382a4fb7d1bccd0c85e09.jpeg?token=e3905aef3f52e03297adfff7f03d35ba");
        articles.add(articleMap);
        contentMap.put("articles",articles);
        map.put("news",contentMap);
        map.put("enable_id_trans",0);
        map.put("enable_duplicate_check",0);
        map.put("duplicate_check_interval",1800);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send" + "?access_token=" + corpToken+"&debug=1";
        String data = HttpUtil.sendPost(url,JSONObject.toJSONString(map));
        return data;
    }

    @Override
    public String sendCorpWxNoteMsg(String notes,Integer type) {
        String corpToken = getCorpToken(configConstant.getRemindSecret());
        Map<String, Object> map = new HashMap<>(5);
        switch (type){
            case 0:{
                map.put("touser", "@all");
                break;
            }
            case 1:{
                map.put("touser",configConstant.getMan());
                break;
            }
            case 2:{
                map.put("touser",configConstant.getWoman());
                break;
            }
            default:{
                map.put("touser", "@all");
                break;
            }
        }

        map.put("msgtype", "textcard");
        map.put("agentid", configConstant.getRemindId());
        Map<String,Object> textcardMap = new HashMap<>();
        textcardMap.put("title","提醒事项");
        textcardMap.put("description","<div class=\"gray\">"+DateUtil.format(new Date(),"yyyy年MM月dd日 HH时mm分 ")+ DateUtil.dayOfWeekEnum(new Date()).toChinese()+ "</div> <div class=\"highlight\">-----------------------------------------------</div>"+" <div class=\"highlight\">"+notes+"</div>");
        textcardMap.put("url","http://zxyang.cn");
        textcardMap.put("btntxt","没有更多");
        map.put("textcard",textcardMap);
        map.put("enable_id_trans",0);
        map.put("enable_duplicate_check",0);
        map.put("duplicate_check_interval",1800);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send" + "?access_token=" + corpToken+"&debug=1";
        String data = HttpUtil.sendPost(url,JSONObject.toJSONString(map));
        return data;

    }

    @Override
    public String sendCorpWxNightMsg() {
        String corpToken = getCorpToken(configConstant.getWeatherSecret());
        Map<String, Object> map = new HashMap<>(5);
        map.put("touser", "@all");
        map.put("totag", "早上好！");
        map.put("msgtype", "news");
        map.put("agentid", configConstant.getWeatherId());
        StringBuilder content = new StringBuilder();
        //天气
        JSONObject nextWeatherByCity = tianQiService.getNextWeatherByCity();
        JSONObject weatherResult = nextWeatherByCity.getJSONArray("data").getJSONObject(1);
        //时间
        String format = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm");
        content.append("\uD83D\uDDD3️").append("时间：").append(format).append(" ").append(DateUtil.dayOfWeekEnum(new Date()).toChinese()).append("\n");
        //城市
        content.append("\n");
        String city = nextWeatherByCity.getString("city");
        content.append("🏡").append("城市：").append(city).append("\n");
        //天气
        content.append("\n");
        content.append("\t\t✨✨✨天气预报✨✨✨").append("\n").append("\n");
        String wea = weatherResult.getString("wea");
        String weaLog = "\uD83C\uDF24️";
        if (wea.contains("晴")){
            weaLog="☀️";

        } else if (wea.contains("阴")) {
            weaLog="⛅️";
        }else if (wea.contains("云")){
            weaLog="☁️";
        }
        else if (wea.contains("雪")){
            weaLog="☃️";
        }
        else if (wea.contains("雨")){
            weaLog="\uD83C\uDF27️";
        }
        else if (wea.contains("雾")){
            weaLog="\uD83C\uDF2B️";
        }
        else if (wea.contains("沙")){
            weaLog="\uD83C\uDFDC️";
        }
        else if (wea.contains("尘")){
            weaLog="\uD83C\uDFDC️";
        }else if (wea.contains("雷")){
            weaLog="⛈️";
        }
        content.append("☁").append("天气：").append(weaLog).append(wea).append("\n");
        //最低温度
        String tem_night = weatherResult.getString("tem_night");
        //最高温度
        String tem_day = weatherResult.getString("tem_day");
        content.append("🌡").append("温度：").append(tem_night).append("~").append(tem_day).append("\n");
        //风
        String wind = weatherResult.getString("win") + "," + weatherResult.getString("win_speed");
        content.append("💨").append("风量：").append(wind).append("\n");
        content.append("✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨").append("\n").append("\n");
       //生日和在一起天数
        content.append("\n");
        content.append("🎂").append("杨梓轩的生日：").append(getBirthday(configConstant.getBirthday1(), format)).append("\n");
        content.append("🎂").append("周艺的生日: ").append(getBirthday(configConstant.getBirthday2(), format)).append("\n");
        content.append("💕").append("在一起：").append(togetherDay(format)).append("\n");
        //每日一句,中文
        content.append("\n");
        String noteZh = proverbService.getOneProverbRandom();
        content.append(noteZh).append("\n");
        //每日一句，英文
        content.append("\n");
        String noteEn = proverbService.translateToEnglish(noteZh);
        content.append(noteEn).append("\n");
        Map<String, Object> contentMap = new HashMap<>();
        List<Object> articles = new ArrayList<>();
        Map<String, Object> articleMap = new HashMap<>();
        articleMap.put("title","🌙晚安，宝贝！");
        articleMap.put("description",content.toString());
        articleMap.put("url","URL");
        articleMap.put("picurl","https://pics4.baidu.com/feed/32fa828ba61ea8d31b26b2f71ea06046241f5874.jpeg?token=11b59f12643f31e727746e6557335dca");
        articles.add(articleMap);
        contentMap.put("articles",articles);
        map.put("news",contentMap);
        map.put("enable_id_trans",0);
        map.put("enable_duplicate_check",0);
        map.put("duplicate_check_interval",1800);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send" + "?access_token=" + corpToken+"&debug=1";
        String data = HttpUtil.sendPost(url,JSONObject.toJSONString(map));
        return data;
    }

    @Override
    public String addRemind(RemindVo remindVo) {
        String id = IdUtil.fastSimpleUUID();
        String corn = DateToCron.DateToCron(remindVo.getTime());
        QuartzUtil.addJob(scheduler,id, RemindJob.class,remindVo,corn);
        return null;
    }

    @Override
    public List<RemindVo> getRemindList() {
        List<QuartzJobsVO> allJobs = QuartzUtil.getAllJobs(scheduler);
        List<RemindVo> remindVos = new ArrayList<>();
        allJobs.forEach(job->{
            RemindVo params = (RemindVo) job.getParams();
            params.setStatus(job.getStatus());
            remindVos.add(params);
        });
        return remindVos;
    }

    @Override
    public void deleteRemind(String id) {
        QuartzUtil.removeJob(scheduler,id);
    }

/*    @Override
    public String send() {

        // 定义一个任务
        CronUtil.schedule("0 30 7 * * ?", new Task() {
            @Override
            public void execute() {
                sendCorpWxMorningMsg();
            }
        });
        // 计时器

        // 开始执行任务 (延迟1000毫秒执行，每3000毫秒执行一次)
        CronUtil.setMatchSecond(true);
        Scheduler scheduler = CronUtil.getScheduler();
        boolean started = scheduler.isStarted();
        if (started){
        }else {
            System.err.println("启动");
            CronUtil.start();
        }
        return null;
    }*/

    private String getBirthday(String configConstant, String date) {
        String birthDay = "无法识别";
        try {
            Calendar calendar = Calendar.getInstance();
            String newD = calendar.get(Calendar.YEAR) + "-" + configConstant;
            birthDay = dateUtil.daysBetween(date, newD);
            if (Integer.parseInt(birthDay) < 0) {
                Integer newBirthDay = Integer.parseInt(birthDay) + 365;
                birthDay = newBirthDay + "天";
            } else {
                birthDay = birthDay + "天";
            }
        } catch (ParseException e) {
            log.error("togetherDate获取失败" + e.getMessage());
        }
        return birthDay;
    }
    public static String daysBetween(String startDate,String endDate) throws ParseException {
        long nd = 1000 * 24 * 60 * 60;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//设置时间格式
        Date newStartDate=sdf.parse(startDate);
        Date newEndDate=sdf.parse(endDate);
        long diff = (newEndDate.getTime()) - (newStartDate.getTime()); //计算出毫秒差
        // 计算差多少天
        String day = diff / nd +"";
        return day;
    }
    private String togetherDay(String date) {
        //在一起时间
        String togetherDay = "";
        try {
            togetherDay = "第" + dateUtil.daysBetween(configConstant.getTogetherDate(), date) + "天";
        } catch (ParseException e) {
            log.error("togetherDate获取失败" + e.getMessage());
        }
        return togetherDay;
    }

}
