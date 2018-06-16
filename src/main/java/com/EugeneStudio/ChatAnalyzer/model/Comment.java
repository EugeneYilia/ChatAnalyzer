package com.EugeneStudio.ChatAnalyzer.model;

public class Comment {
    private String emotionState;

    private String comment;

    public String getEmotionState() {
        return emotionState;
    }

    public void setEmotionState(String emotionState) {
        this.emotionState = emotionState == null ? null : emotionState.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }
}