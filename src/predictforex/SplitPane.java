/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package predictforex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import org.jfree.ui.RefineryUtilities;

class SplitPane extends JFrame {

private     JSplitPane  splitPaneV;
private     JSplitPane  splitPaneH;
private     JPanel      panel1;
private     JPanel      panel2;
private     JPanel      panel3;


public SplitPane() throws IOException{
    setTitle( "Split Pane Application" );
    setBackground( Color.gray );

    JPanel topPanel = new JPanel();
    topPanel.setLayout( new BorderLayout() );
    getContentPane().add( topPanel );

    // Create the panels
    createPanel1();
    createPanel2();
//    createPanel3();

    // Create a splitter pane
    splitPaneV = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
    topPanel.add( splitPaneV, BorderLayout.CENTER );

    splitPaneH = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
    splitPaneH.setLeftComponent( panel1 );
    splitPaneH.setRightComponent( panel2 );

    splitPaneV.setLeftComponent( splitPaneH );
    splitPaneV.setRightComponent( panel3 );
}

public void createPanel1(){
    panel1 = new JPanel();
    panel1.setLayout( new BorderLayout() );

//    JFrame frame = new JFrame("T1Data");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        // Create and set up the content pane.
//        T1Data newContentPane = new T1Data();
//        frame.setContentPane(newContentPane);
//        frame.setContentPane(newContentPane);
//        // Display the window.
//        frame.pack();
//        frame.setVisible(true);
    
    // Add some buttons
    panel1.add( new T1Data() );

}

public void createPanel2() throws IOException{

    panel2 = new JPanel();
    panel2.setLayout(new java.awt.BorderLayout());
    
//    panel2.setLayout( new BorderLayout() );
////
//    final ChartUI demo = new ChartUI("Test XY Line chart");
////////        System.out.println("Created, pakcking");
////////        demo.pack();
////////        RefineryUtilities.centerFrameOnScreen(demo);
////////        demo.setVisible(true);
//    
    panel2.add( new JLabel( "Notes:" ), BorderLayout.NORTH );
    //panel2.add( new LineChartDemo2());
}

//public void createPanel3(){
//    panel3 = new JPanel();
//    panel3.setLayout( new BorderLayout() );
//    panel3.setPreferredSize( new Dimension( 400, 100 ) );
//    panel3.setMinimumSize( new Dimension( 100, 50 ) );
//
//    panel3.add( new JLabel( "Notes:" ), BorderLayout.NORTH );
//    panel3.add( new JTextArea(), BorderLayout.CENTER );
//}

public static void main( String args[] ) throws IOException{
    try {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } catch (Exception evt) {}
    // Create an instance of the test application
    SplitPane mainFrame = new SplitPane();
    mainFrame.pack();
    mainFrame.setVisible( true );
}
}
