/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package predictforex;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;


/**
 *
 * @author Melvin
 */
public class MACD {
    public String[][] MACDLine = new String[ForexFileReader.row][3]; 
    public String[][] SignalLine = new String[ForexFileReader.row][3]; 
    public String[][] Histogram = new String[ForexFileReader.row][3];
    public String[][] Recommendation = new String[ForexFileReader.row][4];
    String polarity="neutral";
    
    public double SMA (double totalPrice, int periods){    
        return (totalPrice) / (periods);  
    }
    
    public double Multiplier (int periods){       
        return 2/((double)periods + 1);  
    }
    
    public double EMA (double close, double EMAprevDay, int periods){
        return (close - EMAprevDay) * Multiplier(periods) + EMAprevDay;
    }
    
//    public double MACDPoint (double close12, double EMAprevDay12,double close26, double EMAprevDay26){
//         return EMA(close12,EMAprevDay12,12) - EMA(close26,EMAprevDay26,26);
//    }
    
    public double MACDPoint (double EMA12, double EMA26){
         return EMA12 - EMA26;
    }
    
    public double SignalPoint (double EMA9){
         return EMA9;
    }
    
    
    
    public void MACDAnalysis (String[][] rawForexPrice) throws IOException{
            //for storing SMA value
             String SMA9;
             String SMA12;
             String SMA26; 
             
             //for storing EMA value
             String[][] EMA9 = new String[ForexFileReader.row][3];
             String[][] EMA12 = new String[ForexFileReader.row][3];
             String[][] EMA26 = new String[ForexFileReader.row][3];
             
             double totalPrice9;
             double totalPrice12;
             double totalPrice26;
            
             
             //calculate SMA for period 12
            totalPrice12= 0;
            for (int i=0 ; i<12 ; i++)
            { 
                totalPrice12 += Double.parseDouble(rawForexPrice[i][5]);
            }
            SMA12 =  String.valueOf(SMA(totalPrice12,12));
             //System.out.println("SMA12 : " + SMA12);
             
             //calculate SMA for period 26
             totalPrice26= 0;
            for (int i=0 ; i<26 ; i++)
            { 
                totalPrice26 += Double.parseDouble(rawForexPrice[i][5]);
            }
            SMA26 =  String.valueOf(SMA(totalPrice26,26));
             
             //ForexFileReader.printMatrix(SMA26);
            
             //calculate EMA for period 12
             EMA12[0][0] = rawForexPrice [11][0];
             EMA12[0][1] = rawForexPrice [11][1];
             EMA12[0][2] = SMA12;
             int j=1;
             for (int i=12 ; rawForexPrice[i][0]!=null ; i++)
             {
                    //copy date
                    EMA12[j][0] = rawForexPrice [i][0]; 
                    //copy time
                    EMA12[j][1] = rawForexPrice [i][1]; 
                    //calculate EMA12
                    EMA12[j][2] = String.valueOf(EMA(Double.parseDouble(rawForexPrice[i][5]),Double.parseDouble(EMA12[j-1][2]),12));
                    j++;
            }
             
            //ForexFileReader.printMatrix(EMA12);
             
             //calculate EMA for period 26
             EMA26[0][0] = rawForexPrice [25][0];
             EMA26[0][1] = rawForexPrice [25][1];
             EMA26[0][2] = SMA26;
             j=1;
             for (int i=26 ; rawForexPrice[i][0]!=null ; i++)
             {
                    //copy date
                    EMA26[j][0] = rawForexPrice [i][0]; 
                    //copy time
                    EMA26[j][1] = rawForexPrice [i][1]; 
                    //calculate EMA12
                    EMA26[j][2] = String.valueOf(EMA(Double.parseDouble(rawForexPrice[i][5]),Double.parseDouble(EMA26[j-1][2]),26));
                    j++;
             }
            //ForexFileReader.printMatrix(EMA26);
            
             //calculate MACDLine
             for (int i=0 ; EMA26[i][0]!=null ; i++)
             {
                    //copy date
                    MACDLine[i][0] = EMA26 [i][0]; 
                    //copy time
                    MACDLine[i][1] = EMA26 [i][1]; 
                    //calculate SMA9
                    MACDLine[i][2] = String.valueOf(MACDPoint(Double.parseDouble(EMA12[i+14][2]),Double.parseDouble(EMA26[i][2])));
             }
             //ForexFileReader.printMatrix(MACDLine);
              
             //calculate Signal Line
             //calculate SMA for period 9
            totalPrice9= 0;
            for (int i=0 ; i<9 ; i++)
            { 
                totalPrice9 += Double.parseDouble(MACDLine[i][2]);
            }
            SMA9 =  String.valueOf(SMA(totalPrice9,9));
             //ForexFileReader.printMatrix(SMA9);
            
             SignalLine[0][0] = MACDLine [8][0];
             SignalLine[0][1] = MACDLine [8][1];
             SignalLine[0][2] = SMA9;
             j=1;
             for (int i=9 ; MACDLine[i][0]!=null ; i++)
             {
                    //copy date
                    SignalLine[j][0] = MACDLine [i][0]; 
                    //copy time
                    SignalLine[j][1] = MACDLine [i][1]; 
                    //calculate EMA9
                    SignalLine[j][2] = String.valueOf(EMA(Double.parseDouble(MACDLine[i][2]),Double.parseDouble(SignalLine[j-1][2]),9));
                    j++;
             }
             //ForexFileReader.printMatrix(SignalLine);
             
             //calculate MACDLine
             j=8;
             for (int i=0 ; SignalLine[i][0]!=null ; i++)
             {
                    //copy date
                    Histogram[i][0] = SignalLine[i][0]; 
                    //copy time
                    Histogram[i][1] = SignalLine[i][1]; 
                    //calculate Histogram by MACDLine - Signal Line
                    Histogram[i][2] = String.valueOf(Double.parseDouble(MACDLine[j][2]) - Double.parseDouble(SignalLine[i][2]));
                    j++;
             }
             //ForexFileReader.printMatrix(Histogram);
//             matrixToCSV(Histogram);
             
            //give recommendation from intersection between signal and macd line
             j=0;
             for (int i=1 ; Histogram[i][0]!=null ; i++)
             {
                //copy date
                Recommendation[j][0] = Histogram[i][0]; 
                //copy time
                Recommendation[j][1] = Histogram[i][1]; 
                Recommendation[j][2] = Histogram[i][2];
                
                //calculate point where to sell or buy using histogram
                if(Double.parseDouble(Histogram[i-1][2])>0){
                    Recommendation[j][3] = "stall";
                    if(Double.parseDouble(Histogram[i][2])<0){
                        Recommendation[j][3] = "sell";
                    }
                }
                else if(Double.parseDouble(Histogram[i-1][2])<0){
                    Recommendation[j][3] = "stall";
                    if(Double.parseDouble(Histogram[i][2])>0){
                        Recommendation[j][3] = "buy";
                    }
                }
                j++;
             }
//             ForexFileReader.printMatrix(Recommendation);
             ForexFileWriter.MACDPriceToCSV(Recommendation);
             ForexFileWriter.MACDToArff(Recommendation);
             ForexFileWriter.NormalizedMACDToArff(Recommendation);
     }
    
    
}
