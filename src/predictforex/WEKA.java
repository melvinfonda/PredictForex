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
import java.util.Random;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.ConverterUtils.DataSource;

public class WEKA {
    private static Instances dataset;
    private static Classifier clasifier;
    private static Evaluation eval;
    private static String[] args;
    
    static void ReadDataset(String FilePath) throws Exception {
        dataset = DataSource.read(FilePath);
        dataset.setClassIndex(dataset.numAttributes()-1);
    }
    
    /* Multilayer Perceptron */
    static void TenFoldTrain_aNN() throws Exception {
        eval = new Evaluation(dataset);
        MultilayerPerceptron tree = new MultilayerPerceptron();
        eval.crossValidateModel(tree, dataset, 10, new Random(1));
        System.out.println(eval.toSummaryString("Results ANN tenfold \n", false));
//        System.out.println(eval.toClassDetailsString());
//        System.out.println(eval.fMeasure(1) + " "+eval.precision(1)+" "+eval.recall(1));
//        System.out.println(eval.toMatrixString());
    }
    static void FullTraining_aNN() throws Exception {
        Classifier cls = new MultilayerPerceptron();
        cls.buildClassifier(dataset);
        eval = new Evaluation(dataset);
        eval.evaluateModel(cls, dataset);
        System.out.println(eval.toSummaryString("Results ANN full training \n", false));
        System.out.println(eval.toClassDetailsString());
        System.out.println(eval.fMeasure(1) + " "+eval.precision(1)+" "+eval.recall(1));
        System.out.println(eval.toMatrixString());
    }
    
    static void SaveModel(Classifier cls) throws Exception {
        clasifier = cls;
        clasifier.buildClassifier(dataset);
        SerializationHelper.write(cls.getClass().getSimpleName()+".model", cls);
    }
    static void ReadModel(String Filepath) throws Exception {
        clasifier = (Classifier) SerializationHelper.read(Filepath);
    }
    static void Classify(String FilePath) throws Exception {
        Instances unlabeled = DataSource.read(FilePath);
        unlabeled.setClassIndex(unlabeled.numAttributes()-1);
        Instances labeled = new Instances(unlabeled);
        for(int i=0; i<unlabeled.numInstances(); i++) {
            double clsLabel = clasifier.classifyInstance(unlabeled.instance(i));
            labeled.instance(i).setClassValue(clsLabel);
        }
        DataSink.write("newLabeledForexPrice.arff", labeled);
    }
    
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
//    public static void main(String[] args) throws Exception {
//        // membaca dataset awal
//        ReadDataset("forexPrice.arff");
//        
//        // membuat model dan menyimpannya
//        SaveModel(new MultilayerPerceptron());
//
//        /* aNN */
//        TenFoldTrain_aNN();
//        FullTraining_aNN();
//        
//        // membaca model yang telah disimpan pada file eksternal
//        ReadModel("MultilayerPerceptron.model");
//        
//        // melakukan klasifikasi terhadap suatu dataset yang belum terlabel berdasarkan model yang telah diload
//        Classify("forexPrice-unlabeled.arff");
//        
//    }
    
}

