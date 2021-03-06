package com.toolbox.service;

import java.util.List;

import com.toolbox.domain.File;

public interface FileService {

	Integer add(String fileName, String filePath);

	Integer updateName(String originFileName, String ObjectFileName);

	Integer batchDelete(String[] name);

	List<File> selectAll();

	String selectPathByName(String fileName);

	Integer updatePath(String fileOldPath, String fileNewPath);

	String[] selectName(String path);

	List<File> selectByName(String name);
}
