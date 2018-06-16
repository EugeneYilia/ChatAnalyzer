package com.EugeneStudio.ChatAnalyzer.model;

public class User_basic_informationWithBLOBs extends User_basic_information {
    private byte[] basicInformationPicture;

    private byte[] emotionPicture;

    public byte[] getBasicInformationPicture() {
        return basicInformationPicture;
    }

    public void setBasicInformationPicture(byte[] basicInformationPicture) {
        this.basicInformationPicture = basicInformationPicture;
    }

    public byte[] getEmotionPicture() {
        return emotionPicture;
    }

    public void setEmotionPicture(byte[] emotionPicture) {
        this.emotionPicture = emotionPicture;
    }
}