/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package predictforex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import static predictforex.WEKA.TenFoldTrain_aNN;
import weka.classifiers.functions.MultilayerPerceptron;

/**
 *
 * @author Melvin
 */
public class PredictForex {
    private static String[][] forexPairData;
    public static String filename="";
    public static String unlabeledFilename="";
        /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
        
//        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//        filename = bufferRead.readLine();
//        
//        /*TES BACA CSV*/
//        ForexFileReader tes = new ForexFileReader();
//        forexPairData = tes.forexPrice(filename+".csv");
//        //tes.printRawData(forexPairData);
//        
//    
//        /*TES MACD*/
//        MACD tesMACD = new MACD();
//        tesMACD.MACDAnalysis(forexPairData);   
//        
//        /*TES ForexFileWriter */
//        ForexFileWriter tesANN = new ForexFileWriter();
//        tesANN.normalizedForexPriceToArff(forexPairData);
//        tesANN.rawForexPriceToArff(forexPairData);
//        ForexFileWriter.rawForexPriceToArff(forexPairData);
        
        
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        unlabeledFilename = bufferRead.readLine();
        
        ForexFileReader tes = new ForexFileReader();
        forexPairData = tes.forexPrice(unlabeledFilename+".csv");
        ForexFileWriter.unlabeledForexPriceToArff(forexPairData);
        /*WEKA*/
        WEKA weka = new WEKA();
        // membaca dataset awal
        WEKA.ReadDataset(unlabeledFilename+".arff");
////        
//
////
////        /* aNN */
//        TenFoldTrain_aNN();
////        FullTraining_aNN();
////        
//        // membuat model dan menyimpannya
//        WEKA.SaveModel(new MultilayerPerceptron());
////        // membaca model yang telah disimpan pada file eksternal
        WEKA.ReadModel("coba.model");
//        
//        // melakukan klasifikasi terhadap suatu dataset yang belum terlabel berdasarkan model yang telah diload
        WEKA.Classify(unlabeledFilename+".arff");
     } 
    
}
