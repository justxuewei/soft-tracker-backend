package com.niuxuewei.lucius.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.niuxuewei.lucius.entity.po.CaseTagPO;

public interface CaseTagPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CaseTagPO record);

    int insertSelective(CaseTagPO record);

    CaseTagPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CaseTagPO record);

    int updateByPrimaryKey(CaseTagPO record);

    int insertList(@Param("list")List<CaseTagPO> list);


}