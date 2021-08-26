
package com.toolbox.domain;

import javax.persistence.Column;
import javax.persistence.Table;

import lombok.Data;

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

	@Column(name = "`note.note_time`")
	private String noteTime;
}