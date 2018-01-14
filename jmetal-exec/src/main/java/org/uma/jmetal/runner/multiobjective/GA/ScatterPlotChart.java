package org.uma.jmetal.runner.multiobjective.GA;

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

 class ScatterPlotExample extends JFrame {
  private static final long serialVersionUID = 6294689542092367723L;

  public ScatterPlotExample(String title, double[] data1, double[] data2) {
    super(title);

    // Create dataset
    XYDataset dataset = createDataset(data1, data2);

    // Create chart
    JFreeChart chart = ChartFactory.createScatterPlot(
        "NSGA-II - realistic NRP | Cost Factor 0.5 | Population Size 1000", "Profit", "Costs", dataset, PlotOrientation.VERTICAL, false, false, false);

    //chart.getXYPlot().getDomainAxis().setInverted(true);//invert x-axis
    
    chart.getXYPlot().getRangeAxis().setInverted(true); //invert y-axis
    
    
    //Changes background color
    XYPlot plot = (XYPlot)chart.getPlot();
    plot.setBackgroundPaint(new Color(255,228,196));
    
    /*Change shape of points to cross (x) */
    Shape cross = ShapeUtilities.createDiagonalCross(3, 1);
    Shape triangle = ShapeUtilities.createUpTriangle(0.9f);
    Shape diamond = ShapeUtilities.createDiamond(1.9f);
    
    plot = (XYPlot) chart.getPlot();
    XYItemRenderer renderer = plot.getRenderer();
    
    renderer.setSeriesShape(0, diamond);
    
   
    // Create Panel
    ChartPanel panel = new ChartPanel(chart);
    setContentPane(panel);
  }

  private XYDataset createDataset(double[] data1, double[] data2) {
    XYSeriesCollection dataset = new XYSeriesCollection();

    //Boys (Age,weight) series
    XYSeries series1 = new XYSeries("Points");
    for(int i = 0; i < data1.length; i++) {
    	series1.add(data1[i], data2[i]);
    }
    
    dataset.addSeries(series1);
    
//   //Girls (Age,weight) series
//    XYSeries series2 = new XYSeries("Girls");
//    series2.add(1, 72.5);
//    series2.add(2, 80.1);
//    series2.add(3, 87.2);
//    series2.add(4, 94.5);
//    series2.add(5, 101.4);
//    series2.add(6, 107.4);
//    series2.add(7, 112.8);
//    series2.add(8, 118.2);
//    series2.add(9, 122.9);
//    series2.add(10, 123.4);
//
//    dataset.addSeries(series2);

    return dataset;
  }

  public static void main(String[] args) {
    
//      ScatterPlotExample example = new ScatterPlotExample("Scatter Chart Example | BORAJI.COM");
//      example.setSize(800, 400);
//      example.setLocationRelativeTo(null);
//      example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//      example.setVisible(true);
    
  }
}