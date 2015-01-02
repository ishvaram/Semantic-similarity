package com.serendio.textanalytics;

import com.sun.org.apache.bcel.internal.generic.BREAKPOINT;

import java.util.*;
import java.lang.*;
import java.io.*;


/**
 * Created by serendio on 12/19/14.
 */
public class Classifier4jInterface
{
    static Compare c = null;

    public double  getSimilarity(String str1,String str2)
    {
        double score = 0;
        if (filterSentences(str1,str2)==true)
        {
            double normfactor = ((double) (str1.length() + str2.length())/(2 * Math.max(str1.length(),str2.length())));
            c = new Compare(str1, str2);
            score = c.getResult()*normfactor;
        }
        return score;
    }
    public boolean filterSentences(String sent1,String sent2)
    {
        sent1 = sent1.replace(",", " ").trim();
        sent1 = sent1.replace("'", " ").trim();
        sent1 = sent1.replace("/", " ").trim();
        sent1 = sent1.replace("`", " ").trim();
        sent2 = sent2.replace(",", " ").trim();
        sent2 = sent2.replace("'", " ").trim();
        sent2 = sent2.replace("/", " ").trim();
        sent2 = sent2.replace("`", " ").trim();

        String[] word1=sent1.split(" ");
        String[] word2=sent2.split(" ");

        if((word1.length < 5) || (word2.length < 5))
            return false;
        else
            return true;
    }
   public static void main(String[] args)
   {
        Classifier4jInterface obj= new Classifier4jInterface();
   }

}
