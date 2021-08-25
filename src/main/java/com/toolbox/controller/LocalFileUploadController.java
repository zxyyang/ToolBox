package com.toolbox.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toolbox.service.FileStorageService;
import com.toolbox.valueobject.RequestBean;
import com.toolbox.valueobject.UploadFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

//在控制器中，使用@RestController组合注解替换了@Controller+@ResponseBody的注解方式，并采用@RequestMapping的快捷方式注解方法。
@RestController()
@Api(value = "/file", tags = { "上传文件接口" })
@RequestMapping("/localFile")
public class LocalFileUploadController {

	@Autowired
	FileStorageService fileStorageService;

	@ApiOperation(value = "/upload", notes = "上传文件", httpMethod = "POST")
	@PostMapping("/upload")
	public RequestBean<String> upload(@RequestParam("file") MultipartFile file) {
		try {
			fileStorageService.save(file);
			return RequestBean.Success();
		} catch (Exception e) {
			return RequestBean.Error();
		}

	}

	@ApiOperation(value = "/list", notes = "获取文件目录", httpMethod = "GET")
	@GetMapping("/files")
	public RequestBean<List<UploadFile>> files() throws JsonProcessingException {
		List<UploadFile> files = fileStorageService.load().map(path -> {
			String fileName = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(LocalFileUploadController.class, "getFile", URLEncoder.encode(URLEncoder.encode(path.getFileName().toString())))
					.build().toString();
			return new UploadFile(fileName, url);
		}).collect(Collectors.toList());
		return RequestBean.Success(files);
	}

	@ApiOperation(value = "/files1", notes = "获取文件", httpMethod = "GET")
	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable("filename") String filename) throws UnsupportedEncodingException {
		// 文件中文名解密
		String fileNameEncode = URLDecoder.decode(filename, "UTF-8");
		Resource file = fileStorageService.load(fileNameEncode);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + URLEncoder.encode(file.getFilename(), "UTF-8") + "\"").body(file);
	}

}
