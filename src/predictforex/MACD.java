/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package predictforex;


/**
 *
 * @author Melvin
 */
public class MACD {
    public String[][] MACDLine = new String[ForexFileReader.row][3]; 
    public String[][] SignalLine = new String[ForexFileReader.row][3]; 
    public String[][] Histogram = new String[ForexFileReader.row][3];
    
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
    
    
    public void MACDAnalysis (String[][] rawForexPrice){
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
            //calculate SMA for period 9
            totalPrice9= 0;
            System.out.println("raw :" + rawForexPrice[0][5] );
            for (int i=0 ; i<9 ; i++)
            { 
                totalPrice9 += Double.parseDouble(rawForexPrice[i][5]);
            }
            SMA9 =  String.valueOf(SMA(totalPrice9,9));
             
             //ForexFileReader.printMatrix(SMA9);
             
             //calculate SMA for period 12
            totalPrice12= 0;
            for (int i=0 ; i<12 ; i++)
            { 
                totalPrice12 += Double.parseDouble(rawForexPrice[i][5]);
            }
            SMA12 =  String.valueOf(SMA(totalPrice12,12));
             //ForexFileReader.printMatrix(SMA12);
             
             //calculate SMA for period 26
             totalPrice26= 0;
            for (int i=0 ; i<26 ; i++)
            { 
                totalPrice26 += Double.parseDouble(rawForexPrice[i][5]);
            }
            SMA26 =  String.valueOf(SMA(totalPrice26,26));
             
             //ForexFileReader.printMatrix(SMA26);
            
             //calculate EMA for period 12
             EMA12[0][0] = rawForexPrice [0][0];
             EMA12[0][1] = rawForexPrice [0][1];
             System.out.println("sma :" + SMA12);
             EMA12[0][2] = String.valueOf(EMA(Double.parseDouble(rawForexPrice[0][5]),Double.parseDouble(SMA12),12));
             for (int i=1 ; rawForexPrice[i+12][0]!=null ; i++)
             {
                    //copy date
                    EMA12[i][0] = rawForexPrice [i][0]; 
                    //copy time
                    EMA12[i][1] = rawForexPrice [i][1]; 
                    //calculate EMA12
                    EMA12[i][2] = String.valueOf(EMA(Double.parseDouble(rawForexPrice[i][5]),Double.parseDouble(EMA12[i-1][2]),12));
             }
             
            //ForexFileReader.printMatrix(EMA12);
             
             //calculate EMA for period 26
             EMA26[0][0] = rawForexPrice [0][0];
             EMA26[0][1] = rawForexPrice [0][1];
             EMA26[0][2] = String.valueOf(EMA(Double.parseDouble(rawForexPrice[0][5]),Double.parseDouble(SMA26),26));
             for (int i=1 ; rawForexPrice[i+26][0]!=null ; i++)
             {
                    //copy date
                    EMA26[i][0] = rawForexPrice [i][0]; 
                    //copy time
                    EMA26[i][1] = rawForexPrice [i][1]; 
                    //calculate EMA9
                    EMA26[i][2] = String.valueOf(EMA(Double.parseDouble(rawForexPrice[i][5]),Double.parseDouble(EMA26[i-1][2]),26));
             }
            //ForexFileReader.printMatrix(EMA26);
             
             //calculate Signal Line
             SignalLine[0][0] = rawForexPrice [0][0];
             SignalLine[0][1] = rawForexPrice [0][1];
             SignalLine[0][2] = String.valueOf(EMA(Double.parseDouble(rawForexPrice[0][5]),Double.parseDouble(SMA9),9));
             for (int i=1 ; rawForexPrice[i+9][0]!=null ; i++)
             {
                    //copy date
                    SignalLine[i][0] = rawForexPrice [i][0]; 
                    //copy time
                    SignalLine[i][1] = rawForexPrice [i][1]; 
                    //calculate EMA9
                    SignalLine[i][2] = String.valueOf(EMA(Double.parseDouble(rawForexPrice[i][5]),Double.parseDouble(SignalLine[i-1][2]),9));
             }
             
             //ForexFileReader.printMatrix(SignalLine);
             
             //calculate MACDLine
             for (int i=0 ; rawForexPrice[i+26][0]!=null ; i++)
             {
                    //copy date
                    MACDLine[i][0] = rawForexPrice [i][0]; 
                    //copy time
                    MACDLine[i][1] = rawForexPrice [i][1]; 
                    //calculate SMA9
                    MACDLine[i][2] = String.valueOf(MACDPoint(Double.parseDouble(EMA12[i][2]),Double.parseDouble(EMA26[i][2])));
             }
             //ForexFileReader.printMatrix(MACDLine);
     }
    
    
}
