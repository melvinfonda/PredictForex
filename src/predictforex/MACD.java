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
    
    public double SMA (double totalPrice, int periods){    
        return (totalPrice ) / (periods);  
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
    
}
