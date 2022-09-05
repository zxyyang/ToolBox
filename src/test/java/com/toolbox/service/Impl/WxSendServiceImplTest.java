package com.toolbox.service.Impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/5 15:37
 */
@SpringBootTest
class WxSendServiceImplTest {

    @Autowired
    WxSendServiceImpl wxSendService;

    @Test
    void testSend(){
        String s = wxSendService.sendCorpWxMsg();
    }
}