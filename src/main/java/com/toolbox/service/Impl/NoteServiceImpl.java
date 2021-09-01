package com.toolbox.service.Impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.toolbox.domain.Note;
import com.toolbox.mapper.NoteMapper;
import com.toolbox.service.NoteService;

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
		String strDateFormat = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
		note.setNoteTime(sdf.format(new Date()));
		return noteMapper.add(note);
	}

	@Override
	public PageInfo<Note> queryByName(@RequestParam(defaultValue = "1") Integer pageNumber, Integer pageSize, String noteName) {
		PageHelper.startPage(pageNumber, pageSize);// pageNum:当前页码数，第一次进来时默认为1（首页）
		PageInfo<Note> noteByNamePageInfo = new PageInfo<>(noteMapper.queryByName(noteName));// pageInfo:将分页数据和显示的数据封装到PageInfo当中
		return noteByNamePageInfo;
	}

	@Override
	public Integer update(Note note) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		note.setNoteTime(simpleDateFormat.format(new Date()));
		return noteMapper.update(note);
	}

	@Override
	public Note selectById(Integer id) {
		return noteMapper.selectById(id);
	}

}
