package com.niuxuewei.lucius.mapper;

import com.niuxuewei.lucius.entity.po.ProjectPO;

public interface ProjectPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectPO record);

    int insertSelective(ProjectPO record);

    ProjectPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProjectPO record);

    int updateByPrimaryKey(ProjectPO record);
}