package predictforex;

import com.opencsv.CSVReader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import predictforex.RecommTable;

public class ResultUI extends ApplicationFrame {
    /**
     * 
     */
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    JButton backButton = new JButton("Back");  
    private     JSplitPane  splitPaneV;
    private     JSplitPane  splitPaneH;
    private     JPanel      tablePanel;
    private     JPanel      tableInputPanel;
    
    private static final long serialVersionUID = 1L;
    XYSeriesCollection dataset;
    JFreeChart chart;
    
    ChartPanel chartPanel;
    final int chartWidth = 460;
    final int chartHeight = 867;
    CSVReader reader;
    String[] readNextLine;
    XYSeries series;

    public ResultUI(String applicationTitle) throws IOException {
    super(applicationTitle);
    
    setBackground( Color.gray );

    //add table panel      
    createTable();
    createInputPanel();
    createChart();
    createMenuBar();


//    //add chart
//    dataset = createDataset();
//    chart = createChart(dataset);
//    chartPanel = new ChartPanel(chart);
//    chartPanel.setPreferredSize(new java.awt.Dimension(chartHeight,
//            chartWidth));
    
    //split pane
    JPanel topPanel = new JPanel();
    topPanel.setLayout( new BorderLayout() );
    getContentPane().add( topPanel );  
    this.add(topPanel);
    // Create a splitter pane
    splitPaneV = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
    topPanel.add( splitPaneV, BorderLayout.CENTER );

    splitPaneH = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
    splitPaneH.setRightComponent( chartPanel);
    splitPaneH.setLeftComponent( tablePanel);
    splitPaneV.setLeftComponent( splitPaneH );
    splitPaneV.setRightComponent( tableInputPanel );
//    splitPaneH.setRightComponent();
    }

    private void createMenuBar() {

        JMenuBar menubar = new JMenuBar();

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        JMenuItem homeMenuItem = new JMenuItem("Home");
        homeMenuItem.setMnemonic(KeyEvent.VK_E);
        homeMenuItem.setToolTipText("Back to home");
        homeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                homeMenuItemActionPerformed(event);
            }
        });
        
        file.add(homeMenuItem);
        file.add(eMenuItem);
        menubar.add(file);

        setJMenuBar(menubar);
    }

    private void homeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
        
        ForexPredictor forexPredictor = new ForexPredictor();
        forexPredictor.pack();
        RefineryUtilities.centerFrameOnScreen(forexPredictor);
        forexPredictor.setVisible(true);
        this.dispose();
    }  

public void createTable(){
    TitledBorder title = BorderFactory.createTitledBorder("Recommendation Table");
    tablePanel = new JPanel();
    tablePanel.setLayout( new BorderLayout() );
    tablePanel.setBorder(title);
    tablePanel.add( new RecommTable() );
}

public void createInputPanel() throws IOException{
    TitledBorder title = BorderFactory.createTitledBorder("Input Table");
    tableInputPanel = new JPanel();
    tableInputPanel.setLayout(new java.awt.BorderLayout());
    tableInputPanel.setBorder(title);
    tableInputPanel.add( new InputTable());
}
public void createChart() throws IOException{
    //add chart
    dataset = createDataset();
    chart = createChart(dataset);
    
    chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(chartHeight,
            chartWidth));
}

    
    public XYSeriesCollection createDataset() throws NumberFormatException,
            IOException {
        dataset = new XYSeriesCollection();
        try {
            reader = new CSVReader(new FileReader("csv_files/Graph_"+ForexPredictor.unlabeledFilename+".csv"),',');
            // Read the header and chuck it away
            //readNextLine = reader.readNext();
            final XYSeries seriesX;  
            final XYSeries seriesY;  
          
            // Set up series
            if("ANN".equals(ForexPredictor.option)||"MACD&ANN".equals(ForexPredictor.option)){
                seriesX = new XYSeries("Actual");
                seriesY = new XYSeries("Predicted");
            }
            else
            {
                seriesX = new XYSeries("Signal Line");
                seriesY = new XYSeries("MACD Line");
            }
//            final XYSeries seriesZ = new XYSeries("Z");

            while ((readNextLine = reader.readNext()) != null) {
                // add values to dataset
                double Time = Double.valueOf(readNextLine[0]);
                double X = Double.valueOf(readNextLine[1]);
                double Y = Double.valueOf(readNextLine[2]);
                seriesX.add(Time, X);
                seriesY.add(Time, Y);
            }

            //System.out.println(seriesX.getMaxX() + "; " + seriesX.getMaxY());

            dataset.addSeries(seriesX);
            dataset.addSeries(seriesY);
//            dataset.addSeries(seriesZ);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
        return dataset;
    }

    public JFreeChart createChart(XYDataset dataset)
            throws NumberFormatException, IOException {
        chart = ChartFactory.createXYLineChart(ForexPredictor.filename + " Analysis Chart", // chart
                                                                        // title
                "Time (Hour)", // domain axis label
                "Price", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // the plot orientation
                true, // legend
                true, // tooltips
                false); // urls
        
        if(ForexPredictor.option.equalsIgnoreCase("ANN")){
        final XYPlot plot = chart.getXYPlot();
        NumberAxis range = (NumberAxis)plot.getRangeAxis();  
            if(ForexPredictor.filename.equals("DAT_MT_GBPUSD_M1_201603")){
            range.setRange(1.35, 1.48);
            range.setTickUnit(new NumberTickUnit(0.01));
            }
            else if(ForexPredictor.filename.equals("DAT_MT_EURUSD_M1_201603")){
            range.setRange(1.07, 1.15);
            range.setTickUnit(new NumberTickUnit(0.01));
            }
            else if(ForexPredictor.filename.equals("DAT_MT_USDCHF_M1_201603")){
            range.setRange(0.95, 1.01);
            range.setTickUnit(new NumberTickUnit(0.01));
            }
            else if(ForexPredictor.filename.equals("DAT_MT_USDJPY_M1_201603")){
            range.setRange(110, 117);
            range.setTickUnit(new NumberTickUnit(1));
            }
        }
        return chart;
    }
      
             
    
    public static void main(String[] args) throws IOException {
        final ResultUI demo = new ResultUI("PredictForex");
//        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}