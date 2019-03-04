package com.niuxuewei.lucius.mapper;

import com.niuxuewei.lucius.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> selectAll();

}
