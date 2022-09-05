package com.toolbox.service;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/5 14:59
 */
public interface WxSendService {
    String sendCorpWxMorningMsg();

    String sendCorpWxNoteMsg(String content , String time);

    String sendCorpWxNightMsg();
}
