package com.toolbox.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.toolbox.domain.Note;

@Mapper
public interface NoteMapper extends tk.mybatis.mapper.common.Mapper<Note> {

	// 查询所有
	@Select("select id,note_name,note_remark,note_type,note_time from note order by note_time  DESC")
	List<Note> queryAll();

	// 插入
	@Insert(" insert into note (note_name,note_content,note_remark,note_type,note_time) values(#{noteName}, #{noteContent},#{noteRemark},#{noteType},#{noteTime}) ")
	Integer add(Note note);

	// 根据名字查询
	@Select("select * from note where  note_name like   concat('%',#{noteName},'%')")
	List<Note> queryByName(String noteName);

	// 更新
	@Update("update note set  note_name = #{noteName},note_content = #{noteContent},note_remark = #{noteRemark},note_type = #{noteType},note_time = #{noteTime}  where id = #{id}")
	Integer update(Note note);

	@Select("SELECT * FROM note where id =#{id}")
	Note selectById(Integer id);

	@Delete("<script>" + "delete from note where id in"
			+ "<foreach collection ='ids' open='(' item='ids' separator = ',' close=')'> #{ids} </foreach>" + "" + "</script>")
	Integer deleteNote(Integer[] ids);
}
