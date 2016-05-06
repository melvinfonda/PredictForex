/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package predictforex;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jfree.ui.RefineryUtilities;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVSaver;

/**
 *
 * @author Melvin
 */
public class ForexPredictor extends javax.swing.JFrame {
    File file;
    private static String[][] forexPairData;
    public static String[][] labeledForex = new String[ForexFileReader.row][5]; //labeled predicted forexproce in matrix
    public static String[][] forexWithSignal = new String[ForexFileReader.row][3]; // labeled forexprice with signal 
    public static String[][] labeledMACD = new String[ForexFileReader.row][5]; //labeled predicted MACD in matrix
    public static String[][] MACDWithSignal = new String[ForexFileReader.row][3]; //labeled MACD in matrix
    public static String filename="";
    public static String unlabeledFilename="";
    public static String option;
        /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    
    public void ARFFtoCSVLoader(String option) throws IOException
    {
        //load arff
        ArffLoader loader = new ArffLoader();
        if("ANN".equals(option))
            loader.setSource(new File("arff_files/labeled_"+filename+".arff"));
        else
            loader.setSource(new File("arff_files/labeled_"+filename+"_MACDANNRecommendation.arff"));    
        Instances data = loader.getDataSet();
        
        //save CSV
        CSVSaver saver = new CSVSaver();
        saver.setInstances(data);
        //and save as CSV
        if("ANN".equals(option))
            saver.setFile(new File("csv_files/labeled_"+filename+".csv"));
        else
            saver.setFile(new File("csv_files/labeled_"+filename+"_MACDANNRecommendation.csv"));
        saver.writeBatch();
    }
    
    public static void ANNRecommendationToGraph (String[][] ANNPrice) throws IOException{
        FileWriter fw = new FileWriter("csv_files/Graph_"+ForexPredictor.filename+".csv");
        PrintWriter pw = new PrintWriter(fw);
        
        //write price to csv
        for(int i=2;ANNPrice[i][0]!=null;i++)
        {
                pw.println(Integer.toString(i-1)+","+ANNPrice[i][4]+","+ANNPrice[i-1][5]);
        }
        
        //Flush the output to the file
        pw.flush();

        //Close the Print Writer
        pw.close();

        //Close the File Writer
        fw.close();
    }
    
    public static void MACDANNRecommendationToGraph (String[][] MACDPrice) throws IOException{
        FileWriter fw = new FileWriter("csv_files/Graph_"+ForexPredictor.filename+"_MACDANNRecommendation.csv");
        PrintWriter pw = new PrintWriter(fw);
        
        //write price to csv
         int j=1;
        //write price to csv
        for(int i=2;MACDPrice[i][0]!=null;i++)
        {
                pw.println(Integer.toString(i-1)+","+MACDPrice[i][8]+","+MACDPrice[i-1][9]);
        }
        
        //Flush the output to the file
        pw.flush();

        //Close the Print Writer
        pw.close();

        //Close the File Writer
        fw.close();
    }
    
    public void forexSignal() throws IOException
    {
        ForexFileReader forexFile = new ForexFileReader();
        labeledForex = forexFile.loadCSVtoArray("csv_files/labeled_"+filename+".csv",6);
        ForexPredictor.ANNRecommendationToGraph(labeledForex);
        int j;
        
        j=0;
        for (int i=2 ; labeledForex[i][0]!=null ; i++)
        {
           // number
           forexWithSignal[j][0] = Integer.toString(i-1); 
           //copy date time
           forexWithSignal[j][1] = labeledForex[i][0]; 

           //calculate point where to sell or buy using close2 value
           if(Double.parseDouble(labeledForex[i-1][5])<Double.parseDouble(labeledForex[i][5])){
                   forexWithSignal[j][2] = "buy";
           }
           else if(Double.parseDouble(labeledForex[i-1][5])>Double.parseDouble(labeledForex[i][5])){
                   forexWithSignal[j][2] = "sell";
           }
           else
           {
               forexWithSignal[j][2] = "stall";
           }
           j++;
        }
        //ForexFileReader.printMatrix(forexWithSignal);
        ForexFileWriter.forexSignalToCSV(forexWithSignal);
    }
    
    public void MACDSignal() throws IOException
    {
        ForexFileReader forexFile = new ForexFileReader();
        labeledMACD = forexFile.loadCSVtoArray("csv_files/labeled_"+filename+"_MACDANNRecommendation.csv",10);
        ForexPredictor.MACDANNRecommendationToGraph(labeledMACD);
        int j;
        
        j=0;
        for (int i=2 ; labeledMACD[i][0]!=null ; i++)
        {
           //calculate point where to sell or buy using histogram
                if(Double.parseDouble(labeledMACD[i-1][9])>0){
                    if(Double.parseDouble(labeledMACD[i][9])<0){
                        // number
                        MACDWithSignal[j][0] = Integer.toString(j+1); 
                        //copy date time
                        MACDWithSignal[j][1] = labeledMACD[i][0]; 
                        MACDWithSignal[j][2] = "sell";
                        j++;
                    }
                }
                else if(Double.parseDouble(labeledMACD[i-1][9])<0){
                    if(Double.parseDouble(labeledMACD[i][9])>0){
                        // number
                        MACDWithSignal[j][0] = Integer.toString(j+1); 
                        //copy date time
                        MACDWithSignal[j][1] = labeledMACD[i][0]; 
                        MACDWithSignal[j][2] = "buy";
                        j++;
                    }
                }
        }
        //ForexFileReader.printMatrix(MACDWithSignal);
        ForexFileWriter.MACDSignalToCSV(MACDWithSignal);
    }
    
    public void useMACD(String[][] forexPairData) throws IOException
    {
        MACD MACDIndicator = new MACD();
        MACDIndicator.MACDAnalysis(forexPairData); 
        unlabeledFilename=filename+"_MACDRecommendation";
    }
    
    public void useANN(String[][] forexPairData, String forexFilename) throws IOException, Exception
    {
        unlabeledFilename = forexFilename;
        ForexFileWriter.unlabeledForexPriceToArff(forexPairData);
        /*ANN*/
        //read initial dataset
        ANN.ReadDataset("arff_files/"+forexFilename+".arff");
        //read the model which going to be used
        //ANN.ReadModel(unlabeledFilename.substring(0, 14)+"ANN.model");
        ANN.ReadModel("model/"+forexFilename.substring(0, 14)+"ANN.model");
//        ANN.ReadModel(unlabeledFilename.substring(0, 14)+"normalizedclassANN.model");
        //classifiy unlabeled arff
        ANN.Classify("arff_files/"+forexFilename+".arff");
    }
    
    public void useMACDandANN(String[][] forexPairData, String forexFilename) throws IOException, Exception
    {
        /*MACD*/
        //analyze with MACD
        MACD MACDIndicator = new MACD();
        MACDIndicator.MACDAnalysis(forexPairData);
        
        /*ANN*/
        //write to arff
        
        ForexFileWriter.unlabeledMACDToArff(MACDIndicator.Recommendation);
        
        unlabeledFilename = forexFilename+"_MACDANNRecommendation";
        //read initial dataset
        ANN.ReadDataset("arff_files/"+forexFilename+"_unlabeledMACDANNRecommendation.arff");
        //read the model which going to be used
//        ANN.ReadModel(forexFilename.substring(0, 14)+"MACD&ANN.model");
        ANN.ReadModel("model/"+forexFilename.substring(0, 14)+"MACD&ANN.model");
        //classifiy unlabeled arff
        ANN.Classify("arff_files/"+forexFilename+"_unlabeledMACDANNRecommendation.arff");
        
    }
    /**
     * Creates new form PredictForexUI
     */
    public ForexPredictor() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jFileChooser2 = new javax.swing.JFileChooser();
        jFileChooser3 = new javax.swing.JFileChooser();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Rockwell", 1, 24)); // NOI18N
        jLabel1.setText("PredictForex");

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel2.setText("File");

        jLabel3.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel3.setText("Method");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("MACD Only");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("ANN Only");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("MACD and ANN");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Rockwell", 1, 14)); // NOI18N
        jButton1.setText("Analyze");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setEditable(false);
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton2.setText("Browse");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvUploader(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addGap(46, 46, 46))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(181, 181, 181))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jRadioButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(29, 29, 29))
        );

        jMenu3.setText("File");

        jMenuItem1.setText("Home");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem2.setText("Exit");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuBar2.add(jMenu3);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
        option = "MACD";
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void csvUploader(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvUploader
        // TODO add your handling code here:
        JFileChooser jfc;
        jfc = new JFileChooser();  
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
        File f = new File(System.getProperty("user.dir"));
        jfc.setFileFilter(filter);
        jfc.setCurrentDirectory(f);
        int returnVal = jfc.showOpenDialog(jLabel1);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = jfc.getSelectedFile();
            //jTextField1.setText(file.toString());
            jTextField1.setText(jfc.getSelectedFile().getName());
            filename = jfc.getSelectedFile().getName().substring(0, jfc.getSelectedFile().getName().lastIndexOf('.'));
        } 
        
            ForexFileReader forexFile = new ForexFileReader();
                forexPairData = forexFile.loadForexPrice(filename+".csv");
                try {
                    ForexFileWriter.ForexPriceToCSV(forexPairData);
                }
                catch(Exception IOException){
                }
    }//GEN-LAST:event_csvUploader

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
        option = "ANN";
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
        option = "MACD&ANN";
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ForexPredictor forexPredictor = new ForexPredictor();
        
        if("MACD".equals(option)){
            try {
                forexPredictor.useMACD(forexPairData);
            }
            catch(Exception IOException){
            }
        }
        else if("ANN".equals(option)){
            try {
                forexPredictor.useANN(forexPairData,filename);
                forexPredictor.ARFFtoCSVLoader(option);
                forexPredictor.forexSignal();
            }
            catch(Exception IOException){
                IOException.printStackTrace();
            }
        }
        else if("MACD&ANN".equals(option)){
            try {
                forexPredictor.useMACDandANN(forexPairData,filename);
                forexPredictor.ARFFtoCSVLoader(option);
                forexPredictor.MACDSignal();
            }
            catch(Exception IOException){
                System.err.println("An IOException was caught!");

            }
        }
        
        try {
            //new ResultsUI().setVisible(true);
            //new T1Data().setVisible(true);
            ResultUI resultpage = new ResultUI("Results");
            resultpage.pack();
            RefineryUtilities.centerFrameOnScreen(resultpage);
            resultpage.setVisible(true);
        } catch (IOException ex){};
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
            // TODO add your handling code here:
         System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ForexPredictor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ForexPredictor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ForexPredictor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ForexPredictor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ForexPredictor forexPredictor = new ForexPredictor();
                forexPredictor.setVisible(true);
                RefineryUtilities.centerFrameOnScreen(forexPredictor);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JFileChooser jFileChooser2;
    private javax.swing.JFileChooser jFileChooser3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
