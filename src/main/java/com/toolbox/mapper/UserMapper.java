package com.toolbox.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.toolbox.domain.User;

/**
 * @Version 1.0
 * @Author zixuan.yang
 * @Created 2021/8/13 17:35
 * @Description
 *              <p>
 * @Modification
 *               <p>
 *               Date Author Version Description
 *               <p>
 *               2021/8/13 zixuan.yang@hirain.com 1.0 create file
 */
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
