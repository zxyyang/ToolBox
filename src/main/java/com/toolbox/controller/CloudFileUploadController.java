package com.toolbox.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.toolbox.util.QiNiuUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toolbox.domain.File;
import com.toolbox.service.FileService;
import com.toolbox.valueobject.Files;
import com.toolbox.valueobject.RequestBean;
import com.toolbox.vo.DeleteResult;
import com.toolbox.vo.down_ret;

import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/cloudFile", tags = { "七牛文件存储" })
@RequestMapping("/cloudFile")
public class CloudFileUploadController {

	@Autowired
	private FileService fileService;
	
	@Autowired
	private QiNiuUtil qiNiuUtil;

	 @RequiresPermissions(value = { "add" })
	@ApiOperation(value = "/upload", notes = "上传文件", httpMethod = "POST")
	@PostMapping("/upload")
	public RequestBean<String> upload(@RequestParam("file") MultipartFile file, @RequestParam(defaultValue = "/") String path) {
		System.out.println("上传开始：");
		try {
			if (qiNiuUtil.uploadMultipartFile(file, file.getOriginalFilename(), false)) {
				fileService.add(file.getOriginalFilename(), path);
			} else {
				return RequestBean.Error();
			}
		} catch (Exception e) {
			RequestBean.Error(e.toString());
		}
		return RequestBean.Success();
	}

	@RequiresPermissions(value = { "down" })
	@ApiOperation(value = "/downLoadByIe", notes = "调用浏览器下载文件", httpMethod = "GET")
	@GetMapping("/downLoadByIe")
	public ResponseEntity<Resource> downLoadByIe(String fileName) throws IOException {
		Resource file = qiNiuUtil.downloadByIE(fileName);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"")
				.body(file);
	}

	@RequiresPermissions(value = { "down" })
	@ApiOperation(value = "/downLoad", notes = "url下载", httpMethod = "GET")
	@GetMapping("/downLoad")
	public RequestBean<down_ret> downLoad(String fileName) throws IOException {

		return RequestBean.Success(qiNiuUtil.downloadByUrl(fileName));
	}

	@RequiresPermissions(value = { "list" })
	@ApiOperation(value = "/list", notes = "文件目录", httpMethod = "GET")
	@GetMapping("/list")
	public RequestBean<List<Files>> list() throws Exception {
		List<Files> directory = qiNiuUtil.directory();
		return RequestBean.Success(directory);
	}

	@RequiresPermissions(value = { "list" })
	@ApiOperation(value = "/listByPath", notes = "文件目录", httpMethod = "GET")
	@GetMapping("/listByPath")
	public RequestBean<List<Files>> listByPath(String path) throws Exception {

		return RequestBean.Success(qiNiuUtil.listByPath(fileService.selectName(path)));
	}

	@RequiresPermissions(value = { "update" })
	@ApiOperation(value = "/reName", notes = "修改名字", httpMethod = "POST")
	@PostMapping("/reName")
	public RequestBean<Boolean> reName(String originName, String objectName) throws Exception {
		Boolean reNameIs = qiNiuUtil.reNameOrMove(originName, objectName);
		if (reNameIs) {
			fileService.updateName(originName, objectName);
			return RequestBean.Success();
		} else {
			return RequestBean.Error();
		}

	}

	@RequiresPermissions(value = { "delete" })
	@ApiOperation(value = "/batchDelete", notes = "批量删除", httpMethod = "POST")
	@PostMapping("/batchDelete")
	public RequestBean<List<DeleteResult>> batchDelete(String[] name) throws Exception {
		 List<String> dir = new ArrayList<>();
		 List<String> file = new ArrayList<>();
		List<DeleteResult> delDirs = new ArrayList<>();
		for (String s : name) {
			DeleteResult dirDeLs =new DeleteResult();
			dirDeLs.setResult("succeed");
			if(s.charAt(s.length()-1) == '/') {
				dir.add(s);
				dirDeLs.setName(s);
				delDirs.add(dirDeLs);
			}
			else if (s.contains("(文件云端获取失败)")){
				dir.add(s.replace("(文件云端获取失败)",""));
				dirDeLs.setName(s);
				delDirs.add(dirDeLs);
			}
			else {
				file.add(s);
			}
		}
		if (!CollectionUtils.isEmpty(dir)){
			fileService.batchDelete(dir.toArray(new String[0]));
		}
		List<DeleteResult> deleteResultList = qiNiuUtil.batchDelete(file.toArray(new String[0]));

		deleteResultList.addAll(delDirs);
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

	@RequiresPermissions(value = { "add" })
	@ApiOperation(value = "/networkResources", notes = "网络地址资源存储在云端", httpMethod = "POST")
	@PostMapping("/networkResources")
	public RequestBean<Boolean> networkResources(String fileName, String SrcURL, @RequestParam(defaultValue = "/") String path) throws Exception {
		String name = qiNiuUtil.networkResources(fileName, SrcURL);
		if (name != null) {
			fileService.add(name, path);
			return RequestBean.Success();
		} else {
			return RequestBean.Error();
		}

	}

	@RequiresPermissions(value = { "add" })
	@ApiOperation(value = "/makeDir", notes = "创建文件夹", httpMethod = "POST")
	@PostMapping("/makeDir")
	public RequestBean<Boolean> makeDir(String name, @RequestParam(defaultValue = "/") String path) {
		fileService.add(name, path);
		return RequestBean.Success();
	}

	@RequiresPermissions(value = { "select" })
	@ApiOperation(value = "/selectByName", notes = "查询文件", httpMethod = "GET")
	@GetMapping("/selectByName")
	public RequestBean<List<Files>> selectByName(String name) throws Exception {
		List<File> files = fileService.selectByName(name);
		List<String> list = new ArrayList<>();
		for (File file : files) {
			list.add(file.getFileName());
		}
		String[] names = list.toArray(new String[list.size()]);

		return RequestBean.Success(qiNiuUtil.listByPath(names));
	}

	@RequiresPermissions(value = { "add" })
	@ApiOperation(value = "/noteAddImage", notes = "笔记图片", httpMethod = "POST")
	@PostMapping("/noteAddImage")
	public RequestBean<String> noteAddImage(@RequestParam("file") MultipartFile file) {
		try {

			String url = qiNiuUtil.uploadNoteImage(file, RandomUtil.randomString(8) + (file.getOriginalFilename()), false);
			if (url != null) {
				return RequestBean.Success(url);
			} else {
				return RequestBean.Error();
			}
		} catch (Exception e) {
			return RequestBean.Error(e.toString());
		}

	}

	@RequiresPermissions(value = { "delete" })
	@ApiOperation(value = "/noteDeleteImage", notes = "日记图片删除", httpMethod = "POST")
	@PostMapping("/noteDeleteImage")
	public RequestBean<String> noteDeleteImage(String url) throws JsonProcessingException {
		try {
			String[] split = url.split("/");
			String fileNanme = split[split.length - 1];
			qiNiuUtil.deleteNoteImage(fileNanme);

		} catch (Exception e) {
			RequestBean.Error(e.toString());
		}
		return RequestBean.Success();
	}
}