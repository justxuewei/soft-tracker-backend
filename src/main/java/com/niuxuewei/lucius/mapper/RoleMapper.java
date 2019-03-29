package com.niuxuewei.lucius.mapper;
import org.apache.ibatis.annotations.Param;

import com.niuxuewei.lucius.entity.po.Role;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    Role selectFirstByRole(@Param("role")String role);


}