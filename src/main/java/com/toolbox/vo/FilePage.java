
package com.toolbox.vo;

import java.util.List;

import com.toolbox.valueobject.Files;

import lombok.Data;

@Data
public class FilePage {

	private Integer pageSize;

	private Integer pageNumber;

	private Integer total;

	private List<Files> filesList;

}