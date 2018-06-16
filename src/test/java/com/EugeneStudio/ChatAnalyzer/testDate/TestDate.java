package com.EugeneStudio.ChatAnalyzer.testDate;

import com.EugeneStudio.ChatAnalyzer.algorithm.Utils;

import java.text.SimpleDateFormat;

public class TestDate {
    public static void main(String[] args) {
//        String date = Utils.getTime(System.currentTimeMillis());
//       System.out.println(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String date = simpleDateFormat.format(System.currentTimeMillis());
        System.out.println(date);
    }
}
