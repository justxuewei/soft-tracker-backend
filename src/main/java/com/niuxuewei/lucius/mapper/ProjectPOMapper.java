package com.niuxuewei.lucius.mapper;
import org.apache.ibatis.annotations.Param;

import com.niuxuewei.lucius.entity.po.ProjectPO;import com.niuxuewei.lucius.entity.po.ProjectWithMembersAndStatusPO;import com.niuxuewei.lucius.entity.po.ProjectWithMembersDetailsAndStatusPO;

import java.util.List;

public interface ProjectPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectPO record);

    int insertSelective(ProjectPO record);

    ProjectPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProjectPO record);

    int updateByPrimaryKey(ProjectPO record);

    ProjectWithMembersAndStatusPO selectFirstProjectWithoutUserDetailsByProjectId(Integer projectId);

    ProjectWithMembersDetailsAndStatusPO selectFirstProjectByProjectId(Integer projectId);

    ProjectPO selectFirstByName(@Param("name")String name);

    List<ProjectPO> selectByUserId(@Param("id") Integer id);

    ProjectPO selectFirstByProjectIdAndUserId(@Param("projectId") Integer projectId, @Param("userId") Integer userId);

}