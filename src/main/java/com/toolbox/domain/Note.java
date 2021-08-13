
package com.toolbox.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import org.springframework.context.annotation.Primary;


@Data
@Table(name ="note")
public class Note {


	private Integer id;

	@Column(name = "note.note_name")
	private String noteName;

	@Column(name = "note_content")
	private String noteContent;

	@Column(name = "note_remark")
	private String noteRemark;

	@Column(name = "note_type")
	private String noteType;


}