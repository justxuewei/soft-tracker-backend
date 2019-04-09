package com.niuxuewei.lucius.mapper;
import com.niuxuewei.lucius.entity.po.UserRolePO;
import org.apache.ibatis.annotations.Param;

import com.niuxuewei.lucius.entity.po.RolePO;

import java.util.List;

public interface RolePOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RolePO record);

    int insertSelective(RolePO record);

    RolePO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RolePO record);

    int updateByPrimaryKey(RolePO record);

    RolePO selectFirstByRole(@Param("role")String role);

    List<RolePO> selectRoleByRoleIds(List<UserRolePO> userRolePOList);

}