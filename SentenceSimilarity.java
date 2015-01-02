package com.serendio.textanalytics;

import edu.cmu.lti.ws4j.RelatednessCalculator;
import java.io.*;
import java.util.*;
import java.lang.*;

class SimilarParams
{
    double similarity;
    double nmatchingStems;
    double stemMatchScore;
    ArrayList<String> verbs1;
    ArrayList<String> verbs2;
    SimilarParams()
    {
        verbs1 = new ArrayList<String>();
        verbs2 = new ArrayList<String>();
    }
}
public class SentenceSimilarity
{
    InputStream modelIn = null;
    RelatednessCalculator rcs = null;
    static Classifier4jInterface simObj = new Classifier4jInterface();

    SentenceSimilarity()
    {

    }
    void loadPOSModel()
    {
        try {
            modelIn = new FileInputStream("en-pos-maxent.bin");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    double getDistinctMatchCount(String[] words1,String[] words2)
    {
        double score = 0;
        int min_length = Math.min(words1.length,words2.length);
        String temp1;
        String temp2;
        for (int i= 0; i< min_length;i++)
        {
            temp1 = words1[i].toLowerCase().trim();
            temp2 = words2[i].toLowerCase().trim();
            if(temp1.compareToIgnoreCase(temp2) == 0)
                score = score + 1;
        }
        return score;
    }
    double getSimilarityScore1(double[][] simMatrix, String[] tag1,String[] tag2)
    {
        double score = 0;
        int no = 0;
        for(int i=0;i < simMatrix.length;i++)
        {
            for(int j=0;j < simMatrix[i].length;j++)
            {
                if((tag1.length == tag2.length) && (i==j) )
                {
                    score = score + simMatrix[i][j];
                    no = no + 1;
                }
                if((tag1.length != tag2.length)&&(simMatrix[i][j] > 0) /*&& (((tag1[i].compareToIgnoreCase("VB")==0) && (tag2[j].compareToIgnoreCase("VB")==0)) || ((tag1[i].compareToIgnoreCase("NN") == 0)&&(tag2[j].compareToIgnoreCase("NN") == 0)) )*/)
                {

                    score = score + simMatrix[i][j];
                    no = no + 1;
                }
            }

        }

        if(no>1)
            score = score/no;
        return score;
    }

    void populateFuzzyGroup(Map<Integer,String> docSentences)
    {
        int nSize = docSentences.size();
        SimilarParams similarityScore = new SimilarParams();
        DatabaseManager dman = new DatabaseManager();
        Integer[] sentenceKeys = Arrays.copyOf(docSentences.keySet().toArray(),nSize,Integer[].class);
        double totalSentences = (sentenceKeys.length * sentenceKeys.length)/2 - sentenceKeys.length;
        double counter = 0;

        for( int i = 1; i < sentenceKeys.length; i++)
        {
            for( int j = 0; j < i; j++)
            {
                if(i!=j)
                {
                    counter = counter + 1;
                    similarityScore.similarity = simObj.getSimilarity(docSentences.get(sentenceKeys[i]), docSentences.get(sentenceKeys[j]));
                    if(similarityScore.similarity > 0.3)
                        dman.insertFuzzySets1(String.valueOf(sentenceKeys[i]),String.valueOf(sentenceKeys[j]),String.valueOf(similarityScore.similarity));
                }
            }
            System.out.println(i+"/"+sentenceKeys.length +" sentences processed ; "+"Percent Completion : "+(counter/totalSentences)*100);
        }
        dman.closeConnection();
    }
    void evaluateSentenceSimilarities(Map<Integer,String> docSentences)
    {
        int nSize = docSentences.size();
        SimilarParams similarityScore = new SimilarParams();
        Integer[] sentenceKeys = Arrays.copyOf(docSentences.keySet().toArray(),nSize,Integer[].class);
        double totalSentences = (sentenceKeys.length * sentenceKeys.length)/2 - sentenceKeys.length;
        double counter = 0;
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter("sent_list.txt"));
            BufferedWriter bw1 = new BufferedWriter(new FileWriter("sent_comp.txt"));
            for(int i = 0; i < sentenceKeys.length;i++)
            {
                bw.write(i+"|||"+docSentences.get(sentenceKeys[i])+"\n");
            }
            bw.close();
            for( int i = 1; i < sentenceKeys.length; i++)
            {
                for( int j = 0; j < i; j++)
                {
                    if(i!=j)
                    {
                        counter = counter + 1;
                        similarityScore.similarity = simObj.getSimilarity(docSentences.get(sentenceKeys[i]), docSentences.get(sentenceKeys[j]));
                        if(similarityScore.similarity < 0.5)
                            continue;

                        bw1.write("Sent 1: " + i + " Sent 2: " + j + " similarity score : " + similarityScore.similarity + "\n");


                    }
                }
                System.out.println(i+"/"+sentenceKeys.length +" sentences processed ; "+"Percent Completion : "+(counter/totalSentences)*100);
            }
            bw1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
