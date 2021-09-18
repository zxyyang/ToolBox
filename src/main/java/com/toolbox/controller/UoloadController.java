package com.toolbox.controller;

import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toolbox.service.Impl.UploadService;
import com.toolbox.util.UploadUtils;
import com.toolbox.util.Uploader;

@CrossOrigin
@Controller
@RequestMapping("/upload")
public class UoloadController {

	@Resource
	private UploadService uploadService;

	@ResponseBody
	@RequestMapping(path = "/upload2", method = RequestMethod.POST)
	public void upload(HttpServletRequest request, HttpServletResponse response) {
		new UploadUtils().upload(request);
		this.success(response, "上传成功!");
	}

	public void success(HttpServletResponse response, Object obj) {
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(obj.toString());
		} catch (Exception e) {
		} finally {
			if (writer != null) {
				// writer.close();
			}
		}
	}

	/**
	 * 分片上传
	 *
	 * @param request
	 *            前台发送过来的文件内容
	 * @param response
	 *            响应给前台文件路径
	 * @throws Exception
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void upload2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String[] filepath = { "" };
		final String[] or_filename = { "" };
		try {
			uploadService.post(request, new Uploader.UploadListener() {

				@Override
				public void callback(String status, String filename, String original_filename, String identifier, String fileType) {
					if (status != null) {
						System.out.println(filename);
					}
					filepath[0] = filename;// 文件上传的路径带走
					or_filename[0] = original_filename;// 源文件名称带走
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 这句话的意思，是让浏览器用utf8来解析返回的数据
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		// 这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
		response.setCharacterEncoding("UTF-8");
		response.getWriter().append(filepath[0] + "---" + or_filename[0]);
	}

	// @RequestMapping("/saveFileData")
	// public String saveFileData(@RequestBody Recording recording){
	// try {
	// recordingService.saveFileData(recording);
	// return "ok";
	// } catch (Exception e) {
	// return "no";
	// }
	// }

}