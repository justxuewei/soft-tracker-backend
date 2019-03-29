package com.niuxuewei.lucius.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.niuxuewei.lucius.entity.po.UserRole;

public interface UserRoleMapper {
    int insert(UserRole record);

    int insertSelective(UserRole record);

    List<UserRole> selectByUserId(@Param("userId")Integer userId);

    int insertListWithoutId(@Param("list")List<UserRole> list);


}