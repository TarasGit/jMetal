package org.uma.jmetal.utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTitleAnnotation;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.ShapeUtilities;

public class GenerateScatterPlotChart extends JFrame {
	private static final long serialVersionUID = 6294689542092367723L;

	public GenerateScatterPlotChart(String title, List<double[]> doubleArrayList, List<String> nameList) {
		super(title);

		// Create dataset
		XYDataset dataset = createDataset(doubleArrayList, nameList);

		// Create chart
		JFreeChart chart = ChartFactory.createScatterPlot(
				"Metaheuristics Performance", "Profit",
				"Costs", dataset, PlotOrientation.VERTICAL, false, false, false);

		// chart.getXYPlot().getDomainAxis().setInverted(true);//invert x-axis

		chart.getXYPlot().getRangeAxis().setInverted(true); // invert y-axis

		/*
		 * Label Position inside
		 * */
		XYPlot plot = (XYPlot) chart.getPlot();
		LegendTitle lt = new LegendTitle(plot);
		lt.setItemFont(new Font("Dialog", Font.PLAIN, 9));
		lt.setBackgroundPaint(new Color(200, 200, 255, 100));
		lt.setFrame(new BlockBorder(Color.white));
		lt.setPosition(RectangleEdge.BOTTOM);
		XYTitleAnnotation ta = new XYTitleAnnotation(0.95, 0.05, lt,RectangleAnchor.BOTTOM_RIGHT);

		ta.setMaxWidth(0.48);
		plot.addAnnotation(ta);
		
		
		// Changes background color
		//XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(new Color(255, 228, 196));

		
		/* Change shape of points to cross (x) */
		Shape cross = ShapeUtilities.createDiagonalCross(0.3f, 0.3f);
		Shape triangle = ShapeUtilities.createUpTriangle(0.9f);
		Shape diamond = ShapeUtilities.createDiamond(1.1f);
		Shape crossShape = ShapeUtilities.createRegularCross(1.1f, 1.1f);
		Shape upTrianle = ShapeUtilities.createDownTriangle(0.9f);

		// plot = (XYPlot) chart.getPlot();
		XYItemRenderer renderer = plot.getRenderer();
		

		renderer.setSeriesShape(0, diamond);
		renderer.setSeriesShape(1, cross);
		renderer.setSeriesShape(2, triangle);
		renderer.setSeriesShape(3, crossShape);
		// renderer.setSeriesPaint(1, new GradientPaint(1.0f, 2.0f, Color.blue, 3.0f,
		// 4.0f, Color.BLACK));

		plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.red);
		plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(1, Color.blue);
		plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(2, Color.getColor("brown"));
		plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(3, Color.magenta);
		plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(4, Color.black);
		
		
		plot.getRendererForDataset(plot.getDataset(0)).setSeriesShape(0, triangle);
		plot.getRendererForDataset(plot.getDataset(0)).setSeriesShape(1, triangle);
		plot.getRendererForDataset(plot.getDataset(0)).setSeriesShape(2, triangle);
		plot.getRendererForDataset(plot.getDataset(0)).setSeriesShape(3, triangle);
		plot.getRendererForDataset(plot.getDataset(0)).setSeriesShape(4, triangle);
		
		
		/*
		 * Create Labels
		 * */
		
		
		
		// Create Panel
		ChartPanel panel = new ChartPanel(chart);
		setContentPane(panel);
	}

	private XYDataset createDataset(List<double[]> doubleArrayList, List<String> nameList) {
		XYSeriesCollection dataset = new XYSeriesCollection();

		double[] data1 = null, data2 = null;
		int index=0;
		int size = doubleArrayList.size();
		for (int j = 0; j < size ; j+=2) {
			System.out.println("plog: " + j);
			data1 = doubleArrayList.get(j);
			data2 = doubleArrayList.get(j+1);
			
			XYSeries series = new XYSeries(nameList.get(index++));
			for (int i = 0; i < data1.length; i++) {
				series.add((data1[i]), data2[i]);
			}
			
			dataset.addSeries(series);
		}
		return dataset;
	}

	// public static void main(String[] args) {
	//
	//// ScatterPlotExample example = new ScatterPlotExample("Scatter Chart Example
	// | BORAJI.COM");
	//// example.setSize(800, 400);
	//// example.setLocationRelativeTo(null);
	//// example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	//// example.setVisible(true);
	//
	// }
}