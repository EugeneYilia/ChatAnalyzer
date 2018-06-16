package com.EugeneStudio.ChatAnalyzer.service.impl;


import com.EugeneStudio.ChatAnalyzer.mapper.UserMapper;
import com.EugeneStudio.ChatAnalyzer.model.User;
import com.EugeneStudio.ChatAnalyzer.service.UserInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserInformationServiceImpl implements UserInformationService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Boolean findUser(String id, String password) {
        User user = userMapper.selectByUsernameAndPassword(id, password);
        System.out.println(user);
        if(user == null){
            return false;
        }else{
            return true;
        }
    }
}
