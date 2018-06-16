package com.EugeneStudio.ChatAnalyzer.mapper;

import com.EugeneStudio.ChatAnalyzer.model.User_basic_informationWithBLOBs;

public interface User_basic_informationMapper {
    int insert(User_basic_informationWithBLOBs record);

    int insertSelective(User_basic_informationWithBLOBs record);
}