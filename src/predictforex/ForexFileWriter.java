/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package predictforex;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Melvin
 */
public class ForexFileWriter {
    
    public static String [] findMinMax (String[][] ForexPrice, int column)
    {
        String[] minmax = new String[2];
        String min = ForexPrice[0][column];
        String max = ForexPrice[0][column];
        for(int i=0;ForexPrice[i][0]!=null;i++){
               if(Double.parseDouble(min)>Double.parseDouble(ForexPrice[i][column])){
                   min = ForexPrice[i][column];
               } 
               if(Double.parseDouble(max)<Double.parseDouble(ForexPrice[i][column])){
                   max = ForexPrice[i][column];
               } 
        }
       minmax[0] = min;
       minmax[1] = max;
       return minmax;
    }
    
    public static String FeatureScaling(String price,String[] minmax)
    {
        Double normalizedPrice;
        
        //get the normalized price using : (price - min) / (max - min)
        normalizedPrice = (Double.parseDouble(price) - Double.parseDouble(minmax[0])) / (Double.parseDouble(minmax[1]) - Double.parseDouble(minmax [0]));
        
        return String.valueOf(normalizedPrice);
    }
    
    public static String[][] rawForexPriceNormalization (String[][] ForexPrice)
    {
        String [][]normalizedForexPrice = new String [ForexPrice.length][ForexPrice[0].length];
        String []openMinMax = findMinMax(ForexPrice, 2);
        String []highMinMax = findMinMax(ForexPrice, 3);
        String []lowMinMax = findMinMax(ForexPrice, 4);
        String []closeMinMax = findMinMax(ForexPrice, 5);
        String []volumeMinMax = findMinMax(ForexPrice, 6);
        
        for(int i=0;ForexPrice[i][0]!=null;i++){
                normalizedForexPrice[i][0] = ForexPrice [i][0];
                normalizedForexPrice[i][1] = ForexPrice [i][1];
                normalizedForexPrice[i][2] = FeatureScaling(ForexPrice [i][2], openMinMax);
                normalizedForexPrice[i][3] = FeatureScaling(ForexPrice [i][3], highMinMax);
                normalizedForexPrice[i][4] = FeatureScaling(ForexPrice [i][4], lowMinMax);
                normalizedForexPrice[i][5] = FeatureScaling(ForexPrice [i][5], closeMinMax);
                normalizedForexPrice[i][6] = FeatureScaling(ForexPrice [i][6], volumeMinMax);
        }
        
        return normalizedForexPrice;
    }
    
    public static String[][] MACDPriceNormalization (String[][] MACDPrice)
    {
        String [][]normalizedMACDPrice = new String [MACDPrice.length][MACDPrice[0].length];
        String []MACDMinMax = findMinMax(MACDPrice, 2);
        
        for(int i=0;MACDPrice[i][0]!=null;i++){
                normalizedMACDPrice[i][0] = MACDPrice [i][0];
                normalizedMACDPrice[i][1] = MACDPrice [i][1];
                normalizedMACDPrice[i][2] = FeatureScaling(MACDPrice [i][2], MACDMinMax);
                normalizedMACDPrice[i][1] = MACDPrice [i][3];
        }
        
        return normalizedMACDPrice;
    }
    
    //to save matriks from raw forex price to arff format
    public static void rawForexPriceToArff (String[][] ForexPrice) throws IOException{
        FileWriter fw = new FileWriter("forexPrice.arff");
        PrintWriter pw = new PrintWriter(fw);
        FileWriter fw2 = new FileWriter("unlabeledForexPrice.arff");
        PrintWriter pw2 = new PrintWriter(fw2);
        int counter=0;
        
        pw.println("@RELATION forexPrice");
        pw.println("");
        pw.println("@ATTRIBUTE open real");
        pw.println("@ATTRIBUTE high real");
        pw.println("@ATTRIBUTE low real");
        pw.println("@ATTRIBUTE close real");
        pw.println("@ATTRIBUTE close2 real");
        pw.println("@data");
        
        pw.println("@RELATION unlabeledForexPrice");
        pw.println("");
        pw.println("@ATTRIBUTE open real");
        pw.println("@ATTRIBUTE high real");
        pw.println("@ATTRIBUTE low real");
        pw.println("@ATTRIBUTE close real");
        pw.println("@ATTRIBUTE close2 real");
        pw.println("@data");
        
        //count for row to divide between training set and test set
        for(int i=0;ForexPrice[i][0]!=null;i++){
            counter++;
        }
        
        //write price to ARFF open, high, low, close, close2
        for(int i=1;(i<=counter*0.8);i++)
        {
                pw.println(ForexPrice[i-1][2]+","+ForexPrice[i-1][3]+","+ForexPrice[i-1][4]+","+ForexPrice[i-1][5]+","+ForexPrice[i][5]);
        }
        
        for(int j=(int) Math.ceil(counter*0.8);ForexPrice[j][0]!=null;j++)
        {
            pw2.println(ForexPrice[j-1][2]+","+ForexPrice[j-1][3]+","+ForexPrice[j-1][4]+","+ForexPrice[j-1][5]+",?");
        }
        
        //Flush the output to the file
        pw.flush();
        pw2.flush();

        //Close the Print Writer
        pw.close();
        pw2.close();

        //Close the File Writer
        fw.close();
        fw2.close();
    }
    
        //to save matriks to arff format
    public static void normalizedForexPriceToArff (String[][] ForexPrice) throws IOException{
        FileWriter fw = new FileWriter("normalizedForexPrice.arff");
        PrintWriter pw = new PrintWriter(fw);
        FileWriter fw2 = new FileWriter("unlabeledNormalizedForexPrice.arff");
        PrintWriter pw2 = new PrintWriter(fw2);
        int counter=0;
        int t=0;
        String [][]normalizedForexPrice = new String [ForexPrice.length][ForexPrice[0].length];
        String [][]unlabeledNormalizedForexPrice = new String [ForexPrice.length][ForexPrice[0].length];
        String [][]ForexPriceTrainingSet = new String [ForexPrice.length][ForexPrice[0].length];
        String [][]unlabeledForexPriceTrainingSet = new String [ForexPrice.length][ForexPrice[0].length];
        pw.println("@RELATION normalizedForexPrice");
        pw.println("");
        pw.println("@ATTRIBUTE open real");
        pw.println("@ATTRIBUTE high real");
        pw.println("@ATTRIBUTE low real");
        pw.println("@ATTRIBUTE close real");
        pw.println("@ATTRIBUTE close2 real");
        pw.println("@data");
        
        pw2.println("@RELATION unlabeledNormalizedForexPrice");
        pw2.println("");
        pw2.println("@ATTRIBUTE open real");
        pw2.println("@ATTRIBUTE high real");
        pw2.println("@ATTRIBUTE low real");
        pw2.println("@ATTRIBUTE close real");
        pw2.println("@ATTRIBUTE close2 real");
        pw2.println("@data");
        
        //count for row to divide between training set and test set
        for(int i=0;ForexPrice[i][0]!=null;i++){
            counter++;
        }
        
        //differentiate the training set
        for(int i=0;i<counter*0.8;i++)
        {
            for(int j=0;j<ForexPrice[0].length;j++)
            {
                ForexPriceTrainingSet[i][j] = ForexPrice[i][j]; 
            }
        }
        
        
        for(int i=((int) Math.ceil(counter*0.8))-1;ForexPrice[i][0]!=null;i++)
        {
            for(int j=0;j<ForexPrice[0].length;j++)
            {
                unlabeledForexPriceTrainingSet[t][j] = ForexPrice[i][j]; 
            }
            t++;
        }
        
        //write price to ARFF
        normalizedForexPrice = rawForexPriceNormalization(ForexPriceTrainingSet);
        //write price to ARFF open, high, low, close, close2
        for(int i=1;(i<=counter*0.8);i++)
        {
            pw.println(normalizedForexPrice[i-1][2]+","+normalizedForexPrice[i-1][3]+","+normalizedForexPrice[i-1][4]+","+normalizedForexPrice[i-1][5]+","+normalizedForexPrice[i][5]);
        }
        
        unlabeledNormalizedForexPrice = rawForexPriceNormalization(unlabeledForexPriceTrainingSet);
        for(int i=1;(i<t);i++)
        {
            pw2.println(unlabeledNormalizedForexPrice[i-1][2]+","+unlabeledNormalizedForexPrice[i-1][3]+","+unlabeledNormalizedForexPrice[i-1][4]+","+unlabeledNormalizedForexPrice[i-1][5]+",?");
        }
        
        //Flush the output to the file
        pw.flush();
        pw2.flush();

        //Close the Print Writer
        pw.close();
        pw2.close();

        //Close the File Writer
        fw.close();
        fw2.close();
    }
    
    public static void MACDPriceToCSV(String[][] MACDPrice) throws IOException
    {
        FileWriter fw = new FileWriter("MACDPrice.csv");
        PrintWriter pw = new PrintWriter(fw);
        
        for(int i=0;MACDPrice[i][0]!=null;i++){
            for(int j=0;j<MACDPrice[0].length;j++){ 
                pw.print(MACDPrice[i][j]);
                if(j!=MACDPrice[0].length-1)
                    pw.print(",");
            }
            pw.println("");
        }        
        
        //Flush the output to the file
        pw.flush();

        //Close the Print Writer
        pw.close();

        //Close the File Writer
        fw.close();
    }
    
    public static void MACDToArff (String[][] MACDPrice) throws IOException{
        FileWriter fw = new FileWriter("MACDRecommendation.arff");
        PrintWriter pw = new PrintWriter(fw);
        FileWriter fw2 = new FileWriter("unlabeledMACDRecommendation.arff");
        PrintWriter pw2 = new PrintWriter(fw2);
        int counter=0;
        
        pw.println("@RELATION MACDRecommendation");
        pw.println("");
        pw.println("@ATTRIBUTE histogram1 real");
        pw.println("@ATTRIBUTE histogram2 real");
        pw.println("@ATTRIBUTE recommendation {buy,sell,stall}");
        pw.println("@data");
        
        pw2.println("@RELATION unlabeledMACDRecommendation");
        pw2.println("");
        pw2.println("@ATTRIBUTE histogram1 real");
        pw2.println("@ATTRIBUTE histogram2 real");
        pw2.println("@ATTRIBUTE recommendation {buy,sell,stall}");
        pw2.println("@data");
        
        //count for row to divide between training set and test set
        for(int i=0;MACDPrice[i][0]!=null;i++){
            counter++;
        }
        
        //write price to ARFF
        for(int i=1;(i<=counter*0.8);i++)
        {
            pw.println(MACDPrice[i-1][2]+","+MACDPrice[i][2]+","+MACDPrice[i][3]);
        }
        
        for(int j=(int) Math.ceil(counter*0.8);MACDPrice[j][0]!=null;j++)
        {
            pw2.println(MACDPrice[j-1][2]+","+MACDPrice[j][2]+",?");
        }
        
        //Flush the output to the file
        pw.flush();
        pw2.flush();

        //Close the Print Writer
        pw.close();
        pw2.close();

        //Close the File Writer
        fw.close();
        fw2.close();
    }
    
    public static void NormalizedMACDToArff (String[][] MACDPrice) throws IOException{
        FileWriter fw = new FileWriter("normalizedMACDRecommendation.arff");
        PrintWriter pw = new PrintWriter(fw);
        FileWriter fw2 = new FileWriter("unlabeledNormalizedMACDRecommendation.arff");
        PrintWriter pw2 = new PrintWriter(fw2);
        int counter=0;
        int t=0;
        String [][]normalizedMACDPrice = new String [MACDPrice.length][MACDPrice[0].length];
        String [][]MACDTrainingSet = new String [MACDPrice.length][MACDPrice[0].length];
        String [][]unlabeledNormalizedMACDPrice = new String [MACDPrice.length][MACDPrice[0].length];
        String [][]unlabeledMACDTrainingSet = new String [MACDPrice.length][MACDPrice[0].length];
        
        pw.println("@RELATION MACDRecommendation");
        pw.println("");
        pw.println("@ATTRIBUTE histogram1 real");
        pw.println("@ATTRIBUTE histogram2 real");
        pw.println("@ATTRIBUTE recommendation {buy,sell,stall}");
        pw.println("@data");
        
        pw2.println("@RELATION unlabeledMACDRecommendation");
        pw2.println("");
        pw2.println("@ATTRIBUTE histogram1 real");
        pw2.println("@ATTRIBUTE histogram2 real");
        pw2.println("@ATTRIBUTE recommendation {buy,sell,stall}");
        pw2.println("@data");
       
        
        //count for row to divide between training set and test set
        for(int i=0;MACDPrice[i][0]!=null;i++){
            counter++;
        }
        
        //differentiate the training set
        for(int i=0;i<counter*0.8;i++)
        {
            for(int j=0;j<MACDPrice[0].length;j++)
            {
                MACDTrainingSet[i][j] = MACDPrice[i][j]; 
            }
        }
        
        for(int i=((int) Math.ceil(counter*0.8))-1;MACDPrice[i][0]!=null;i++)
        {
            for(int j=0;j<MACDPrice[0].length;j++)
            {
                unlabeledMACDTrainingSet[t][j] = MACDPrice[i][j]; 
            }
            t++;
        }
        
        
        //write price to ARFF
        normalizedMACDPrice = MACDPriceNormalization(MACDTrainingSet);
        for(int i=1;(i<=counter*0.8);i++)
        {
            pw.println(normalizedMACDPrice[i-1][2]+","+normalizedMACDPrice[i][2]+","+normalizedMACDPrice[i][3]);
        }
        
        unlabeledNormalizedMACDPrice = MACDPriceNormalization(unlabeledMACDTrainingSet);
        for(int i=1;(i<t);i++)
        {
            pw2.println(unlabeledNormalizedMACDPrice[i-1][2]+","+unlabeledNormalizedMACDPrice[i][2]+",?");
        }
        
        //Flush the output to the file
        pw.flush();
        pw2.flush();

        //Close the Print Writer
        pw.close();
        pw2.close();

        //Close the File Writer
        fw.close();
        fw2.close();
    }
    
}
