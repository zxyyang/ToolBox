package com.toolbox.vo.wx;

import lombok.Data;

import java.io.Serializable;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/16 15:36
 */
@Data
public class RemindVo implements Serializable {
    private String id;
    private String content;
    private String time;
    private Integer type;
    private String status;
}
