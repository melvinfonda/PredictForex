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
import static predictforex.ANN.TenFoldTrain_aNN;
import weka.classifiers.functions.MultilayerPerceptron;

/**
 *
 * @author Melvin
 */
public class PredictForexTest {
    private static String[][] forexPairData;
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
    
    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
        ForexPredictor forexPredictor = new ForexPredictor();
        
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        filename = bufferRead.readLine();
        
        /*TES BACA CSV*/
        ForexFileReader forexFile = new ForexFileReader();
        forexPairData = forexFile.loadForexPrice(filename+".csv");
        //tes.printRawData(forexPairData);
        
        forexPredictor.useMACD(forexPairData);
        //forexPredictor.useANN(forexPairData,filename);
        //forexPredictor.useMACDandANN(forexPairData,filename);
        
//        /*TES ForexFileWriter */
        ForexFileWriter tesANN = new ForexFileWriter();
//        tesANN.normalizedForexPriceToArff(forexPairData);
//        tesANN.rawForexPriceToArff(forexPairData);
        ForexFileWriter.rawForexPriceToArff(forexPairData);
        ForexFileWriter.normalizedForexPriceToArff(forexPairData);
        
        
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
