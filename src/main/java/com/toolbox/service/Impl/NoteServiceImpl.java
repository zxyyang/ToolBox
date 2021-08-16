package com.toolbox.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolbox.domain.Note;
import com.toolbox.mapper.NoteMapper;
import com.toolbox.service.NoteService;

@Service
public class NoteServiceImpl implements NoteService {

	@Autowired
	NoteMapper noteMapper;

	@Override
	public List<Note> queryAll() {
		return noteMapper.queryAll();
	}

	@Override
	public Integer add(Note note) {
		return noteMapper.add(note);
	}

	@Override
	public Note queryByName(String noteName) {
		return noteMapper.queryByName(noteName);
	}

	@Override
	public Integer update(Note note) {
		return noteMapper.update(note);
	}
}
