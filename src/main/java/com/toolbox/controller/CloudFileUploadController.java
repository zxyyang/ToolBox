package com.toolbox.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toolbox.domain.File;
import com.toolbox.service.FileService;
import com.toolbox.util.QiNiuUtil;
import com.toolbox.valueobject.Files;
import com.toolbox.valueobject.RequestBean;
import com.toolbox.vo.DeleteResult;
import com.toolbox.vo.down_ret;

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
	public RequestBean<String> upload(@RequestParam("file") MultipartFile file, @RequestParam(defaultValue = "/") String path) {
		try {
			if (QiNiuUtil.uploadMultipartFile(file, file.getOriginalFilename(), false)) {
				fileService.add(file.getOriginalFilename(), path);
			} else {
				return RequestBean.Error();
			}
		} catch (Exception e) {
			return RequestBean.Error();
		}
		return RequestBean.Success();
	}

	@ApiOperation(value = "/downLoadByIe", notes = "调用浏览器下载文件", httpMethod = "GET")
	@GetMapping("/downLoadByIe")
	public ResponseEntity<Resource> downLoadByIe(String fileName) throws IOException {
		Resource file = QiNiuUtil.downloadByIE(fileName);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"")
				.body(file);
	}

	@ApiOperation(value = "/downLoad", notes = "url下载", httpMethod = "GET")
	@GetMapping("/downLoad")
	public RequestBean<down_ret> downLoad(String fileName) throws IOException {

		return RequestBean.Success(QiNiuUtil.downloadByUrl(fileName));
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
	public RequestBean<List<DeleteResult>> batchDelete(String[] name) throws JsonProcessingException {
		List<DeleteResult> deleteResultList = QiNiuUtil.batchDelete(name);
		List<String> list = new ArrayList<>();

		// 取出只有成功的操作key
		for (DeleteResult deleteResult : deleteResultList) {
			if (deleteResult.getResult().equals("succeed")) {
				list.add(deleteResult.getName());
			}
		}
		if (list.size() != 0) {
			String[] deleteVar = list.toArray(new String[list.size()]);
			fileService.batchDelete(deleteVar);
			return RequestBean.Success(deleteResultList);
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

	@ApiOperation(value = "/makeDir", notes = "创建文件夹", httpMethod = "POST")
	@PostMapping("/makeDir")
	public RequestBean<Boolean> makeDir(String name, @RequestParam(defaultValue = "/") String path) {
		fileService.add(name, path);
		return RequestBean.Success();
	}

	@ApiOperation(value = "/selectByName", notes = "查询文件", httpMethod = "GET")
	@GetMapping("/selectByName")
	public RequestBean<List<Files>> selectByName(String name) throws JsonProcessingException {
		List<File> files = fileService.selectByName(name);
		List<String> list = new ArrayList<>();
		for (File file : files) {
			list.add(file.getFileName());
		}
		String[] names = list.toArray(new String[list.size()]);

		return RequestBean.Success(QiNiuUtil.listByPath(names));
	}
}