package com.toolbox.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toolbox.service.FileService;
import com.toolbox.util.QiNiuUtil;
import com.toolbox.valueobject.Files;
import com.toolbox.valueobject.RequestBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/cloudFile", tags = { "七牛文件存储" })
@RequestMapping("/cloudFile")
public class CloudFileUploadController {

	@Autowired
	FileService fileService;

	@ApiOperation(value = "/upload", notes = "上传文件", httpMethod = "POST")
	@PostMapping("/upload")
	public RequestBean<String> upload(@RequestParam("file") MultipartFile file, @RequestParam(defaultValue = "/") String filePath) {
		try {
			QiNiuUtil.uploadMultipartFile(file, file.getOriginalFilename(), false);
			fileService.add(file.getOriginalFilename(), filePath);
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

	@ApiOperation(value = "/listByPath", notes = "文件目录", httpMethod = "GET")
	@GetMapping("/listByPath")
	public RequestBean<List<Files>> listByPath(String path) throws JsonProcessingException {

		return RequestBean.Success(QiNiuUtil.listByPath(fileService.selectName(path)));
	}

	@ApiOperation(value = "/reName", notes = "修改名字", httpMethod = "POST")
	@PostMapping("/reName")
	public RequestBean<Boolean> reName(String originName, String objectName) {
		Boolean reNameIs = QiNiuUtil.reNameOrMove(originName, objectName);
		if (reNameIs) {
			fileService.updateName(originName, objectName);
			return RequestBean.Success();
		} else {
			return RequestBean.Error();
		}

	}

	@ApiOperation(value = "/batchDelete", notes = "批量删除", httpMethod = "POST")
	@PostMapping("/batchDelete")
	public RequestBean<Map<String, String>> batchDelete(String[] fileNameList) throws JsonProcessingException {
		Map<String, String> stringStringMap = QiNiuUtil.batchDelete(fileNameList);
		List<String> list = new ArrayList<>();

		// 取出只有成功的操作key
		for (Map.Entry<String, String> map : stringStringMap.entrySet()) {
			if (map.getValue().equals("succeed")) {
				list.add(map.getKey());
			}
		}
		if (list.size() != 0) {
			String[] deleteVar = list.toArray(new String[list.size()]);
			System.out.println(deleteVar);
			fileService.batchDelete(deleteVar);
			return RequestBean.Success(stringStringMap);
		} else {
			return RequestBean.Error();
		}
	}

	@ApiOperation(value = "/networkResources", notes = "网络地址资源存储在云端", httpMethod = "POST")
	@PostMapping("/networkResources")
	public RequestBean<Boolean> networkResources(String fileName, String SrcURL, @RequestParam(defaultValue = "/") String path) {
		String name = QiNiuUtil.networkResources(fileName, SrcURL);
		if (name != null) {
			fileService.add(name, path);
			return RequestBean.Success();
		} else {
			return RequestBean.Error();
		}

	}
}