package com.toolbox.service.Impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.service.ProverbService;
import com.toolbox.vo.config.ConfigConstant;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/5 14:57
 */
@Service
public class ProverbServiceImpl  implements ProverbService {
    @Autowired
    private ConfigConstant configConstant;
    @Override
    public String getOneProverbRandom() {
        String proverb;
        do {
            proverb = null;
            try {
                OkHttpClient client = new OkHttpClient().newBuilder().build();
                Request request = new Request.Builder()
                        .url("https://api.xygeng.cn/one")
                        .get()
                        .addHeader("Content-Type","")
                        .build();
                Response response = client.newCall(request).execute();
                //解析
                JSONObject jsonObject = JSONObject.parseObject(response.body().string());
                JSONObject content = jsonObject.getJSONObject("data");
                proverb = content.getString("content");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } while (proverb.length()>25);
        return proverb;
    }

    @Override
    public String translateToEnglish(String sentence) {
        String result = null;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder()
                    .url("https://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i="+sentence)
                    .get()
                    .addHeader("Content-Type","")
                    .build();
            Response response = client.newCall(request).execute();
            result = response.body().string();
            //解析
            JSONObject jsonObject = JSONObject.parseObject(result);
            result = jsonObject.getJSONArray("translateResult").getJSONArray(0).getJSONObject(0).getString("tgt");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String[] getOneNormalProverb() {
        String[] proverb = new String[2];
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "titleID="+ RandomUtil.randomInt(81,89));
            Request request = new Request.Builder()
                    .url("https://eolink.o.apispace.com/yymy/common/aphorism/getEnglishAphorismList")
                    .method("POST",body)
                    .addHeader("X-APISpace-Token",configConstant.getToken())
                    .addHeader("Authorization-Type","apikey")
                    .addHeader("Content-Type","")
                    .build();

            Response response = client.newCall(request).execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body().string());
            //随机取出一条句子
            JSONArray objects = JSONObject.parseArray((String) jsonObject.getJSONArray("result").getJSONObject(0).get("words"));
            JSONObject jsonObject1 = objects.getJSONObject(RandomUtil.randomInt(0, objects.size()-1));
            Object ch = jsonObject1.get("ch");
            Object en = jsonObject1.get("en");

            proverb[0] = en.toString().replaceAll("^([^s]*)、", "");
            proverb[1] = ch.toString();
            //去除无关元素
            //   proverb = s.replaceAll("^.*、", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return proverb;
    }

}
