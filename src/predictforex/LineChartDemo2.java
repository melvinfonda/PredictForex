import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.swing.JPanel;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.*;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class LineChartDemo2 extends ApplicationFrame
{

        public LineChartDemo2(String s) throws FileNotFoundException, IOException
        {
                super(s);
                JPanel jpanel = createDemoPanel();
                jpanel.setPreferredSize(new Dimension(500, 270));
                setContentPane(jpanel);
        }

        private static XYDataset createDataset() throws FileNotFoundException, IOException
        {
        
        //Line 1     
        FileReader fr;
        fr = new FileReader("csv_files/b.csv");
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        StringTokenizer st = new StringTokenizer(line, ",");
            // Get the data to plot from the remaining lines.
        float minY = Float.MAX_VALUE;
        float maxY = -Float.MAX_VALUE;                           
        XYSeries series = new XYSeries("First");
        while (true) {
            line = br.readLine();
            if (line == null) {
                break;
            }
            st = new StringTokenizer(line, ",");
            // The first token is the x value.
            String xValue = st.nextToken();
            // The last token is the y value.
            String yValue = "";
            while (st.hasMoreTokens()) {
                yValue = st.nextToken();
            }
            float x = Float.parseFloat(xValue);
            float y = Float.parseFloat(yValue);
            series.add(x, y);                        
            minY = Math.min(y, minY);
            maxY = Math.max(y, maxY);
        }
        
//        //Line 2
//        FileReader fr2;
//        fr2 = new FileReader("csv_files/b.csv");
//        BufferedReader br2 = new BufferedReader(fr2);
//        String line2 = br2.readLine();
//        StringTokenizer st2 = new StringTokenizer(line2, ",");
//            // Get the data to plot from the remaining lines.
//        float minY2 = Float.MAX_VALUE;
//        float maxY2 = -Float.MAX_VALUE;                            
//        XYSeries series2 = new XYSeries("Second");
//        while (true) {
//            line2 = br2.readLine();
//            if (line2 == null) {
//                break;
//            }
//            st2 = new StringTokenizer(line2, ",");
//            // The first token is the x value.
//            String xValue2 = st2.nextToken();
//            // The last token is the y value.
//            String yValue2 = "";
//            while (st2.hasMoreTokens()) {
//                yValue2 = st2.nextToken();
//            }
//            float x = Float.parseFloat(xValue2);
//            float y = Float.parseFloat(yValue2);
//            series2.add(x, y);                        
//            minY = Math.min(y, minY);
//            maxY = Math.max(y, maxY);
//        }
                //Add Series
                XYSeriesCollection xyseriescollection = new XYSeriesCollection();
                xyseriescollection.addSeries(series);
//                xyseriescollection.addSeries(series2);
                //xyseriescollection.addSeries(series3);
                return xyseriescollection;                                 
        }

        private static JFreeChart createChart(XYDataset xydataset)
        {
                JFreeChart jfreechart = ChartFactory.createXYLineChart("LineChart", "Time", "Value", xydataset, PlotOrientation.VERTICAL, true, true, false);
                XYPlot xyplot = (XYPlot)jfreechart.getPlot();
//                xyplot.setDomainPannable(true);
//                xyplot.setRangePannable(true);
                XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)xyplot.getRenderer();
                xylineandshaperenderer.setBaseShapesVisible(false);
                xylineandshaperenderer.setBaseShapesFilled(true);
                NumberAxis numberaxis = (NumberAxis)xyplot.getRangeAxis();
        numberaxis.setRange(0.0, 1.0); 
        numberaxis.setTickUnit(new NumberTickUnit(0.1));         
                return jfreechart;
        }

        public static JPanel createDemoPanel() throws FileNotFoundException, IOException
        {
                JFreeChart jfreechart = createChart(createDataset());
                ChartPanel chartpanel = new ChartPanel(jfreechart);
//                chartpanel.setMouseWheelEnabled(true);
                return chartpanel;
        }

        public static void main(String args[]) throws FileNotFoundException, IOException
        {
                LineChartDemo2 linechartdemo2 = new LineChartDemo2("JFreeChart: LineChartDemo2.java");
                linechartdemo2.pack();
                RefineryUtilities.centerFrameOnScreen(linechartdemo2);
                linechartdemo2.setVisible(true);
        }
}