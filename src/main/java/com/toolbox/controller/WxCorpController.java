package com.toolbox.controller;

import com.alibaba.fastjson.JSONObject;
import com.sun.deploy.net.HttpUtils;
import com.toolbox.service.WxSendService;
import com.toolbox.util.HttpUtil;
import com.toolbox.util.wechat.AesException;
import com.toolbox.util.wechat.WXBizJsonMsgCrypt;
import com.toolbox.valueobject.RequestBean;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/5 17:45
 */
@Slf4j
@RestController
@Api(value = "/wx", tags = { "企业微信推送" }, description = "企业微信推送")
@RequestMapping("/wx")
public class WxCorpController {

    @Autowired
    private WxSendService wxSendService;

    /**
     * 获取Token
     * 每天早上7：30执行推送
     * @return
     */
    @Scheduled(cron = "0 30 7 * * ?")
    @GetMapping("/sendMorning")
    public RequestBean<String> sendMorning() {
        log.info("早晨推送启动！");
        try {
            String s = wxSendService.sendCorpWxMorningMsg();
            return  RequestBean.Success(s);
        }
        catch (Exception e){
            return RequestBean.Error(e.getMessage());
         }
    }

    /**
     * 获取Token
     * 每天早上7：30执行推送
     * @return
     */
    @Scheduled(cron = "0 0 21 * * ?")
    @GetMapping("/sendNight")
    public RequestBean<String> sendNight() {
        log.info("晚上推送启动！");
        try {
            String s = wxSendService.sendCorpWxNightMsg();
            return  RequestBean.Success(s);
        }
        catch (Exception e){
            return RequestBean.Error(e.getMessage());
        }
    }

    /**
     * get 请求  验签.
     * @param msgSignature 加密
     * @param timestamp    时间戳
     * @param nonce        随机
     * @param echostr      .
     * @param response     .
     * @throws Exception .
     */
    @GetMapping(value = "/callback/interAspect")
    public void reveiceMsg(@RequestParam(name = "msg_signature") final String msgSignature,
                           @RequestParam(name = "timestamp") final String timestamp,
                           @RequestParam(name = "nonce") final String nonce,
                           @RequestParam(name = "echostr") final String echostr,
                           final HttpServletResponse response) throws Exception {
        //TODO:修改
        String sToken = "QDG6eK";
        String sCorpID = "wx5823bf96d3bd56c7";
        String sEncodingAESKey = "jWmYm7qr5nMoAUwZRjGtBxmz3KA1tkAj3ykkR6q2B2C";

        WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt(sToken, sEncodingAESKey, sCorpID);
        String sEchoStr; //需要返回的明文
        try {
            sEchoStr = wxcpt.VerifyURL(msgSignature, timestamp, nonce, echostr);
            System.out.println("-----验证url的echostr: " + sEchoStr);
            // 验证URL成功，将sEchoStr返回
            PrintWriter writer = response.getWriter();
            writer.write(sEchoStr);
            writer.flush();
        } catch (Exception e) {
            System.out.println("-----验证url失败: ");
            //验证URL失败，错误原因请查看异常
            e.printStackTrace();
        }
    }
    /**
     * 企业微信客户联系回调.
     * @param request       request
     * @param msgSignature 签名
     * @param timestamp    时间戳
     * @param nonce        随机值
     * @return success
     */
    @ResponseBody
    @PostMapping(value = "/callback/interAspect")
    public String acceptMessage(final HttpServletRequest request,
                                @RequestParam(name = "msg_signature") final String msgSignature,
                                @RequestParam(name = "timestamp") final String timestamp,
                                @RequestParam(name = "nonce") final String nonce) throws IOException, AesException {
        String sToken = "QDG6eK";
        String sCorpID = "wx5823bf96d3bd56c7";
        String sEncodingAESKey = "jWmYm7qr5nMoAUwZRjGtBxmz3KA1tkAj3ykkR6q2B2C";

        WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt(sToken, sEncodingAESKey, sCorpID);
        //获取数据
        InputStream inputStream = request.getInputStream();
        String sPostData = IOUtils.toString(inputStream, "UTF-8");
        String sMsg = wxcpt.DecryptMsg(msgSignature, timestamp, nonce, sPostData);
        System.out.println("解析后的消息: " + sMsg);
        JSONObject json = JSONObject.parseObject(sMsg);
        String Content = json.getString("Content");
        //内容就行业务处理
        //TODO 业务处理
        System.out.println("Content：" + Content);

        return null;

    }

}