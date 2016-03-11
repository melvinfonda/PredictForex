/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package predictforex;

import java.io.FileNotFoundException;
import java.io.IOException;
import static predictforex.WEKA.TenFoldTrain_aNN;
import weka.classifiers.functions.MultilayerPerceptron;

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
    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
        /*TES BACA CSV*/
        ForexFileReader tes = new ForexFileReader();
        forexPairData = tes.forexPrice("tes3.csv");
        //tes.printRawData(forexPairData);
        
    
        /*TES MACD*/
        MACD tesMACD = new MACD();
        tesMACD.MACDAnalysis(forexPairData);   
        
        /*TES ForexFileWriter */
        ForexFileWriter tesANN = new ForexFileWriter();
        tesANN.normalizedForexPriceToArff(forexPairData);
        tesANN.rawForexPriceToArff(forexPairData);
     //   ForexFileWriter.rawForexPriceToArff(forexPairData);
        
//        /*WEKA*/
//        WEKA weka = new WEKA();
//        // membaca dataset awal
//        WEKA.ReadDataset("forexPrice.arff");
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
//        WEKA.ReadModel("MultilayerPerceptron.model");
//        
//        // melakukan klasifikasi terhadap suatu dataset yang belum terlabel berdasarkan model yang telah diload
//        Classify("forexPrice-unlabeled.arff");
     } 
    
}
