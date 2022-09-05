package com.toolbox.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/5 14:40
 */
public interface TianQiService {
    JSONObject getWeatherByCity();

    JSONObject getNextWeatherByCity();

    JSONObject getWeatherByIP();
    Map<String, String> getTheNextThreeDaysWeather();

}

