
package com.toolbox.domain;

import javax.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "note")
public class File {

	private Integer id;

	private String fileName;

	private String filePath;

}