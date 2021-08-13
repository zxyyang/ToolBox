/*******************************************************************************
 * Copyright (c) 2021, 2021 Hirain Technologies Corporation.
 ******************************************************************************/
package com.toolbox.domain;

import javax.persistence.Column;
import javax.persistence.Table;

import lombok.Data;

/**
 * @Version 1.0
 * @Author zixuan.yang
 * @Created 2021/8/13 17:35
 * @Description
 *              <p>
 * @Modification
 *               <p>
 *               Date Author Version Description
 *               <p>
 *               2021/8/13 zixuan.yang@hirain.com 1.0 create file
 */
@Data
@Table(name = "note")
public class Note {

	private Integer id;

	@Column(name = "`note.note_name`")
	private String noteName;

	@Column(name = "`note.note_content`")
	private String noteContent;

	@Column(name = "`note.note_remark`")
	private String noteRemark;

	@Column(name = "`note.note_type`")
	private String noteType;
}