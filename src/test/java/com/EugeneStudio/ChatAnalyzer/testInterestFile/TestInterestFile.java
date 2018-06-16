package com.EugeneStudio.ChatAnalyzer.testInterestFile;

import com.EugeneStudio.ChatAnalyzer.algorithm.AlgorithmConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class TestInterestFile {
    static class Word2 {
        String interest;
        ArrayList<String> words = new ArrayList<>();

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

    public static void main(String[] args) {
        try {
            ArrayList<Word2> interests = new ArrayList<>();
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
                    //System.out.println(temp.size());
                    interests.add(new Word2(interestName, temp));
                }
            }
            for (Word2 word : interests) {
                System.out.println(word.getInterest() + " " + word.getWord2s().size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
