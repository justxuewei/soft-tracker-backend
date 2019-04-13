package com.niuxuewei.lucius.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.niuxuewei.lucius.entity.po.ProjectMemberPO;

public interface ProjectMemberPOMapper {
    int insert(ProjectMemberPO record);

    int insertSelective(ProjectMemberPO record);

    int insertListSelective(@Param("list")List<ProjectMemberPO> list);

    ProjectMemberPO selectFirstByProjectIdAndUserId(@Param("projectId")Integer projectId,@Param("userId")Integer userId);

	int deleteByProjectId(@Param("projectId")Integer projectId);



}