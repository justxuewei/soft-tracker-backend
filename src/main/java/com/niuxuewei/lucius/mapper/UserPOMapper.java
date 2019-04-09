package com.niuxuewei.lucius.mapper;
import org.apache.ibatis.annotations.Param;

import com.niuxuewei.lucius.entity.po.UserPO;

public interface UserPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPO record);

    int insertSelective(UserPO record);

    UserPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPO record);

    int updateByPrimaryKey(UserPO record);

    UserPO selectFirstByUsername(@Param("username")String username);



}