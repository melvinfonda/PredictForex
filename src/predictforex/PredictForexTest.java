/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package predictforex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static predictforex.ANN.TenFoldTrain_aNN;
import weka.classifiers.functions.MultilayerPerceptron;

/**
 *
 * @author Melvin
 */
public class PredictForexTest {
    private static String[][] forexPairData;
    private static String[][] labeledForex;
    public static String filename="";
    public static String unlabeledFilename="";
        /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    
    public void useMACD(String[][] forexPairData) throws IOException
    {
        MACD MACDIndicator = new MACD();
        MACDIndicator.MACDAnalysis(forexPairData);   
    }
    
    public void useANN(String[][] forexPairData, String unlabeled) throws IOException, Exception
    {
        unlabeledFilename = unlabeled;
        unlabeled = unlabeled+".csv";
        ForexFileWriter.unlabeledForexPriceToArff(forexPairData);
        /*ANN*/
        //read initial dataset
        ANN.ReadDataset(unlabeledFilename+".arff");
        //read the model which going to be used
        ANN.ReadModel("ANN.model");
        //classifiy unlabeled arff
        ANN.Classify(unlabeledFilename+".arff");
    }
    
    public void useMACDandANN(String[][] forexPairData, String unlabeled) throws IOException, Exception
    {
        /*MACD*/
        //analyze with MACD
        MACD MACDIndicator = new MACD();
        MACDIndicator.MACDAnalysis(forexPairData);
        
        /*ANN*/
        //write to arff
        ForexFileWriter.unlabeledMACDToArff(MACDIndicator.Recommendation);
        
        unlabeledFilename = unlabeled;
        //read initial dataset
        ANN.ReadDataset(unlabeled+"_unlabeledMACDRecommendation.arff");
        //read the model which going to be used
        ANN.ReadModel("ANN.model");
        //classifiy unlabeled arff
        ANN.Classify(unlabeled+"_unlabeledMACDRecommendation.arff");
        
    }
    
    public void forexSignal() throws IOException
    {
        ForexFileReader forexFile = new ForexFileReader();
        labeledForex = forexFile.loadCSVtoArray("csv_files/labeled_DAT_MT_EURUSD_M1_201603.csv",6);
        String [][]forexWithSignal = new String[labeledForex.length][3];
        int j;
        
        j=0;
        for (int i=2 ; labeledForex[i][0]!=null ; i++)
        {
           // number
           forexWithSignal[j][0] = Integer.toString(i-1); 
           //copy date time
           forexWithSignal[j][1] = labeledForex[i][0]; 

           //calculate point where to sell or buy using close2 value
           if(Double.parseDouble(labeledForex[i-1][5])>Double.parseDouble(labeledForex[i][5])){
                   forexWithSignal[j][2] = "buy";
           }
           else if(Double.parseDouble(labeledForex[i-1][5])<Double.parseDouble(labeledForex[i][5])){
                   forexWithSignal[j][2] = "sell";
           }
           else
           {
               forexWithSignal[j][2] = "stall";
           }
           j++;
        }
        ForexFileReader.printMatrix(forexWithSignal);
//        
//        FileWriter fw = new FileWriter("csv_files/labeledWithSignal.csv");
//        PrintWriter pw = new PrintWriter(fw);
//        
//        for(int x=0;forexWithSignal[x][0]!=null;x++){
//            for(int y=0;y<3;j++){ 
//                pw.print(forexWithSignal[x][y]);
//                if(y!=forexWithSignal[0].length-1)
//                    pw.print(",");
//                else
//                {
//                
//                }
//            }
//            if(forexWithSignal[x+1][0]!=null)
//                pw.println("");
//        }        
//        
//        //Flush the output to the file
//        pw.flush();
//
//        //Close the Print Writer
//        pw.close();
//
//        //Close the File Writer
//        fw.close();
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
        PredictForexTest forexPredictor = new PredictForexTest();
        forexPredictor.forexSignal();
//        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//        filename = bufferRead.readLine();
//        
//        /*TES BACA CSV*/
//        ForexFileReader forexFile = new ForexFileReader();
//        forexPairData = forexFile.loadForexPrice(filename+".csv");
//        //tes.printRawData(forexPairData);
//        
//        forexPredictor.useMACD(forexPairData);
//        //forexPredictor.useANN(forexPairData,filename);
//        //forexPredictor.useMACDandANN(forexPairData,filename);
//        
////        /*TES ForexFileWriter */
//        ForexFileWriter tesANN = new ForexFileWriter();
////        tesANN.normalizedForexPriceToArff(forexPairData);
////        tesANN.rawForexPriceToArff(forexPairData);
//        ForexFileWriter.rawForexPriceToArff(forexPairData);
//        ForexFileWriter.normalizedForexPriceToArff(forexPairData);
        
        
//        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//        unlabeledFilename = bufferRead.readLine();
//        
//        ForexFileReader tes = new ForexFileReader();
//        forexPairData = tes.forexPrice(unlabeledFilename+".csv");
//        ForexFileWriter.unlabeledForexPriceToArff(forexPairData);
//        /*ANN*/
//        ANN weka = new ANN();
//        // membaca dataset awal
//        ANN.ReadDataset(unlabeledFilename+".arff");
//////        
////
//////
//////        /* aNN */
////        TenFoldTrain_aNN();
//////        FullTraining_aNN();
//////        
////        // membuat model dan menyimpannya
////        ANN.SaveModel(new MultilayerPerceptron());
//////        // membaca model yang telah disimpan pada file eksternal
//        ANN.ReadModel("coba.model");
////        
////        // melakukan klasifikasi terhadap suatu dataset yang belum terlabel berdasarkan model yang telah diload
//        ANN.Classify(unlabeledFilename+".arff");
     } 
    
}
