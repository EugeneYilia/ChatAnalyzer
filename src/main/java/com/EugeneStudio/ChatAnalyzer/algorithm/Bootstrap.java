package com.EugeneStudio.ChatAnalyzer.algorithm;

import java.util.ArrayList;

public class Bootstrap {
    public static void main(String[] args) {
        Parser parser = new Parser();

        //聊天记录导入
        ArrayList<String> userNames = parser.getUserNames("Beholder.txt");

        //自己和对方用户名称的设置
        ArrayList<String> myNames = new ArrayList<String>();
        myNames.add("Ekyl");
        myNames.add("Eugene Liu");
        myNames.add("Alpha");
        parser.setUserNames(myNames);

        System.out.println("myNames ↓");
        parser.showMyNames();
        System.out.println("otherNames ↓");
        parser.showOtherNames();

        //聊天记录的分析
        parser.chatRecordAnalyze();

    }
}
