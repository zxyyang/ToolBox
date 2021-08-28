package com.toolbox.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.toolbox.domain.Note;
import com.toolbox.mapper.NoteMapper;
import com.toolbox.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteMapper noteMapper;

    @Override
    public PageInfo<Note> queryAll(@RequestParam(defaultValue = "1") Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber, pageSize);// pageNum:当前页码数，第一次进来时默认为1（首页）
        PageInfo<Note> notePageInfo = new PageInfo<>(noteMapper.queryAll());// pageInfo:将分页数据和显示的数据封装到PageInfo当中
        return notePageInfo;
    }

    @Override
    public Integer add(Note note) {
        note.setNoteTime(new Date().toString());
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

    @Override
    public Note selectById(Integer id) {
        return noteMapper.selectById(id);
    }
}
