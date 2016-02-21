/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package predictforex;

import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author Melvin
 */
public class PredictForex {
    private static String[][] forexPairData;
   
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        /*TES BACA CSV*/
        ForexFileReader tes = new ForexFileReader();
        forexPairData = tes.forexPrice("tes.csv");
        //tes.printMatrix(forexPairData);
        
    
        /*TES MACD*/
        MACD tesMACD = new MACD();
        tesMACD.MACDAnalysis(forexPairData);
//        System.out.println("tes: " + tesMACD.SMA(222.3, 10));
//        System.out.println("tes: " + tesMACD.Multiplier(10));
//        System.out.println("tes: " + tesMACD.EMA(22.61, 22.27 , 10));
//        System.out.println("tes: " + tesMACD.MACDPoint(tesMACD.EMA(22.39, 22.21 , 12),tesMACD.EMA(23.33, 23.39 , 26)));
//        //System.out.println("tes: " + tesMACD.SignalPoint(22.24,22.22));
//        
//        MACDLine.add(tesMACD.MACDPoint(tesMACD.EMA(22.39, 22.21 , 12),tesMACD.EMA(23.33, 23.39 , 26)));
//        MACDLine.add(1.0);
//        MACDLine.add(2.0);
//        // adding elements into the enumeration
//           Enumeration e=MACDLine.elements();
//
//           // let us print all the elements available in enumeration
//           System.out.println("Numbers in the enumeration are :- "); 
//           while (e.hasMoreElements()) {         
//           System.out.println("Number = " + e.nextElement());
//           }           
     } 
    
}
