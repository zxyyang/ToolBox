package com.toolbox.controller;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

	@RequiresPermissions(value = { "list", "query" }, logical = Logical.OR) // list或者qurry权限
	@ApiOperation(value = "/list", notes = "展示全部", httpMethod = "GET")
	@GetMapping("/list")
	public RequestBean<PageInfo<Note>> noteList(@RequestParam(defaultValue = "1", value = "pageNumber") Integer pageNumber,
			@RequestParam(value = "pageSize") Integer pageSize) throws JsonProcessingException {
		return RequestBean.Success(noteService.queryAll(pageNumber, pageSize)); // 查询的数据
	}

	@RequiresPermissions(value = { "select" }) // list或者qurry权限
	@ApiOperation(value = "/ByID", notes = "通过ID查询", httpMethod = "GET")
	@GetMapping("/selectById")
	public RequestBean<Note> selectById(Integer id) throws JsonProcessingException {
		return RequestBean.Success(noteService.selectById(id));
	}

	@RequiresPermissions(value = { "add" }) // list或者qurry权限
	@ApiOperation(value = "/add", notes = "新增", httpMethod = "POST")
	@PostMapping("/add")
	public RequestBean<Integer> add(Note note) throws JsonProcessingException {
		return RequestBean.Success(noteService.add(note));
	}

	@RequiresPermissions(value = { "update" })
	@ApiOperation(value = "/update", notes = "更新", httpMethod = "POST")
	@PostMapping("/update")
	public RequestBean<Integer> update(Note note) throws JsonProcessingException {
		return RequestBean.Success(noteService.update(note));
	}

	@RequiresPermissions(value = { "select" })
	@ApiOperation(value = "/selectByName", notes = "通过名字查询", httpMethod = "GET")
	@GetMapping("/selectByName")
	public RequestBean<PageInfo<Note>> selectByName(@RequestParam(defaultValue = "1", value = "pageNumber") Integer pageNumber,
			@RequestParam(value = "pageSize") Integer pageSize, @RequestParam(value = "noteName") String noteName) throws JsonProcessingException {
		return RequestBean.Success(noteService.queryByName(pageNumber, pageSize, noteName)); // 查询的数据
	}

	@RequiresPermissions(value = { "delete" })
	@ApiOperation(value = "/delete", notes = "删除", httpMethod = "POST")
	@PostMapping("/delete")
	public RequestBean<Integer> delete(Integer[] ids) throws JsonProcessingException {
		return RequestBean.Success(noteService.deleteNote(ids));
	}

}
