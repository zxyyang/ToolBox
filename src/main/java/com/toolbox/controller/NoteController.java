package com.toolbox.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toolbox.domain.Note;
import com.toolbox.domain.User;
import com.toolbox.service.NoteService;
import com.toolbox.service.UserService;
import com.toolbox.vo.RequestBean;

@RestController
@RequestMapping("/note")
public class NoteController {

	@Autowired
	NoteService noteService;

	@Autowired
	UserService userService;

	@GetMapping("/list")
	public RequestBean<List<Note>> noteList() throws JsonProcessingException {

		return RequestBean.Success(noteService.queryAll());
	}

	@RequestMapping("/list1")
	public RequestBean<List<User>> noteList1() throws JsonProcessingException {

		return RequestBean.Success(userService.queryAll());
	}
}
