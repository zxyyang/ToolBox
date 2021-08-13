/*******************************************************************************
 * Copyright (c) 2021, 2021 Hirain Technologies Corporation.
 ******************************************************************************/
package com.toolbox.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.toolbox.domain.Note;

/**
 * @Version 1.0
 * @Author zixuan.yang
 * @Created 2021/8/13 17:47
 * @Description
 *              <p>
 * @Modification
 *               <p>
 *               Date Author Version Description
 *               <p>
 *               2021/8/13 zixuan.yang@hirain.com 1.0 create file
 */
@Mapper
public interface NoteMapper {

	// 查询所有
	@Select("select * from note")
	List<Note> queryAll();

	// 插入
	@Insert(" insert into note values(#{noteName}, #{noteContent},#{noteRemark},#{noteType}) ")
	Integer add(Note note);

	// 根据名字查询
	@Select("select * from users where username like '%#{noteName}%' ")
	Note queryByName(String noteName);

	// 更新
	@Update("update note set  note_name = #{noteName},note_content = #{noteContent},note_remark = #{noteRemark},note_type = #{noteType}  where id = #{id}")
	Integer update(Note note);

}
