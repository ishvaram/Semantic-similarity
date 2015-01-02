package com.serendio.textanalytics;

//import opennlp.tools.formats.ad.ADSentenceStream;

public class SimilarityUpdate
{
    public static void main(String[] args)
    {
      /* String str1 = "ARTUSI , CHARLES E. HARRIS , MARSHALL L. MOHR , CHRISTINE KING , QUALCOMM INCORPORATED , and T MERGER SUB , INC. , Defendants .";
        String str2 = "Meng was President and CEO of the Company from May 1998 to October 1999 .";
        SentenceSimilarity sentSim = new SentenceSimilarity();
        SimilarParams param = sentSim.findSentenceSimilarity(str1,str2);
        System.out.println(param.stemMatchScore+"  "+param.similarity);*/
       // args= new String[2];
        //args[0] = "51";
      //  args[1] = "0";
        if(args.length != 2)
        {
            System.out.println("Incorrect Parameters");
            System.out.println("Sample execution: java -jar SimilarityUpdate.jar caseno mode");
            System.out.println("mode - 1 (Production) mode - 0 (test)");
        }
        else
        {
            DatabaseUpdater update = new DatabaseUpdater();
            Integer modeNo = Integer.parseInt(args[1]);
            if(modeNo == 1)
                update.updateSimilarityForCase(Integer.parseInt(args[0]));
            else if(modeNo == 0)
                update.testSimilarityForCase(Integer.parseInt(args[0]));
        }
    }
}
