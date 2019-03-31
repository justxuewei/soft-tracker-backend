package com.niuxuewei.lucius.mapper;
import org.apache.ibatis.annotations.Param;

import com.niuxuewei.lucius.entity.po.GitlabUserPO;

public interface GitlabUserPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GitlabUserPO record);

    int insertSelective(GitlabUserPO record);

    GitlabUserPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GitlabUserPO record);

    int updateByPrimaryKey(GitlabUserPO record);

    GitlabUserPO selectFirstByUserId(@Param("userId")Integer userId);


}