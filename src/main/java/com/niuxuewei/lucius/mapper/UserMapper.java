package com.niuxuewei.lucius.mapper;

import com.niuxuewei.lucius.entity.domain.User;import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectFirstByUsername(@Param("username") String username);
}