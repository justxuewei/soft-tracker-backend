package com.niuxuewei.lucius.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.niuxuewei.lucius.entity.po.UserRolePO;

public interface UserRolePOMapper {
    int insert(UserRolePO record);

    int insertSelective(UserRolePO record);

    List<UserRolePO> selectByUserId(@Param("userId")Integer userId);

    int insertListWithoutId(@Param("list")List<UserRolePO> list);

}