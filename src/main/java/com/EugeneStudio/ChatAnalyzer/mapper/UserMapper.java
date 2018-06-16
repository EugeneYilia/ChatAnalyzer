package com.EugeneStudio.ChatAnalyzer.mapper;

import com.EugeneStudio.ChatAnalyzer.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByUsernameAndPassword(@Param("id") String id,@Param("password") String password);
}