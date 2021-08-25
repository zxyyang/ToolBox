
package com.toolbox.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toolbox.util.QiNiuUtil;
import com.toolbox.valueobject.Files;
import com.toolbox.valueobject.RequestBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/cloudFile", tags = { "七牛文件存储" })
@RequestMapping("/cloudFile")
public class CloudFileUploadController {

	@ApiOperation(value = "/upload", notes = "上传文件", httpMethod = "POST")
	@PostMapping("/upload")
	public RequestBean<String> upload(@RequestParam("file") MultipartFile file) {
		try {
			QiNiuUtil.uploadMultipartFile(file, file.getOriginalFilename(), false);

		} catch (Exception e) {
			return RequestBean.Error();
		}
		return RequestBean.Success();
	}

	@ApiOperation(value = "/downLoad", notes = "下载文件", httpMethod = "GET")
	@GetMapping("/downLoad")
	public ResponseEntity<Resource> downLoad(String fileName) throws IOException {
		Resource file = QiNiuUtil.downloadByIE(fileName);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"")
				.body(file);
	}

	@ApiOperation(value = "/list", notes = "文件目录", httpMethod = "GET")
	@GetMapping("/list")
	public RequestBean<List<Files>> list() throws JsonProcessingException {
		List<Files> directory = QiNiuUtil.directory();
		return RequestBean.Success(directory);
	}

}