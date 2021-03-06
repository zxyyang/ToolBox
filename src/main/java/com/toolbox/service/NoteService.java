package com.toolbox.service;

import com.github.pagehelper.PageInfo;
import com.toolbox.domain.Note;

public interface NoteService {

	PageInfo<Note> queryAll(Integer pageNumber, Integer pageSize);

	Integer add(Note note);

	PageInfo<Note> queryByName(Integer pageNumber, Integer pageSize, String noteName);

	Integer update(Note note);

	Note selectById(Integer id);

	Integer deleteNote(Integer[] ids);

}
