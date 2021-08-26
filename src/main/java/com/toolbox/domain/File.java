
package com.toolbox.domain;

import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "note")
public class File {

	private Integer id;

	private String fileName;

	private String filePath;

}