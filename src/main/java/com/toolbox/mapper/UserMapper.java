package com.toolbox.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.toolbox.domain.User;

@Mapper
public interface UserMapper {

	// get all
	@Select("select * from users")
	List<User> queryAll();

	// put in database
	@Insert(" insert into users values(#{username}, #{password},#{salt},#{label}) ")
	Integer add(User user);

	// get all by user's name
	@Select("select * from users where user_name = #{username}")
	User queryByName(String userName);

}
