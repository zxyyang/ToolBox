package com.toolbox.service;

import com.github.pagehelper.PageInfo;
import com.toolbox.vo.wx.RemindVo;

import java.util.List;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/5 14:59
 */
public interface WxSendService {
    String sendCorpWxMorningMsg();

    String sendCorpWxNoteMsg(String content,Integer type );

    String sendCorpWxNightMsg();

    String addRemind(RemindVo remindVo);

    PageInfo<RemindVo> getRemindList(Integer pageNumber, Integer pageSize);

    void deleteRemind(List<String> ids);
}
