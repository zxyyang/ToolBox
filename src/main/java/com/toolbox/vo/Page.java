/*******************************************************************************
 * Copyright (c) 2021, 2021 Hirain Technologies Corporation.
 ******************************************************************************/
package com.toolbox.vo;

import java.util.List;

import lombok.Data;

@Data
public class Page<T> {

	private Integer pageNumber; // 分页起始页

	private Integer pagesize; // 每页记录数

	private List<T> datas; // 返回的记录集合

	private Integer total; // 总页数

}