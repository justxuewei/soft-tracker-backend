package com.niuxuewei.lucius.mapper;

import com.niuxuewei.lucius.entity.po.CasePO;
import com.niuxuewei.lucius.entity.po.CaseWithTagsPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CasePOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CasePO record);

    int insertSelective(CasePO record);

    CasePO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CasePO record);

    int updateByPrimaryKey(CasePO record);

    List<CaseWithTagsPO> selectWithTags();

    List<CaseWithTagsPO> selectWithTagsByTutorId(@Param("id") Integer id);

    CaseWithTagsPO selectFirstByCaseId(@Param("id") Integer id);

    CasePO selectFirstByIdAndAuthor(@Param("id")Integer id,@Param("author")Integer author);
}