package com.niuxuewei.lucius.mapper;

import com.niuxuewei.lucius.entity.po.CasePO;

public interface CasePOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CasePO record);

    int insertSelective(CasePO record);

    CasePO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CasePO record);

    int updateByPrimaryKey(CasePO record);
}