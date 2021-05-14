package com.sharding.table.mapper;

import com.sharding.table.dto.StudentVo;
import com.sharding.table.entity.StudentScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentScoreMapper {

    int insert(@Param("pojo") StudentScore pojo);

    int insertList(@Param("pojos") List<StudentScore> pojos);

    List<StudentScore> select(@Param("pojo") StudentScore pojo);

    int update(@Param("pojo") StudentScore pojo);

    List<StudentVo> selectStudentScore(@Param("studentName") String studentName, @Param("day") String day);
}
