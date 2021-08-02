package com.toolbox;

import com.toolbox.domain.User;
import com.toolbox.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@SpringBootTest
class ToolboxApplicationTests {
    @Autowired
    DataSource dataSource;

    @Test
    void contextLoads() throws SQLException {
        System.out.println(dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println(connection);

        //template模板，拿来即用
        connection.close();
    }
    @Autowired
    UserMapper userMapper;
    @Test
    public void toTest(){
        List<User> userLogins = userMapper.queryAll();
        userLogins.forEach(e-> System.out.println(e));
    }


}
