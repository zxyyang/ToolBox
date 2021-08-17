package com.toolbox.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import com.toolbox.domain.Note;
import com.toolbox.service.NoteService;
import com.toolbox.vo.RequestBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "/note", tags = {"笔记接口"}, description = "笔记总接口")
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;


    @ApiOperation(value = "/list", notes = "展示全部", httpMethod = "GET")
    @GetMapping("/list")
    public RequestBean<PageInfo<Note>> noteList(@RequestParam(defaultValue = "1") Integer pageNumber, Integer pageSize)
            throws JsonProcessingException {
        return RequestBean.Success(noteService.queryAll(pageNumber, pageSize)); // 查询的数据
    }
}


