package com.sharding.dynamic.mapper;

import com.sharding.dynamic.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper {

    int insert(@Param("pojo") Student pojo);

    int insertList(@Param("pojos") List<Student> pojo);

    List<Student> select(@Param("pojo") Student pojo);

    int update(@Param("pojo") Student pojo);

}
