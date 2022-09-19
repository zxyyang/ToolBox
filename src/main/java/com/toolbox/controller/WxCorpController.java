package com.toolbox.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.toolbox.service.WxSendService;
import com.toolbox.util.wechat.AesException;
import com.toolbox.util.wechat.WXBizJsonMsgCrypt;
import com.toolbox.valueobject.RequestBean;
import com.toolbox.vo.config.ConfigConstant;
import com.toolbox.vo.wx.RemindVo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

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

    @Autowired
    ConfigConstant configConstant;
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
                           @RequestParam(name = "echostr") final String echostr
                           ,HttpServletRequest request,final HttpServletResponse response) throws Exception {
        //TODO:修改
        String sToken = configConstant.getHdToken();
        String sCorpID = configConstant.getCorpId();
        String sEncodingAESKey = configConstant.getEncodingAESKey();

        WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt(sToken, sEncodingAESKey, sCorpID);
        String sEchoStr; //需要返回的明文
        try {
            sEchoStr = wxcpt.VerifyURL(msgSignature, timestamp, nonce, echostr);
            log.info("-----验证url的echostr: " + sEchoStr);
            // 验证URL成功，将sEchoStr返回
            PrintWriter writer = response.getWriter();
            writer.write(sEchoStr);
            writer.flush();
        } catch (Exception e) {
            log.error("-----验证url失败: ");
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
        String sToken = configConstant.getHdToken();
        String sCorpID = configConstant.getCorpId();
        String sEncodingAESKey = configConstant.getEncodingAESKey();

        WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt(sToken, sEncodingAESKey, sCorpID);
        //获取数据
        InputStream inputStream = request.getInputStream();
        String sPostData = IOUtils.toString(inputStream, "UTF-8");
        String sMsg = wxcpt.DecryptMsg(msgSignature, timestamp, nonce, sPostData);
        log.info("解析后的消息: " + sMsg);
        JSONObject json = JSONObject.parseObject(sMsg);
        String Content = json.getString("Content");
        //内容就行业务处理
        //TODO 业务处理
        log.error("Content：" + Content);

        return null;

    }

    @GetMapping("/listRemind")
    public RequestBean< PageInfo<RemindVo>> listRemind(@RequestParam(defaultValue = "1", value = "pageNumber") Integer pageNumber,
                                                  @RequestParam(value = "pageSize") Integer pageSize) {
        try {
            PageInfo<RemindVo> remindList = wxSendService.getRemindList(pageNumber, pageSize);
            return  RequestBean.Success(remindList);
        }
        catch (Exception e){
            return RequestBean.Error(e.getMessage());
        }
    }


    @PostMapping("/addRemind")
    public RequestBean<String> addRemind(@RequestBody RemindVo remindVo){
        try {
        String s = wxSendService.addRemind(remindVo);
            return  RequestBean.Success(s);
        }
        catch (Exception e){
            return RequestBean.Error(e.getMessage());
        }
    }

    @GetMapping("/deleteRemind")
    public  RequestBean<String> deleteRemind(@RequestParam List<String> ids){
        try {
            wxSendService.deleteRemind(ids);
            return  RequestBean.Success();
        }
        catch (Exception e){
            return RequestBean.Error(e.getMessage());
        }
    }

}
