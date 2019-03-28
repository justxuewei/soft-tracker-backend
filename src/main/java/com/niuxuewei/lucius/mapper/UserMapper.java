package com.niuxuewei.lucius.mapper;
import org.apache.ibatis.annotations.Param;

import com.niuxuewei.lucius.domain.bo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectFirstByUsername(@Param("username")String username);

}