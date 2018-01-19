package org.uma.jmetal.utility;

import java.awt.Color;
import java.awt.Shape;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

public class GenerateScatterPlotChart extends JFrame {
  private static final long serialVersionUID = 6294689542092367723L;

  public GenerateScatterPlotChart(String title, double[] data1, double[] data2, double[] data3, double[] data4) {
    super(title);

    // Create dataset
    XYDataset dataset = createDataset(data1, data2, data3, data4);

    // Create chart
    JFreeChart chart = ChartFactory.createScatterPlot(
        "NSGAII-classic NRP(nrp1.txt) | CF:0.5 | PS:1000 | Mutation/Crossover: 0.3/0.1 vs. 0.7/0.9", "Profit", "Costs", dataset, PlotOrientation.VERTICAL, false, false, false);

    //chart.getXYPlot().getDomainAxis().setInverted(true);//invert x-axis
    
    chart.getXYPlot().getRangeAxis().setInverted(true); //invert y-axis
    
    
    //Changes background color
    XYPlot plot = (XYPlot)chart.getPlot();
    plot.setBackgroundPaint(new Color(255,228,196));
    
   
    
    /*Change shape of points to cross (x) */
    Shape cross = ShapeUtilities.createDiagonalCross(1, 1);
    Shape triangle = ShapeUtilities.createUpTriangle(0.9f);
    Shape diamond = ShapeUtilities.createDiamond(1.1f);
    Shape crossShape = ShapeUtilities.createRegularCross(1.1f, 1.1f);
    
    //plot = (XYPlot) chart.getPlot();
    XYItemRenderer renderer = plot.getRenderer();
    
    
    renderer.setSeriesShape(0, diamond);
    renderer.setSeriesShape(1, cross);
    //renderer.setSeriesPaint(1, new GradientPaint(1.0f, 2.0f, Color.blue, 3.0f, 4.0f, Color.BLACK));
    
    plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.red); 
    plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(1, Color.blue); 
    plot.getRendererForDataset(plot.getDataset(0)).setSeriesShape(0, diamond);
    plot.getRendererForDataset(plot.getDataset(0)).setSeriesShape(1, crossShape); 
    
    
    
   
    // Create Panel
    ChartPanel panel = new ChartPanel(chart);
    setContentPane(panel);
  }

  private XYDataset createDataset(double[] data1, double[] data2, double data3[], double data4[]) {
    XYSeriesCollection dataset = new XYSeriesCollection();

    //Boys (Age,weight) series
    XYSeries series1 = new XYSeries("P1");
    for(int i = 0; i < data1.length; i++) {
    	series1.add(data1[i], data2[i]);
    }
    
    dataset.addSeries(series1);
    
    XYSeries series2 = new XYSeries("P2");
    for(int i = 0; i < data3.length; i++) {
    	series2.add(data3[i], data4[i]);
    }
    
    dataset.addSeries(series2);
  
    return dataset;
  }

//  public static void main(String[] args) {
//    
////      ScatterPlotExample example = new ScatterPlotExample("Scatter Chart Example | BORAJI.COM");
////      example.setSize(800, 400);
////      example.setLocationRelativeTo(null);
////      example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
////      example.setVisible(true);
//    
//  }
}