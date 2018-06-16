package com.EugeneStudio.ChatAnalyzer.algorithm;

public class Word {
    private String word;
    private int time = 0;
    public Word(){}
    public Word(String word){
        this.word = word;
    }
    public Word(String word,int time){
        this.word = word;
        this.time = time;
    }

    public String getWord() {
        return word;
    }

    public int getTime() {
        return time;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
