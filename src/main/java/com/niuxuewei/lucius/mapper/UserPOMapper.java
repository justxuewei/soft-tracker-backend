package com.niuxuewei.lucius.mapper;

import com.niuxuewei.lucius.entity.po.UserPO;import com.niuxuewei.lucius.entity.po.UserWithRolePO;import org.apache.ibatis.annotations.Param;import java.util.List;

public interface UserPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPO record);

    int insertSelective(UserPO record);

    UserPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPO record);

    int updateByPrimaryKey(UserPO record);

    UserPO selectFirstByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

    UserPO selectFirstByUsername(@Param("username") String username);

    List<UserWithRolePO> selectForSearchByUsername(@Param("username") String username);

    List<UserWithRolePO> selectForSearchByEmail(@Param("email") String email);

    UserWithRolePO selectFirstById(@Param("id") Integer id);
}