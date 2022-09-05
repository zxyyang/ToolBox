package com.toolbox.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.service.TianQiService;
import com.toolbox.service.WxSendService;
import com.toolbox.util.HttpUtil;
import com.toolbox.vo.config.ConfigConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/5 15:01
 */
@Service
public class WxSendServiceImpl implements WxSendService {
    @Autowired
    private ConfigConstant configConstant;

    @Autowired
    private TianQiService tianQiService;

    private String getCorpToken(){
        String param  = "corpid="+configConstant.getCorpId()+"&corpsecret="+configConstant.getSecret();
        String token = HttpUtil.sendGet(" https://qyapi.weixin.qq.com/cgi-bin/gettoken", param);
        JSONObject object = JSONObject.parseObject(token);
        String access_token = object.getString("access_token");
        System.err.println(access_token);
        return access_token;
    }
    @Override
    public String sendCorpWxMsg() {
        String corpToken = getCorpToken();
        Map<String, Object> map = new HashMap<>(5);
        map.put("touser", "@all");
        map.put("totag", "早上好！");
        map.put("msgtype", "news");
        map.put("agentid", configConstant.getAgentId());
        StringBuilder content = new StringBuilder();
        //天气
        JSONObject weatherResult = tianQiService.getWeatherByCity();
        //城市
        String city = weatherResult.getString("city");
        content.append("城市：").append(city).append("\n");
        //天气
        String wea = weatherResult.getString("wea");
        content.append("天气：").append(wea).append("\n");
        //最低温度
        String tem_night = weatherResult.getString("tem_night");
        //最高温度
        String tem_day = weatherResult.getString("tem_day");
        content.append("今日温度：").append(tem_night).append("~").append(tem_day).append("\n");
        //风
        String wind = weatherResult.getString("win") + "," + weatherResult.getString("win_speed");
        content.append("风量：").append(wind).append("\n");
        //湿度
        String humidity = weatherResult.getString("humidity");
        content.append("湿度：").append(humidity).append("\n");
        //未来三天天气
        Map<String, String> threeMap = tianQiService.getTheNextThreeDaysWeather();
        content.append("未来三天天气：").append(threeMap.get("今")).append("|").append(threeMap.get("明")).append("|").append(threeMap.get("后")).append("\n");
        //TODO 生日-在一起时间，每日一句

//        Map<String, Object> contentMap = new HashMap<>(1);
//        contentMap.put("articles", content.toString());
//        map.put("news", contentMap);
        //
        Map<String, Object> contentMap = new HashMap<>();
        List<Object> articles = new ArrayList<>();
        Map<String, Object> articleMap = new HashMap<>();
        articleMap.put("title","早安，宝贝！");
        articleMap.put("description",content.toString());
        articleMap.put("url","URL");
        articleMap.put("picurl","https://pics1.baidu.com/feed/b2de9c82d158ccbf518f7f42c6ba3238b1354104.jpeg?token=9996f54fbb785b946a594cc1a1365c57");
        articles.add(articleMap);
        contentMap.put("articles",articles);
        map.put("news",contentMap);
        map.put("enable_id_trans",0);
        map.put("enable_duplicate_check",0);
        map.put("duplicate_check_interval",1800);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send" + "?access_token=" + corpToken+"&debug=1";
        String data = HttpUtil.sendPost(url,JSONObject.toJSONString(map));
        System.err.println(JSON.toJSONString(data));
        return data;
    }

    @Override
    public String sendCorpWxNoteMsg(String content, String time) {
        return null;
    }

    @Override
    public String sendCorpWxNightMsg() {
        return null;
    }

}
