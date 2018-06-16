package com.EugeneStudio.ChatAnalyzer.algorithm;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class AlgorithmConstants {
    private AlgorithmConstants() {
    }

    public static final String CHAT_RECORD_ROOT = "chatRecord";
    public static final String WORDS_ROOT = "comparisonSource/Words.txt";
    public static ArrayList<String> WORDS = new ArrayList<String>();
    public static Long Limit = 1 * 60 * 60L;//1小时

    static {
        File file = new File(WORDS_ROOT);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                while (stringTokenizer.hasMoreTokens()) {
                    WORDS.add(stringTokenizer.nextToken());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isWordsContainWord(String word) {
        for (String existWord : WORDS) {
            if (word.equals(existWord)) {
                return true;
            }
        }
        return false;
    }

    public static void setLimit(Long limit) {
        Limit = limit;
    }

    private static void testReadContent() {
        File file = new File(WORDS_ROOT);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                while (stringTokenizer.hasMoreTokens()) {
                    WORDS.add(stringTokenizer.nextToken());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showAllWords() {
        for (String word : WORDS) {
            System.out.println(word);
        }
        System.out.println(WORDS.size());
    }

    public static void main(String[] args) {
        testReadContent();
        showAllWords();
    }

    static class Word2 {
        private String interest;
        private ArrayList<String> words = new ArrayList<>();

        public Word2() {
        }

        public Word2(String interest, ArrayList words) {
            this.words = words;
            this.interest = interest;
        }

        public void setWord2s(ArrayList<String> words) {
            this.words = words;
        }

        public ArrayList<String> getWord2s() {
            return words;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public String getInterest() {
            return interest;
        }
    }

    public static ArrayList getInterests() {
        ArrayList<Word2> interests = new ArrayList<>();
        try {
            File file = new File("comparisonSource/interests.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            String interestName = "";
            String words;
            int i = 1;
            while ((line = bufferedReader.readLine()) != null) {
                if (i == 1) {
                    interestName = line;
                    i++;
                } else if (i == 2) {
                    words = line;
                    i = 1;
                    //System.out.println(line);
                    ArrayList temp = new ArrayList();
                    StringTokenizer stringTokenizer = new StringTokenizer(words, " ");
                    while (stringTokenizer.hasMoreTokens()) {
                        String tempWord2 = stringTokenizer.nextToken();
                        if (AlgorithmConstants.isWordsContainWord(tempWord2)) {
                            temp.add(tempWord2);
                        } else {
                            System.out.println(tempWord2);
                        }
                    }
                    interests.add(new Word2(interestName, temp));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return interests;
    }

}
