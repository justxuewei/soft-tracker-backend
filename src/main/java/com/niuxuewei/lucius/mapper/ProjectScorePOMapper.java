package com.niuxuewei.lucius.mapper;

import com.niuxuewei.lucius.entity.po.ProjectScorePO;

public interface ProjectScorePOMapper {
    int deleteByPrimaryKey(Integer projectId);

    int insert(ProjectScorePO record);

    int insertSelective(ProjectScorePO record);

    ProjectScorePO selectByPrimaryKey(Integer projectId);

    int updateByPrimaryKeySelective(ProjectScorePO record);

    int updateByPrimaryKey(ProjectScorePO record);
}