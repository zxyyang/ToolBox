package com.toolbox.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class MyDateTypeHandler extends BaseTypeHandler<List<Integer>> {


    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<Integer> integers, JdbcType jdbcType) throws SQLException {
        Integer ids = Integer.valueOf(Joiner.on(",").skipNulls().join(integers));
        preparedStatement.setInt(i, ids);
    }

    @Override
    public List<Integer> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return this.stringToList(resultSet.getString(s));
    }

    @Override
    public List<Integer> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return this.stringToList(resultSet.getString(i));
    }

    @Override
    public List<Integer> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return this.stringToList(callableStatement.getString(i));

    }

    private List<Integer> stringToList(String str) {
        return Strings.isNullOrEmpty(str) ? new ArrayList<>() : Collections.singletonList(Integer.valueOf(String.valueOf(Splitter.on(",").splitToList(str))));
    }

}
