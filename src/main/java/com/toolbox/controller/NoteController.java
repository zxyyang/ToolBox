package com.toolbox.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toolbox.domain.Note;
import com.toolbox.domain.User;
import com.toolbox.service.NoteService;
import com.toolbox.service.UserService;
import com.toolbox.vo.RequestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    NoteService noteService;
    @Autowired
    UserService userService;


    @RequestMapping("/list")
    public RequestBean<List<Note>> noteList() throws JsonProcessingException {

        return RequestBean.Success(noteService.queryAll());
    }


    @RequestMapping("/list1")
    public RequestBean<List<User>> noteList1() throws JsonProcessingException {

        return RequestBean.Success(userService.queryAll());
    }
}
