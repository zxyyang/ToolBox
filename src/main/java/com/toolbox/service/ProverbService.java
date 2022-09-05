package com.toolbox.service;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/5 14:57
 */
public interface ProverbService {
    /**
     * 这个接口很奇怪，可能会返回奇奇怪怪的句子
     * @return
     */
    String getOneProverbRandom();

    String translateToEnglish(String sentence);

    /**
     * 得到正常的句子
     * @return
     */
    String[] getOneNormalProverb();
}
