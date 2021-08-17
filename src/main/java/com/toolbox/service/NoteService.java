package com.toolbox.service;

import com.github.pagehelper.PageInfo;
import com.toolbox.domain.Note;

public interface NoteService {

	PageInfo<Note> queryAll(Integer pageNumber, Integer pageSize);

	Integer add(Note note);

	Note queryByName(String noteName);

	Integer update(Note note);
}
