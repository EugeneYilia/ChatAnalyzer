package com.EugeneStudio.ChatAnalyzer.mapper;

import com.EugeneStudio.ChatAnalyzer.model.Comment;

public interface CommentMapper {
    int deleteByPrimaryKey(String emotionState);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(String emotionState);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);
}