package com.niuxuewei.lucius.mapper;

import com.niuxuewei.lucius.entity.po.Gitlab;

public interface GitlabMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Gitlab record);

    int insertSelective(Gitlab record);

    Gitlab selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Gitlab record);

    int updateByPrimaryKey(Gitlab record);
}