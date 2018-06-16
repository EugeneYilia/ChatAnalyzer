package com.EugeneStudio.ChatAnalyzer.pojo;

import com.EugeneStudio.ChatAnalyzer.model.User;

public class UserStatus {
    boolean isExist ;
    public UserStatus(boolean isExist){
        this.isExist = isExist;
    }

    public void setIsExist(boolean exist) {
        isExist = exist;
    }

    public boolean getIsExist(){
        return this.isExist;
    }
}
