package com.toolbox.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolbox.domain.File;
import com.toolbox.mapper.FileMapper;
import com.toolbox.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Autowired
	private FileMapper fileMapper;

	@Override
	public Integer add(String fileName, String filePath) {
		return fileMapper.add(fileName, filePath);
	}

	@Override
	public Integer updateName(String originFileName, String ObjectFileName) {
		return fileMapper.updateName(originFileName, ObjectFileName);
	}

	@Override
	public Integer batchDelete(String[] name) {
		return fileMapper.batchDelete(name);
	}

	@Override
	public List<File> selectAll() {
		return fileMapper.selectAll();
	}

	@Override
	public String selectPathByName(String fileName) {
		return fileMapper.selectPathByName(fileName);
	}

	@Override
	public Integer updatePath(String fileOldPath, String fileNewPath) {
		return fileMapper.updatePath(fileOldPath, fileNewPath);
	}

	// @Override
	// public String[] selectName(String path) {
	// String[] names = fileMapper.selectName(path);
	// List<String> filenames = new ArrayList<>();
	// for (String name : names) {
	// if (!name.endsWith("/")) {
	// filenames.add(name);
	// }
	// }
	// String[] strArray = filenames.toArray(new String[filenames.size()]);
	//
	// return strArray;
	// }
	@Override
	public String[] selectName(String path) {

		return fileMapper.selectName(path);
	}

	@Override
	public List<File> selectByName(String name) {
		return fileMapper.selectByName(name);
	}
}