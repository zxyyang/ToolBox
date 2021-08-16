
package com.toolbox.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.toolbox.domain.Note;

@Mapper
public interface NoteMapper extends tk.mybatis.mapper.common.Mapper<Note> {

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
