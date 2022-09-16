package com.toolbox.enums;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/8/31 20:33
 */
public class CronFormate {

    /**
     * 每年时间format格式
     */
    public static final String DATEFORMAT_YEAR = "ss mm HH dd MM ? *";

    /**
     * 每天时间format格式
     */
    public static final String DATEFORMAT_EVERYDAY = "ss mm HH * * ?";

    /**
     * 每周时间format格式
     */
    public static final String DATEFORMAT_SUN = "ss mm HH ? * 1";

    public static final String DATEFORMAT_MON = "ss mm HH ? * 2";

    public static final String DATEFORMAT_TUE = "ss mm HH ? * 3";

    public static final String DATEFORMAT_WED = "ss mm HH ? * 4";

    public static final String DATEFORMAT_THU = "ss mm HH ? * 5";

    public static final String DATEFORMAT_FRI = "ss mm HH ? * 6";

    public static final String DATEFORMAT_SAT = "ss mm HH ? * 7";

}

