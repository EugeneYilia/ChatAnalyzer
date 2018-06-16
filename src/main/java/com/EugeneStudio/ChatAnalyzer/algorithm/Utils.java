package com.EugeneStudio.ChatAnalyzer.algorithm;

import java.util.ArrayList;

public class Utils {
    private Utils() {
    }

    public static String getTime(Long sourceTime) {
        String returnTime = "所用时间为";
        String secondRecord = "";
        String miniuteRecord = "";
        String hourRecord = "";
        String dayRecord = "";
        String monRecord = "";
        String yearRecord = "";
        if (sourceTime / 60 >= 0) {
            secondRecord = String.valueOf(sourceTime % 60);
            sourceTime = sourceTime / 60;
            if (sourceTime / 60 >= 0) {
                miniuteRecord = String.valueOf(sourceTime % 60);
                sourceTime = sourceTime / 60;
                if(miniuteRecord.equals("0")){
                    miniuteRecord = "";
                }
                if (sourceTime / 24 >= 0) {
                    hourRecord = String.valueOf(sourceTime % 24);
                    sourceTime = sourceTime / 24;
                    if(hourRecord.equals("0")){
                        hourRecord = "";
                    }
                    if (sourceTime / 30 >= 0) {
                        dayRecord = String.valueOf(sourceTime % 30);
                        sourceTime = sourceTime / 30;
                        if(dayRecord.equals("0")){
                            dayRecord = "";
                        }
                        if (sourceTime / 12 >= 0) {
                            monRecord = String.valueOf(sourceTime % 12);
                            yearRecord = String.valueOf(sourceTime / 12);
                            if(monRecord.equals("0")){
                                monRecord = "";
                            }
                            if(yearRecord.equals("0")){
                                yearRecord = "";
                            }
                        }
                    }
                }
            }
        }else{
            secondRecord = String.valueOf("0");
        }
        if (yearRecord.length() != 0) {
            returnTime += yearRecord+"年"+monRecord+"月"+dayRecord+"天"+hourRecord+"小时"+miniuteRecord+"分钟"+secondRecord+"秒";
        } else if (monRecord.length() != 0) {
            returnTime += monRecord+"月"+dayRecord+"天"+hourRecord+"小时"+miniuteRecord+"分钟"+secondRecord+"秒";
        } else if (dayRecord.length() != 0) {
            returnTime += dayRecord+"天"+hourRecord+"小时"+miniuteRecord+"分钟"+secondRecord+"秒";
        } else if (hourRecord.length() != 0) {
            returnTime += hourRecord+"小时"+miniuteRecord+"分钟"+secondRecord+"秒";
        } else if (miniuteRecord.length() != 0) {
            returnTime += miniuteRecord+"分钟"+secondRecord+"秒";
        } else {
            returnTime += secondRecord+"秒";
        }
        return returnTime;
    }

    public static Long getAverage(ArrayList<Long> arrayList){
        Long sum = 0L;
        for(int i=0;i<arrayList.size();i++){
            sum += arrayList.get(i);
        }
        return sum/arrayList.size();
    }
}
