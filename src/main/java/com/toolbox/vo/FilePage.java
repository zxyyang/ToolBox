
package com.toolbox.vo;

import java.util.List;

import org.springframework.stereotype.Component;

import com.toolbox.valueobject.Files;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FilePage {

	private Integer pageSize;

	private Integer pageNumber;

	private Integer total;

	private List<Files> filesList;

}