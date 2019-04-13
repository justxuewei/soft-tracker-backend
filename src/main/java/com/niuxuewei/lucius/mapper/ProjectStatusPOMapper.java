package com.niuxuewei.lucius.mapper;

import com.niuxuewei.lucius.entity.po.ProjectStatusPO;import org.apache.ibatis.annotations.Param;

public interface ProjectStatusPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectStatusPO record);

    int insertSelective(ProjectStatusPO record);

    ProjectStatusPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProjectStatusPO record);

    int updateByPrimaryKey(ProjectStatusPO record);

    int deleteByProjectId(@Param("projectId") Integer projectId);
}