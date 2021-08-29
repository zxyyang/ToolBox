package com.toolbox.mapper;

import com.toolbox.domain.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("select file_name from file where file_path = #{path}")
    String[] selectName(String path);

    @Insert("insert into file (file_name,file_path) values(#{fileName},#{filePath}) ")
    Integer add(String fileName, String filePath);

    @Update("update file set file_name = #{ObjectFileName} where id =#{originFileName}")
    Integer updateName(String originFileName, String ObjectFileName);

    @Select("select file_path  from file where file_name = #{fileName} ")
    String selectPathByName(String fileName);

    @Select("select *  from file ")
    List<File> selectAll();

    @Delete("<script>" + "delete from file where file_name in"
            + "<foreach collection ='name' open='(' item='name' separator = ',' close=')'> #{name} </foreach>" + "" + "</script>")
    Integer batchDelete(@Param("name") String[] name);

    @Update("UPDATE file SET file_path = #{fileNewPath} WHERE file_path = #{fileOldPath}")
    Integer updatePath(String fileOldPath, String fileNewPath);
}
