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
public class ANN {
    
    //to save matriks to arff format
    public static void rawForexPriceToArff (String[][] ForexPrice) throws IOException{
        FileWriter fw = new FileWriter("forexPrice.arff");
        PrintWriter pw = new PrintWriter(fw);
        int counter=0;
        
        pw.println("@RELATION forexPrice");
        pw.println("");
        pw.println("@ATTRIBUTE open real");
        pw.println("@ATTRIBUTE high real");
        pw.println("@ATTRIBUTE low real");
        pw.println("@ATTRIBUTE close real");
        pw.println("@ATTRIBUTE close2 real");
        pw.println("@data");
        
        //count for row to divide between training set and test set
        for(int i=1;ForexPrice[i][0]!=null;i++){
            counter++;
        }
        
        //write price to ARFF
        for(int i=1;(i<=counter*0.8);i++)
        {
                pw.println(ForexPrice[i-1][2]+","+ForexPrice[i-1][3]+","+ForexPrice[i-1][4]+","+ForexPrice[i-1][5]+","+ForexPrice[i][5]);
        }
        
        //Flush the output to the file
        pw.flush();

        //Close the Print Writer
        pw.close();

        //Close the File Writer
        fw.close();
    }
    
    public static void MACDToArff (String[][] MACDPrice) throws IOException{
        FileWriter fw = new FileWriter("MACDPrice.arff");
        PrintWriter pw = new PrintWriter(fw);
        String recommendation;
        double delta;
        int counter=0;
        
        pw.println("@RELATION forexPrice");
        pw.println("");
        pw.println("@ATTRIBUTE open real");
        pw.println("@ATTRIBUTE high real");
        pw.println("@ATTRIBUTE low real");
        pw.println("@ATTRIBUTE close real");
        pw.println("@ATTRIBUTE close2 real");
        pw.println("@data");
        
        //count for row to divide between training set and test set
        for(int i=1;MACDPrice[i][0]!=null;i++){
            counter++;
        }
        
        //write price to ARFF
        for(int i=1;(i<=counter*0.8);i++)
        {
                pw.println(MACDPrice[i-1][2]+","+MACDPrice[i-1][3]+","+MACDPrice[i-1][4]+","+MACDPrice[i-1][5]+","+MACDPrice[i][5]);
        }
        
        //Flush the output to the file
        pw.flush();

        //Close the Print Writer
        pw.close();

        //Close the File Writer
        fw.close();
    }
    
    
}
