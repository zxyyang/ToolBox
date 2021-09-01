package com.toolbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import com.toolbox.domain.Note;
import com.toolbox.service.NoteService;
import com.toolbox.valueobject.RequestBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/note", tags = { "笔记接口" }, description = "笔记总接口")
@RequestMapping("/note")
public class NoteController {

	@Autowired
	private NoteService noteService;

	@ApiOperation(value = "/list", notes = "展示全部", httpMethod = "GET")
	@GetMapping("/list")
	public RequestBean<PageInfo<Note>> noteList(@RequestParam(defaultValue = "1", value = "pageNumber") Integer pageNumber,
			@RequestParam(value = "pageSize") Integer pageSize) throws JsonProcessingException {
		return RequestBean.Success(noteService.queryAll(pageNumber, pageSize)); // 查询的数据
	}

	@ApiOperation(value = "/ByID", notes = "通过ID查询", httpMethod = "GET")
	@GetMapping("/selectById")
	public RequestBean<Note> selectById(Integer id) throws JsonProcessingException {
		return RequestBean.Success(noteService.selectById(id));
	}

	@ApiOperation(value = "/add", notes = "新增", httpMethod = "POST")
	@PostMapping("/add")
	public RequestBean<Integer> add(Note note) throws JsonProcessingException {
		return RequestBean.Success(noteService.add(note));
	}

	@ApiOperation(value = "/update", notes = "更新", httpMethod = "POST")
	@PostMapping("/update")
	public RequestBean<Integer> update(Note note) throws JsonProcessingException {
		return RequestBean.Success(noteService.update(note));
	}

	@ApiOperation(value = "/selectByName", notes = "通过名字查询", httpMethod = "GET")
	@GetMapping("/selectByName")
	public RequestBean<PageInfo<Note>> selectByName(@RequestParam(defaultValue = "1", value = "pageNumber") Integer pageNumber,
			@RequestParam(value = "pageSize") Integer pageSize, @RequestParam(value = "noteName") String noteName) throws JsonProcessingException {
		return RequestBean.Success(noteService.queryByName(pageNumber, pageSize, noteName)); // 查询的数据
	}

}
