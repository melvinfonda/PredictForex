/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package predictforex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import org.jfree.ui.RefineryUtilities;

public class RecommTable extends JPanel {
    private final JTable table;

    public RecommTable() {
        super(new BorderLayout(3, 3));
        this.table = new JTable(new MyModel()){
    public Component prepareRenderer(TableCellRenderer renderer, int Index_row, int Index_col) {
        // get the current row
        Component comp = super.prepareRenderer(renderer, Index_row, Index_col);
        // even index, not selected
        if ("buy".equalsIgnoreCase((String) table.getModel().getValueAt(Index_row, 2)) ) {
            comp.setForeground(Color.green);
        } else if ("sell".equalsIgnoreCase((String) table.getModel().getValueAt(Index_row, 2))){
            comp.setForeground(Color.red);
        }
        return comp;
    }
};
        this.table.setPreferredScrollableViewportSize(new Dimension(330, 400));
        this.table.getColumnModel().getColumn(0).setPreferredWidth(10);
        this.table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        this.table.setFillsViewportHeight(true);
        this.table.setBackground(Color.black);
//        this.table.setForeground(Color.DARK_GRAY);
        
        
        JPanel ButtonOpen = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(ButtonOpen, BorderLayout.SOUTH);
        // Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        // Add the scroll pane to this panel.
        add(scrollPane, BorderLayout.CENTER);
        // add a nice border
        setBorder(new EmptyBorder(5, 5, 5, 5));
        CSVFile Rd = new CSVFile();
        MyModel NewModel = new MyModel();
        this.table.setModel(NewModel);
        File DataFile = new File("csv_files/labeledWithSignal"+ForexPredictor.unlabeledFilename+".csv");
        ArrayList<String[]> Rs2 = Rd.ReadCSVfile(DataFile);
        NewModel.AddCSVData(Rs2);
        
//        System.out.println("Rows: " + NewModel.getRowCount());
//        System.out.println("Cols: " + NewModel.getColumnCount());
    }

    // Method for reading CSV file
    public class CSVFile {
        private final ArrayList<String[]> Rs = new ArrayList<String[]>();
        private String[] OneRow;

        public ArrayList<String[]> ReadCSVfile(File DataFile) {
            try {
                BufferedReader brd = new BufferedReader(new FileReader(DataFile));
                while (brd.ready()) {
                    String st = brd.readLine();
                    OneRow = st.split(",");
                    Rs.add(OneRow);
//                    System.out.println(Arrays.toString(OneRow));
                } // end of while
            } // end of try
            catch (Exception e) {
                String errmsg = e.getMessage();
                System.out.println("File not found:" + errmsg);
            } // end of Catch
            return Rs;
        }// end of ReadFile method
    }// end of CSVFile class

    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("T1Data");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Create and set up the content pane.
        RecommTable newContentPane = new RecommTable();
        frame.setContentPane(newContentPane);
        // Display the window.
//        frame.pack();
        frame.setVisible(true);
    }

    class MyModel extends AbstractTableModel {
        private final String[] columnNames = { "No.", "Datetime", "Signal"};
        private ArrayList<String[]> Data = new ArrayList<String[]>();

        public void AddCSVData(ArrayList<String[]> DataIn) {
            this.Data = DataIn;
            this.fireTableDataChanged();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;// length;
        }

        @Override
        public int getRowCount() {
            return Data.size();
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            return Data.get(row)[col];
        }
    }

    public static void main(String[] args) {
        
        
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}