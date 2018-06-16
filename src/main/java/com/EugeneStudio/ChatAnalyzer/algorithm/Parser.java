package com.EugeneStudio.ChatAnalyzer.algorithm;

import com.EugeneStudio.ChatAnalyzer.algorithm.pictureGenerator.MySpriderWebPlotTest;

import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.EugeneStudio.ChatAnalyzer.algorithm.AlgorithmConstants.CHAT_RECORD_ROOT;
import static com.EugeneStudio.ChatAnalyzer.algorithm.AlgorithmConstants.WORDS;

public class Parser {
    private ArrayList<String> userNames = new ArrayList<>();
    private String fileName = "";
    private ArrayList<String> myNames = new ArrayList<>();
    private ArrayList<String> otherNames = new ArrayList<>();

    private ArrayList<Word> myWords = new ArrayList<>();
    private ArrayList<Word> otherWords = new ArrayList<>();

    private ArrayList<Long> myChatTimeStamp = new ArrayList<>();
    private ArrayList<Long> otherChatTimeStamp = new ArrayList<>();
    private ArrayList<Long> myReplyTime = new ArrayList<>();
    private ArrayList<Long> otherReplyTime = new ArrayList<>();

    private ArrayList<Long> myLatelyReplyTime = new ArrayList<>();
    private ArrayList<Long> otherLatelyReplyTime = new ArrayList<>();

    private ArrayList<Long> myLatelyInteractionTime = new ArrayList<>();
    private ArrayList<Long> otherLatelyInteractionTime = new ArrayList<>();

    private ArrayList<Long> myInteractionTime = new ArrayList<>();
    private ArrayList<Long> otherInteractionTime = new ArrayList<>();

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DecimalFormat decimalFormat = new DecimalFormat("###.00");

    private int myEndConversationTime = 0;
    private int otherEndConversationTime = 0;

    private int myConversationTime = 0;
    private int myValidConversationtime = 0;

    private int myOptimizedConversationTime = 0;
    private int otherOptimizedConversationTime = 0;

    private int otherConversationtime = 0;
    private int otherValidConversationTime = 0;

    private String firstConversationDate = "";
    private String lastConversationDate = "";

    private int myIgnoredTime = 0;
    private int otherIgnoredTime = 0;

    public Parser() {
        for (String word : WORDS) {
            myWords.add(new Word(word, 0));
            otherWords.add(new Word(word, 0));
        }
    }

    public void chatRecordAnalyze() {
        File file = new File(CHAT_RECORD_ROOT, fileName);
        wordFrequencyAnalyze(file);
        attitudeAnalyze(file);
        String myTopic = topicAnalyze(myWords);
        String otherTopic = topicAnalyze(otherWords);
        System.out.println("我比较感兴趣的话题可能是:" + myTopic);
        System.out.println("对方比较感兴趣的话题可能是:" + otherTopic);
        emotionAnalyze(file);

        printBasicInformation();
        generatePicture();
        //printOtherWords();
    }

    public void printOtherWords() {
        for (int i = 0; i < otherWords.size(); i++) {
            System.out.println(otherWords.get(i).getWord());
        }
    }

    public void wordFrequencyAnalyze(File file) {
        boolean isMe = true;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            filterHeaders(bufferedReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (line.length() == 0) {
                    String nameLine = bufferedReader.readLine();
                    if (nameLine == null) {
                        break;
                    } else if (nameLine.length() == 0) {
                        nameLine = bufferedReader.readLine();
                        if (nameLine == null) {
                            break;
                        } else {
                            if (!nameLine.startsWith("20")) {
                                continue;
                            }
                            String name = nameLine.substring(nameLine.indexOf(" ") + 1);
                            name = name.substring(name.indexOf(" ") + 1);
                            if (nameLine.equals(name)) {
                                continue;
                            }
                            if (isMyName(name)) {
                                myConversationTime++;
                                isMe = true;
                            } else {
                                otherConversationtime++;
                                isMe = false;
                            }
                        }
                    } else {
                        if (!nameLine.startsWith("20")) {
                            continue;
                        }
                        String name = nameLine.substring(nameLine.indexOf(" ") + 1);
                        name = name.substring(name.indexOf(" ") + 1);
                        if (nameLine.equals(name)) {
                            continue;
                        }
                        if (isMyName(name)) {
                            myConversationTime++;
                            isMe = true;
                        } else {
                            otherConversationtime++;
                            isMe = false;
                        }
                    }
                } else {//聊天正文内容
                    if (isMe) {
                        analyzeContentByWords(line, true);
                    } else {
                        analyzeContentByWords(line, false);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printFrequentWords();
    }

   /* public void ignoredTimeAnalyze(File file) {
        boolean isMe = true;
        int myFrequentConversationTime = 0;
        int otherFrequentConversationTime = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            filterHeaders(bufferedReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (line.length() == 0) {
                    String nameLine = bufferedReader.readLine();
                    if (nameLine == null) {
                        break;
                    } else if (nameLine.length() == 0) {
                        nameLine = bufferedReader.readLine();
                        if (nameLine == null) {
                            break;
                        } else {
                            if (!nameLine.startsWith("20")) {
                                continue;
                            }
                            String name = nameLine.substring(nameLine.indexOf(" ") + 1);
                            name = name.substring(name.indexOf(" ") + 1);
                            if (nameLine.equals(name)) {
                                continue;
                            }
                            if (isMyName(name)) {
                                myConversationTime++;
                                isMe = true;
                            } else {
                                otherConversationtime++;
                                isMe = false;
                            }
                        }
                    } else {
                        if (!nameLine.startsWith("20")) {
                            continue;
                        }
                        String name = nameLine.substring(nameLine.indexOf(" ") + 1);
                        name = name.substring(name.indexOf(" ") + 1);
                        if (nameLine.equals(name)) {
                            continue;
                        }
                        if (isMyName(name)) {
                            myConversationTime++;
                            isMe = true;
                        } else {
                            otherConversationtime++;
                            isMe = false;
                        }
                    }
                } else {//聊天正文内容
                    if (isMe) {
                        analyzeContentByWords(line, true);
                    } else {
                        analyzeContentByWords(line, false);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printFrequentWords();
    }*/

    public void printFrequentWords() {
        ArrayList<Word> myFrequentWords = getMyFrequentWords(10);
        ArrayList<Word> otherFrequentWords = getOtherFrequentWords(10);
        System.out.println("########我使用频率最多的一些词名和次数  START#######");
        for (Word myFrequentWord : myFrequentWords) {
            System.out.println("词名:" + myFrequentWord.getWord() + "  " + "次数:" + myFrequentWord.getTime());
        }
        System.out.println("#######我使用频率最多的一些词名和次数  END###########");
        System.out.println();
        System.out.println("########对方使用频率最多的一些词名和次数  START#######");
        for (Word otherFrequentWord : otherFrequentWords) {
            System.out.println("词名:" + otherFrequentWord.getWord() + "  " + "次数:" + otherFrequentWord.getTime());
        }
        System.out.println("########对方使用频率最多的一些词名和次数  END########");
        System.out.println();
    }

    private void analyzeContentByWords(String content, boolean isMe) {
        if (isMe) {
            for (Word word : myWords) {
                int count = countString(content, word.getWord());
                if (count != 0) {
                    word.setTime(word.getTime() + count);
                }
            }
        } else {
            for (Word word : otherWords) {
                int count = countString(content, word.getWord());
                if (count != 0) {
                    word.setTime(word.getTime() + count);
                }
            }
        }
    }

    public void printBasicInformation() {
        System.out.println("##############基本信息  START############");
        System.out.println("我所说的消息总数:" + myConversationTime);
        System.out.println("对方所说的消息总数:" + otherConversationtime);
        System.out.println("双方刚认识的时间:" + firstConversationDate);
        System.out.println("双方最后交谈的时间:" + lastConversationDate);
        System.out.println("双方总共进行过的话题次数:" + (myInteractionTime.size() + otherInteractionTime.size()));
        System.out.println("我发起的话题次数" + myInteractionTime.size());
        System.out.println("对方发起的话题次数" + otherInteractionTime.size());

        System.out.println("我平均回复时间:" + Utils.getTime(Utils.getAverage(myReplyTime)));
        System.out.println("对方平均回复时间:" + Utils.getTime(Utils.getAverage(otherReplyTime)));

        System.out.println("最近一个月我平均回复时间:" + Utils.getTime(Utils.getAverage(myLatelyReplyTime)));
        System.out.println("最近一个月对方平均回复时间:" + Utils.getTime(Utils.getAverage(otherLatelyReplyTime)));

        System.out.println("我平均交互时间:" + Utils.getTime(Utils.getAverage(myInteractionTime)));
        System.out.println("对方平均交互时间:" + Utils.getTime(Utils.getAverage(otherInteractionTime)));

        System.out.println("最近一个月我平均交互时间:" + Utils.getTime(Utils.getAverage(myLatelyInteractionTime)));
        System.out.println("最近一个月对方平均交互时间:" + Utils.getTime(Utils.getAverage(otherLatelyInteractionTime)));

        System.out.println("我被对方忽略的次数:" + myIgnoredTime);
        System.out.println("对方被我忽略的次数:" + otherIgnoredTime);


        if ((Utils.getAverage(myInteractionTime)) > (Utils.getAverage(myLatelyInteractionTime))) {
            System.out.println("最近一个月你对对方的交互时间缩短了哦");
        } else if ((Utils.getAverage(myInteractionTime)) == (Utils.getAverage(myLatelyInteractionTime))) {
            System.out.println("最近一个月你对对方的交互时间保持不变，好厉害哦");
        } else {
            System.out.println("最近一个月你对对方的交互时间延长了哦");
        }

        if (Utils.getAverage(otherInteractionTime) > Utils.getAverage(otherLatelyInteractionTime)) {
            System.out.println("最近一个月对方对你的交互时间缩短了哦");
        } else if ((Utils.getAverage(otherInteractionTime) == Utils.getAverage(otherLatelyInteractionTime))) {
            System.out.println("最近一个月对方对你的交互时间保持不变，好厉害哦");
        } else {
            System.out.println("最近一个月对方对你的交互时间延长了哦");
        }
        System.out.println("##############基本信息  END##############");
    }

    private int countString(String sourceString, String targetString) {
        int count = 0;
        while (sourceString.indexOf(targetString) != -1) {
            sourceString = sourceString.substring(sourceString.indexOf(targetString) + 1, sourceString.length());
            count++;
        }
        return count;
    }

    public void attitudeAnalyze(File file) {
        boolean isMe = true;
        myChatTimeStamp.clear();
        otherChatTimeStamp.clear();

        //int myFrequentConversationTime = 0;
        //int otherFrequentConversationTime = 0;

        int myTopicConversation = 0;
        int otherTopicConversation = 0;

        Long nowTime = System.currentTimeMillis();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            filterHeaders(bufferedReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (line.length() == 0) {
                    String nameLine = bufferedReader.readLine();
                    if (nameLine == null) {
                        break;
                    } else if (nameLine.length() == 0) {
                        nameLine = bufferedReader.readLine();
                        if (nameLine == null) {
                            break;
                        } else {
                            if (!nameLine.startsWith("20")) {
                                continue;
                            }
                            String date = nameLine.substring(0, nameLine.indexOf(" "));
                            String name = nameLine.substring(nameLine.indexOf(" ") + 1);
                            String time = name.substring(0, name.indexOf(" "));
                            name = name.substring(name.indexOf(" ") + 1);
                            String fullTime = date + " " + time;
                            lastConversationDate = fullTime;
                            Date fullDate = simpleDateFormat.parse(fullTime);
                            Long fullMillisecond = fullDate.getTime();

                            boolean isLately = false;
                            Long value = (nowTime - fullMillisecond);
                            value = (value / (1000 * 60 * 60 * 24)) / 30;
                            if (value < 1) {
                                isLately = true;
                            }

                            if (nameLine.equals(name)) {
                                continue;
                            }
                            if (isMyName(name)) {
                                isMe = true;
                                Long myReply;
                                if (otherChatTimeStamp.size() != 0) {
                                    myReply = (fullMillisecond - otherChatTimeStamp.get(0)) / 1000;
                                    myReplyTime.add(myReply);
                                    myOptimizedConversationTime++;
                                    if (isLately) {
                                        myLatelyReplyTime.add(myReply);
                                    }
                                    if (myReply <= AlgorithmConstants.Limit) {
                                        //System.out.println(myReply);
                                        myInteractionTime.add(myReply);
                                        if (isLately) {
                                            myLatelyInteractionTime.add(myReply);
                                        }
                                        otherValidConversationTime++;
                                        myTopicConversation++;
                                    } else {
                                       /* if (otherFrequentConversationTime == 1) {
                                            //otherIgnoredTime++;
                                        }*/

                                        //新话题开始
                                        if (myTopicConversation == 0 && otherTopicConversation == 0) {

                                        } else if (myTopicConversation == 0 && otherTopicConversation > 0) {
                                            otherIgnoredTime++;
                                        } else if (myTopicConversation > 0 && otherTopicConversation == 0) {
                                            myIgnoredTime++;
                                        }
                                        myTopicConversation = 0;
                                        otherTopicConversation = 0;
                                        myTopicConversation++;
                                        otherEndConversationTime++;
                                    }
                                } else {
                                    if (myChatTimeStamp.size() != 0) {
                                        myReply = (fullMillisecond - myChatTimeStamp.get(myChatTimeStamp.size() - 1)) / 1000;
                                        if (myReply > AlgorithmConstants.Limit) {
                                            myEndConversationTime++;
                                            if (myTopicConversation == 0 && otherTopicConversation == 0) {

                                            } else if (myTopicConversation == 0 && otherTopicConversation > 0) {
                                                otherIgnoredTime++;
                                            } else if (myTopicConversation > 0 && otherTopicConversation == 0) {
                                                myIgnoredTime++;
                                            }
                                            myTopicConversation = 0;
                                            otherTopicConversation = 0;
                                            myTopicConversation++;
                                            /*if (myFrequentConversationTime == 1) {
                                                //myIgnoredTime++;
                                            }*/
                                        } else if (myReply > 5) {
                                            myOptimizedConversationTime++;
                                            myTopicConversation++;//小于五秒的话多条合并，因为要算话题被忽略次数，因此这块无所谓，只要加上就行
                                        }
                                    } else {
                                        firstConversationDate = fullTime;
                                    }
                                }
                                otherChatTimeStamp.clear();
                                myChatTimeStamp.add(fullMillisecond);

                               /* myFrequentConversationTime++;
                                otherFrequentConversationTime = 0;*/

                            } else {
                                isMe = false;
                                Long otherReply;
                                if (myChatTimeStamp.size() != 0) {
                                    otherReply = (fullMillisecond - myChatTimeStamp.get(0)) / 1000;
                                    otherReplyTime.add(otherReply);
                                    otherOptimizedConversationTime++;
                                    if (isLately) {
                                        otherLatelyReplyTime.add(otherReply);
                                    }
                                    if (otherReply <= AlgorithmConstants.Limit) {
                                        //System.out.println(otherReply);
                                        otherInteractionTime.add(otherReply);
                                        if (isLately) {
                                            otherLatelyInteractionTime.add(otherReply);
                                        }
                                        myValidConversationtime++;
                                        otherTopicConversation++;
                                    } else {
                                       /* if(myFrequentConversationTime == 1){
                                            //myIgnoredTime++;
                                        }*/
                                        if (myTopicConversation == 0 && otherTopicConversation == 0) {

                                        } else if (myTopicConversation == 0 && otherTopicConversation > 0) {
                                            otherIgnoredTime++;
                                        } else if (myTopicConversation > 0 && otherTopicConversation == 0) {
                                            myIgnoredTime++;
                                        }
                                        myTopicConversation = 0;
                                        otherTopicConversation = 0;
                                        otherTopicConversation++;
                                        myEndConversationTime++;
                                    }
                                } else {
                                    if (otherChatTimeStamp.size() != 0) {
                                        otherReply = (fullMillisecond - otherChatTimeStamp.get(otherChatTimeStamp.size() - 1)) / 1000;
                                        if (otherReply > AlgorithmConstants.Limit) {
                                            /*if(otherFrequentConversationTime == 1){
                                                //otherIgnoredTime++;
                                            }*/
                                            if (myTopicConversation == 0 && otherTopicConversation == 0) {

                                            } else if (myTopicConversation == 0 && otherTopicConversation > 0) {
                                                otherIgnoredTime++;
                                            } else if (myTopicConversation > 0 && otherTopicConversation == 0) {
                                                myIgnoredTime++;
                                            }
                                            myTopicConversation = 0;
                                            otherTopicConversation = 0;
                                            otherTopicConversation++;
                                            otherEndConversationTime++;
                                        } else if (otherReply > 5) {
                                            otherTopicConversation++;
                                            otherOptimizedConversationTime++;
                                        }
                                    } else {
                                        firstConversationDate = fullTime;
                                    }
                                }
                                myChatTimeStamp.clear();
                                otherChatTimeStamp.add(fullMillisecond);

                               /* otherFrequentConversationTime++;
                                myFrequentConversationTime = 0;*/
                            }
                        }
                    } else {
                        if (!nameLine.startsWith("20")) {
                            continue;
                        }
                        String date = nameLine.substring(0, nameLine.indexOf(" "));
                        String name = nameLine.substring(nameLine.indexOf(" ") + 1);
                        String time = name.substring(0, name.indexOf(" "));
                        name = name.substring(name.indexOf(" ") + 1);
                        String fullTime = date + " " + time;
                        lastConversationDate = fullTime;
                        Date fullDate = simpleDateFormat.parse(fullTime);
                        Long fullMillisecond = fullDate.getTime();

                        boolean isLately = false;
                        Long value = (nowTime - fullMillisecond);
                        value = (value / (1000 * 60 * 60 * 24)) / 30;
                        if (value < 1) {
                            isLately = true;
                        }

                        if (nameLine.equals(name)) {
                            continue;
                        }
                        if (nameLine.equals(name)) {
                            continue;
                        }
                        if (isMyName(name)) {
                            isMe = true;
                            Long myReply;
                            if (otherChatTimeStamp.size() != 0) {
                                myReply = (fullMillisecond - otherChatTimeStamp.get(0)) / 1000;
                                myReplyTime.add(myReply);
                                myOptimizedConversationTime++;
                                if (isLately) {
                                    myLatelyReplyTime.add(myReply);
                                }
                                if (myReply <= AlgorithmConstants.Limit) {
                                    //System.out.println(myReply);
                                    myInteractionTime.add(myReply);
                                    if (isLately) {
                                        myLatelyInteractionTime.add(myReply);
                                    }
                                    otherValidConversationTime++;
                                    myTopicConversation++;
                                } else {
                                       /* if (otherFrequentConversationTime == 1) {
                                            //otherIgnoredTime++;
                                        }*/

                                    //新话题开始
                                    if (myTopicConversation == 0 && otherTopicConversation == 0) {

                                    } else if (myTopicConversation == 0 && otherTopicConversation > 0) {
                                        otherIgnoredTime++;
                                    } else if (myTopicConversation > 0 && otherTopicConversation == 0) {
                                        myIgnoredTime++;
                                    }
                                    myTopicConversation = 0;
                                    otherTopicConversation = 0;
                                    myTopicConversation++;
                                    otherEndConversationTime++;
                                }
                            } else {
                                if (myChatTimeStamp.size() != 0) {
                                    myReply = (fullMillisecond - myChatTimeStamp.get(myChatTimeStamp.size() - 1)) / 1000;
                                    if (myReply > AlgorithmConstants.Limit) {
                                        myEndConversationTime++;
                                        if (myTopicConversation == 0 && otherTopicConversation == 0) {

                                        } else if (myTopicConversation == 0 && otherTopicConversation > 0) {
                                            otherIgnoredTime++;
                                        } else if (myTopicConversation > 0 && otherTopicConversation == 0) {
                                            myIgnoredTime++;
                                        }
                                        myTopicConversation = 0;
                                        otherTopicConversation = 0;
                                        myTopicConversation++;
                                            /*if (myFrequentConversationTime == 1) {
                                                //myIgnoredTime++;
                                            }*/
                                    } else if (myReply > 5) {
                                        myOptimizedConversationTime++;
                                        myTopicConversation++;//小于五秒的话多条合并，因为要算话题被忽略次数，因此这块无所谓，只要加上就行
                                    }
                                } else {
                                    firstConversationDate = fullTime;
                                }
                            }
                            otherChatTimeStamp.clear();
                            myChatTimeStamp.add(fullMillisecond);

                               /* myFrequentConversationTime++;
                                otherFrequentConversationTime = 0;*/

                        } else {
                            isMe = false;
                            Long otherReply;
                            if (myChatTimeStamp.size() != 0) {
                                otherReply = (fullMillisecond - myChatTimeStamp.get(0)) / 1000;
                                otherReplyTime.add(otherReply);
                                otherOptimizedConversationTime++;
                                if (isLately) {
                                    otherLatelyReplyTime.add(otherReply);
                                }
                                if (otherReply <= AlgorithmConstants.Limit) {
                                    //System.out.println(otherReply);
                                    otherInteractionTime.add(otherReply);
                                    if (isLately) {
                                        otherLatelyInteractionTime.add(otherReply);
                                    }
                                    myValidConversationtime++;
                                    otherTopicConversation++;
                                } else {
                                       /* if(myFrequentConversationTime == 1){
                                            //myIgnoredTime++;
                                        }*/
                                    if (myTopicConversation == 0 && otherTopicConversation == 0) {

                                    } else if (myTopicConversation == 0 && otherTopicConversation > 0) {
                                        otherIgnoredTime++;
                                    } else if (myTopicConversation > 0 && otherTopicConversation == 0) {
                                        myIgnoredTime++;
                                    }
                                    myTopicConversation = 0;
                                    otherTopicConversation = 0;
                                    otherTopicConversation++;
                                    myEndConversationTime++;
                                }
                            } else {
                                if (otherChatTimeStamp.size() != 0) {
                                    otherReply = (fullMillisecond - otherChatTimeStamp.get(otherChatTimeStamp.size() - 1)) / 1000;
                                    if (otherReply > AlgorithmConstants.Limit) {
                                            /*if(otherFrequentConversationTime == 1){
                                                //otherIgnoredTime++;
                                            }*/
                                        if (myTopicConversation == 0 && otherTopicConversation == 0) {

                                        } else if (myTopicConversation == 0 && otherTopicConversation > 0) {
                                            otherIgnoredTime++;
                                        } else if (myTopicConversation > 0 && otherTopicConversation == 0) {
                                            myIgnoredTime++;
                                        }
                                        myTopicConversation = 0;
                                        otherTopicConversation = 0;
                                        otherTopicConversation++;
                                        otherEndConversationTime++;
                                    } else if (otherReply > 5) {
                                        otherTopicConversation++;
                                        otherOptimizedConversationTime++;
                                    }
                                } else {
                                    firstConversationDate = fullTime;
                                }
                            }
                            myChatTimeStamp.clear();
                            otherChatTimeStamp.add(fullMillisecond);

                               /* otherFrequentConversationTime++;
                                myFrequentConversationTime = 0;*/
                        }
                    }
                } else {//聊天正文内容
                    if (isMe) {
                        analyzeContentByWords(line, true);
                    } else {
                        analyzeContentByWords(line, false);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        printReplyTime();
        printInteractiontime();


        //showMyReplyTime();
        //showOtherReplyTime();
    }

    public void printInteractiontime() {
      /*  System.out.println("自己交互时间记录↓");
        for(int i = 0;i<myInteractionTime.size();i++){
            System.out.println(myInteractionTime.get(i));
        }

        System.out.println("对方交互时间记录↓");
        for(int i = 0;i<otherInteractionTime.size();i++){
            System.out.println(otherInteractionTime.get(i));
        }
*/
        ArrayList<Long> myInteractionArrayList = getLongestAndShortestReplyTime(myInteractionTime);
        ArrayList<Long> otherInteractionArrayList = getLongestAndShortestReplyTime(otherInteractionTime);

        System.out.println("##########我的交互时间记录   START###########");
        Long myShortestInteractionTime = myInteractionArrayList.get(0);
        Long myLongestInteractionTime = myInteractionArrayList.get(myInteractionArrayList.size() - 1);

        System.out.println("我得到交互的次数:" + myValidConversationtime);
        System.out.println("我的优化之后的语句数目:" + myOptimizedConversationTime);

        System.out.println("我的最短交互时间段内回复时间为:" + Utils.getTime(myShortestInteractionTime));
        System.out.println("我的最长交互时间段内回复时间为:" + Utils.getTime(myLongestInteractionTime));
        System.out.println("我发起的话题率:" + decimalFormat.format(100 * (Double.parseDouble(String.valueOf(myInteractionTime.size()))) / (myInteractionTime.size() + otherInteractionTime.size())) + "%");
        System.out.println("我最终结束话题率:" + decimalFormat.format(100 * (Double.parseDouble(String.valueOf(myEndConversationTime)) / (myEndConversationTime + otherEndConversationTime))) + "%");
        System.out.println("我的有效交互率:" + decimalFormat.format(100 * (Double.parseDouble(String.valueOf(myValidConversationtime)) / myConversationTime)) + "%");
        System.out.println("优化后的我的有效交互率:" + decimalFormat.format(100 * (Double.parseDouble(String.valueOf(myValidConversationtime)) / myOptimizedConversationTime)) + "%");
        System.out.println("##########我的交互时间记录   END##############");

        System.out.println();

        System.out.println("##########对方的交互时间记录   START###########");
        Long otherShortestInteractionTime = otherInteractionArrayList.get(0);
        Long otherLongestInteractionTime = otherInteractionArrayList.get(otherInteractionArrayList.size() - 1);

        System.out.println("对方得到交互的次数:" + otherValidConversationTime);
        System.out.println("对方的优化之后的语句数目:" + otherOptimizedConversationTime);

        System.out.println("对方的最短交互时间段内回复时间为:" + Utils.getTime(otherShortestInteractionTime));
        System.out.println("对方的最长交互时间段内回复时间为:" + Utils.getTime(otherLongestInteractionTime));
        System.out.println("对方发起的话题率:" + decimalFormat.format(100 * (Double.parseDouble(String.valueOf(otherInteractionTime.size()))) / (myInteractionTime.size() + otherInteractionTime.size())) + "%");
        System.out.println("对方最终结束话题率:" + decimalFormat.format(100 * (Double.parseDouble(String.valueOf(otherEndConversationTime)) / (myEndConversationTime + otherEndConversationTime))) + "%");
        System.out.println("对方的有效交互率:" + decimalFormat.format(100 * (Double.parseDouble(String.valueOf(otherValidConversationTime)) / otherConversationtime)) + "%");
        System.out.println("优化后的对方的有效交互率:" + decimalFormat.format(100 * (Double.parseDouble(String.valueOf(otherValidConversationTime)) / otherOptimizedConversationTime)) + "%");
        System.out.println("##########对方的交互时间记录   END##############");
        System.out.println();
    }

    public void printReplyTime() {
        ArrayList<Long> myLongestAndShortestReplyArraylist = getMyLongestAndShortestReplyTime();
        ArrayList<Long> otherLongestAndShortestReplyArrayList = getOtherLongestAndShortestReplyTime();

        System.out.println("##########我的回复时间记录   START###########");
        Long myShortestReplyTime = myLongestAndShortestReplyArraylist.get(0);
        Long myLongestReplyTime = myLongestAndShortestReplyArraylist.get(myLongestAndShortestReplyArraylist.size() - 1);
        System.out.println("我的最短回复时间为:" + Utils.getTime(myShortestReplyTime));
        System.out.println("我的最长回复时间为:" + Utils.getTime(myLongestReplyTime));
        System.out.println("##########我的回复时间记录   END##############");

        System.out.println();

        System.out.println("##########对方的回复时间记录   START###########");
        Long otherShortestReplyTime = otherLongestAndShortestReplyArrayList.get(0);
        Long otherLongestReplyTime = otherLongestAndShortestReplyArrayList.get(otherLongestAndShortestReplyArrayList.size() - 1);
        System.out.println("对方的最短回复时间为:" + Utils.getTime(otherShortestReplyTime));
        System.out.println("对方的最长回复时间为:" + Utils.getTime(otherLongestReplyTime));
        System.out.println("##########对方的回复时间记录   END##############");
        System.out.println();
    }

    public ArrayList<Long> getLongestAndShortestReplyTime(ArrayList sourceArrayList) {
        ArrayList returnArrayList = new ArrayList();
        Collections.sort(sourceArrayList, new Comparator<Long>() {
            @Override
            public int compare(Long time1, Long time2) {
                //返回正数的时候会交换两个单元的位置，否则位置不变
                if (time1 < time2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        returnArrayList.add(sourceArrayList.get(sourceArrayList.size() - 1));
        returnArrayList.add(sourceArrayList.get(0));
        return returnArrayList;
    }

    public ArrayList<Long> getMyLongestAndShortestReplyTime() {
        ArrayList myLongestAndShortestReplyArraylist = new ArrayList();
        Collections.sort(myReplyTime, new Comparator<Long>() {
            @Override
            public int compare(Long time1, Long time2) {
                //返回正数的时候会交换两个单元的位置，否则位置不变
                if (time1 < time2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        myLongestAndShortestReplyArraylist.add(myReplyTime.get(myReplyTime.size() - 1));
        myLongestAndShortestReplyArraylist.add(myReplyTime.get(0));
        return myLongestAndShortestReplyArraylist;
    }

    public ArrayList<Long> getOtherLongestAndShortestReplyTime() {
        ArrayList otherLongestAndShortestReplyArrayList = new ArrayList();
        Collections.sort(otherReplyTime, new Comparator<Long>() {
            @Override
            public int compare(Long time1, Long time2) {
                //返回正数的时候会交换两个单元的位置，否则位置不变
                if (time1 < time2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        //System.out.println("otherReplyTime.get(0)->" + otherReplyTime.get(0));
        //System.out.println("otherReplyTime.get(otherReplyTime.size() - 1)->" + otherReplyTime.get(otherReplyTime.size() - 1));
        otherLongestAndShortestReplyArrayList.add(otherReplyTime.get(otherReplyTime.size() - 1));
        otherLongestAndShortestReplyArrayList.add(otherReplyTime.get(0));
        return otherLongestAndShortestReplyArrayList;
    }

    public void showMyReplyTime() {
        System.out.println("##########我的回复时间记录   START###########");
        for (Long replyTime : myReplyTime) {
            System.out.println(replyTime);
        }
        System.out.println("##########我的回复时间记录   END##############");
    }

    public void showOtherReplyTime() {
        System.out.println("##########别人的回复时间记录   START###########");
        for (Long replyTime : otherReplyTime) {
            System.out.println(replyTime);
        }
        System.out.println("##########别人的回复时间记录   END##############");
    }

    public String topicAnalyze(ArrayList<Word> words) {
        String topic = "睡觉";//如果无法从之前预定义的词语中找到合适的，那就认为其喜欢睡觉
        int score = 0;
        ArrayList<AlgorithmConstants.Word2> interests = AlgorithmConstants.getInterests();
        for (AlgorithmConstants.Word2 word2 : interests) {
            int tempScore = 0;
            //word2.words;=》ArrayList
            for (int i = 0; i < words.size(); i++) {
                ArrayList word2Words = word2.getWord2s();
                for (int j = 0; j < word2Words.size(); j++) {
                    if (words.get(i).getWord().equals(word2Words.get(j))) {
                        tempScore++;
                        break;
                    }
                }
            }
            if (tempScore > score) {
                score = tempScore;
                topic = word2.getInterest();
            }
        }
        return topic;
    }

    public void emotionAnalyze(File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void generatePicture() {
        MySpriderWebPlotTest mySpriderWebPlotTest = new MySpriderWebPlotTest();
        ArrayList<PeopleAttribute.Attribute> myAttributes = new ArrayList<>();
        myAttributes.add(new PeopleAttribute.Attribute("最近一个月平均交互时间", Utils.getAverage(myLatelyInteractionTime)));
        myAttributes.add(new PeopleAttribute.Attribute("话题发起率", Double.parseDouble(decimalFormat.format(100 * (Double.parseDouble(String.valueOf(myInteractionTime.size()))) / (myInteractionTime.size() + otherInteractionTime.size())))));
        myAttributes.add(new PeopleAttribute.Attribute("有效交互率", Double.parseDouble(decimalFormat.format(100 * (Double.parseDouble(String.valueOf(myValidConversationtime)) / myConversationTime)))));
        myAttributes.add(new PeopleAttribute.Attribute("话题结束率", Double.parseDouble(decimalFormat.format(100 * (Double.parseDouble(String.valueOf(myEndConversationTime)) / (myEndConversationTime + otherEndConversationTime))))));
        myAttributes.add(new PeopleAttribute.Attribute("语句所占比", Double.parseDouble(decimalFormat.format(100 * (Double.parseDouble(String.valueOf(myConversationTime)) / (myConversationTime + otherConversationtime))))));
        myAttributes.add(new PeopleAttribute.Attribute("被忽略次数", myIgnoredTime));

        ArrayList<PeopleAttribute.Attribute> otherAttributes = new ArrayList<>();
        otherAttributes.add(new PeopleAttribute.Attribute("最近一个月平均交互时间", Utils.getAverage(otherLatelyInteractionTime)));
        otherAttributes.add(new PeopleAttribute.Attribute("话题发起率", Double.parseDouble(decimalFormat.format(100 * (Double.parseDouble(String.valueOf(otherInteractionTime.size()))) / (myInteractionTime.size() + otherInteractionTime.size())))));
        otherAttributes.add(new PeopleAttribute.Attribute("有效交互率", Double.parseDouble(decimalFormat.format(100 * (Double.parseDouble(String.valueOf(otherValidConversationTime)) / otherConversationtime)))));
        otherAttributes.add(new PeopleAttribute.Attribute("话题结束率", Double.parseDouble(decimalFormat.format(100 * (Double.parseDouble(String.valueOf(otherEndConversationTime)) / (myEndConversationTime + otherEndConversationTime))))));
        otherAttributes.add(new PeopleAttribute.Attribute("语句所占比", Double.parseDouble(decimalFormat.format(100 * (Double.parseDouble(String.valueOf(otherConversationtime)) / (myConversationTime + otherConversationtime))))));
        otherAttributes.add(new PeopleAttribute.Attribute("被忽略次数", otherIgnoredTime));

        mySpriderWebPlotTest.draw(myAttributes, otherAttributes, myNames.get(0), otherNames.get(0));
    }

    public void setUserNames(ArrayList<String> myNamesInput) {
        for (String myName : myNamesInput) {
            myNames.add(myName);
        }

        for (String userName : userNames) {
            boolean isExist = false;
            for (String myName : myNames) {
                if (userName.equals(myName)) {
                    isExist = true;
                    break;
                }
            }

            if (!isExist) {
                otherNames.add(userName);
            }
        }
    }

    public ArrayList<String> getUserNames(String fileName) {
        this.fileName = fileName;
        File file = new File(CHAT_RECORD_ROOT, fileName);
        //System.out.println(file.getAbsolutePath());
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            filterHeaders(bufferedReader);
            StringTokenizer stringTokenizer = null;
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                if (line.length() == 0) {
                    String nameLine = bufferedReader.readLine();
                    if (nameLine == null) {
                        break;
                    } else if (nameLine.length() == 0) {
                        nameLine = bufferedReader.readLine();
                        if (nameLine == null) {
                            break;
                        } else {
                            if (!nameLine.startsWith("20")) {
                                continue;
                            }
                            String name = nameLine.substring(nameLine.indexOf(" ") + 1);
                            name = name.substring(name.indexOf(" ") + 1);
                            if (nameLine.equals(name)) {
                                continue;
                            }
                            if (!isExist(name)) {
                                userNames.add(name);
                            }
                        }
                    } else {
                        if (!nameLine.startsWith("20")) {
                            continue;
                        }
                        String name = nameLine.substring(nameLine.indexOf(" ") + 1);
                        name = name.substring(name.indexOf(" ") + 1);
                        if (nameLine.equals(name)) {
                            continue;
                        }
                        if (!isExist(name)) {
                            userNames.add(name);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userNames;
    }

    private void filterHeaders(BufferedReader bufferedReader) {
        for (int i = 0; i < 7; i++) {
            try {
                bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isExist(String name) {
        boolean isExist = false;
        if (name.contains("系统消息(10000)")) {
            return true;
        }
        if (name.equals(" ")) {
            return true;
        }
        if (name.equals("")) {
            return true;
        }
        for (String userName : userNames) {
            if (userName.equals(name)) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    private void getUserNames() {
        for (String userName : userNames) {
            System.out.println(userName);
        }
    }

    public void showMyNames() {
        for (String myName : myNames) {
            System.out.println(myName);
        }
    }

    public void showOtherNames() {
        for (String otherName : otherNames) {
            System.out.println(otherName);
        }
    }

    private boolean isMyName(String name) {
        for (String myName : myNames) {
            if (name.equals(myName)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Word> getMyFrequentWords(int count) {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        Collections.sort(myWords, new Comparator<Word>() {
            @Override
            public int compare(Word word1, Word word2) {
                //返回正数的时候会交换两个单元的位置，否则位置不变
                if (word1.getTime() < word2.getTime()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        ArrayList<Word> frequentWords = new ArrayList<Word>();
        for (int i = 0; i < count; i++) {
            frequentWords.add(myWords.get(i));
        }
        return frequentWords;
    }

    public ArrayList<Word> getOtherFrequentWords(int count) {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        Collections.sort(otherWords, new Comparator<Word>() {
            @Override
            public int compare(Word word1, Word word2) {
                //返回正数的时候会交换两个单元的位置，否则位置不变
                if (word1.getTime() < word2.getTime()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        ArrayList<Word> frequentWords = new ArrayList<Word>();
        for (int i = 0; i < count; i++) {
            frequentWords.add(otherWords.get(i));
        }
        return frequentWords;
    }

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
