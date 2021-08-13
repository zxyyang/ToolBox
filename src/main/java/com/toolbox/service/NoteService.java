package com.toolbox.service;

import com.toolbox.domain.Note;

import java.util.List;

public interface NoteService {

    List<Note> queryAll();


    Integer add(Note note);


    Note queryByName(String noteName);


   Integer update(Note note);
}
