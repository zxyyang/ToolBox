package com.toolbox.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.toolbox.domain.User;

@Mapper
public interface UserMapper {

	// 查询所有
	@Select("select * from users")
	List<User> queryAll();

	// 插入
	@Insert(" insert into users values(#{username}, #{password},#{salt},#{label}) ")
	Integer add(User user);

	// 根据名字查询
	@Select("select * from users where username = #{username}")
	User queryByName(String username);

}
