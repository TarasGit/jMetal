package org.uma.jmetal.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.WindowConstants;

import org.uma.jmetal.qualityindicator.impl.SetCoverage;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.util.PointSolution;

public class CreateScatterPlotWithParameters {

	public static void main(String[] args) {

		String DIR = "../MetaheuristicsStudy";
		String PROBLEM_NAME = "NRPRealistic";
		if (DIR.isEmpty() || PROBLEM_NAME.isEmpty()) {
			DIR = args[0]; // MetaheuristicsStudy
			PROBLEM_NAME = args[1]; // NRPRealistic
		}

		String problemDirectoryACO = DIR + "/" + "data" + "/ACO/" + PROBLEM_NAME + "/" + "FUN0.tsv";
		String problemDirectoryTS = DIR + "/" + "data" + "/TS/" + PROBLEM_NAME + "/" + "FUN0.tsv";
		String problemDirectorySA = DIR + "/" + "data" + "/SA/" + PROBLEM_NAME + "/" + "FUN0.tsv";
		String problemDirectoryNSGAII = DIR + "/" + "data" + "/NSGAII/" + PROBLEM_NAME + "/" + "FUN0.tsv";
		String problemDirectoryR = DIR + "/" + "data" + "/R/" + PROBLEM_NAME + "/" + "FUN0.tsv";

		String referenceFrontName = DIR + "/" + "referenceFronts" + "/" + PROBLEM_NAME + ".rf"; // referenceFront/NRPRealistic.rf

		File f = new File(".");

		JMetalLogger.logger.info(".: " + f.getPath());
		JMetalLogger.logger.info("ACO: " + problemDirectoryACO);
		JMetalLogger.logger.info("TS: " + problemDirectoryTS);
		JMetalLogger.logger.info("SA: " + problemDirectorySA);
		JMetalLogger.logger.info("R: " + problemDirectoryR);

		Front referenceFront = null;
		Front referenceFrontACO = null;
		Front referenceFrontNSGAII = null;
		Front referenceFrontSA = null;
		Front referenceFrontTS = null;
		Front referenceFrontR = null;

		Front frontACO = null;
		Front frontTS = null;
		Front frontSA = null;
		Front frontNSGAII = null;
		Front frontR = null;

		try {

			referenceFront = new ArrayFront(
					"/home/taras/Programming/jMetalCode/jMetal/jmetal-exec/referenceFronts/NRPRealistic.rf");
			referenceFrontACO = new ArrayFront(
					"/home/taras/Programming/jMetalCode/jMetal/jmetal-exec/referenceFronts/NRPRealistic.ACO.rf");
			referenceFrontNSGAII = new ArrayFront(
					"/home/taras/Programming/jMetalCode/jMetal/jmetal-exec/referenceFronts/NRPRealistic.NSGAII.rf");
			referenceFrontTS = new ArrayFront(
					"/home/taras/Programming/jMetalCode/jMetal/jmetal-exec/referenceFronts/NRPRealistic.MOTS.rf");
			referenceFrontSA = new ArrayFront(
					"/home/taras/Programming/jMetalCode/jMetal/jmetal-exec/referenceFronts/NRPRealistic.MOSA.rf");
			referenceFrontR = new ArrayFront(
					"/home/taras/Programming/jMetalCode/jMetal/jmetal-exec/referenceFronts/NRPRealistic.RS.rf");

			frontACO = new ArrayFront(
					"/home/taras/Programming/jMetalCode/jMetal/jmetal-exec/MetaheuristicsStudy/data/ACO/NRPRealistic/FUN0.tsv");
			frontTS = new ArrayFront(
					"/home/taras/Programming/jMetalCode/jMetal/jmetal-exec/MetaheuristicsStudy/data/MOTS/NRPRealistic/FUN0.tsv");
			frontSA = new ArrayFront(
					"/home/taras/Programming/jMetalCode/jMetal/jmetal-exec/MetaheuristicsStudy/data/MOSA/NRPRealistic/FUN0.tsv");
			frontNSGAII = new ArrayFront(
					"/home/taras/Programming/jMetalCode/jMetal/jmetal-exec/MetaheuristicsStudy/data/NSGAII/NRPRealistic/FUN0.tsv");
			frontR = new ArrayFront(
					"/home/taras/Programming/jMetalCode/jMetal/jmetal-exec/MetaheuristicsStudy/data/RS/NRPRealistic/FUN0.tsv");

			// referenceFront = new ArrayFront(referenceFrontName);
			// frontACO = new ArrayFront(problemDirectoryACO);
			// frontTS = new ArrayFront(problemDirectoryTS);
			// frontSA = new ArrayFront(problemDirectorySA);
			// frontNSGAII = new ArrayFront(problemDirectoryNSGAII);
			// frontR = new ArrayFront(problemDirectoryR);
		} catch (FileNotFoundException e) {
			JMetalLogger.logger.warning("File not found: " + referenceFrontName);
			e.printStackTrace();
		}

		int numberOfPointReferenceFront = referenceFront.getNumberOfPoints();
		int numberOfPointReferenceFrontACO = referenceFrontACO.getNumberOfPoints();
		int numberOfPointReferenceFrontTS = referenceFrontTS.getNumberOfPoints();
		int numberOfPointReferenceFrontSA = referenceFrontSA.getNumberOfPoints();
		int numberOfPointReferenceFrontNSGAII = referenceFrontNSGAII.getNumberOfPoints();
		int numberOfPointReferenceFrontR = referenceFrontR.getNumberOfPoints();
		
		double contribACO = (double)numberOfPointReferenceFrontACO / numberOfPointReferenceFront;
		double contribTS = (double)numberOfPointReferenceFrontTS / numberOfPointReferenceFront;
		double contribSA = (double)numberOfPointReferenceFrontSA / numberOfPointReferenceFront;
		double contribNSGAII = (double)numberOfPointReferenceFrontNSGAII / numberOfPointReferenceFront;
		double contribR = (double)numberOfPointReferenceFrontR / numberOfPointReferenceFront;
		
		
		System.out.println("Contribution ACO:" + contribACO);
		System.out.println("Contribution TS:" + contribTS);
		System.out.println("Contribution SA:" + contribSA);
		System.out.println("Contribution NSGAII:" + contribNSGAII);
		System.out.println("Contribution R:" + contribR);


		
		int numberOfPointACO = frontACO.getNumberOfPoints();
		int numberOfPointTS = frontTS.getNumberOfPoints();
		int numberOfPointSA = frontSA.getNumberOfPoints();
		int numberOfPointNSGAII = frontNSGAII.getNumberOfPoints();
		int numberOfPointR = frontR.getNumberOfPoints();
		
		
		double data1NSGA[] = new double[numberOfPointNSGAII];
		double data2NSGA[] = new double[numberOfPointNSGAII];

		double data1TS[] = new double[numberOfPointTS];
		double data2TS[] = new double[numberOfPointTS];

		double data1SA[] = new double[numberOfPointSA];
		double data2SA[] = new double[numberOfPointSA];

		double data1ACO[] = new double[numberOfPointACO];
		double data2ACO[] = new double[numberOfPointACO];

		double data1R[] = new double[numberOfPointR];
		double data2R[] = new double[numberOfPointR];

		List<PointSolution> populationNSGA = FrontUtils.convertFrontToSolutionList(frontNSGAII);
		List<PointSolution> populationACO = FrontUtils.convertFrontToSolutionList(frontACO);
		List<PointSolution> populationTS = FrontUtils.convertFrontToSolutionList(frontTS);
		List<PointSolution> populationSA = FrontUtils.convertFrontToSolutionList(frontSA);
		List<PointSolution> populationR = FrontUtils.convertFrontToSolutionList(frontR);

		for (int i = 0; i < numberOfPointNSGAII; i++) {
			data1NSGA[i] = populationNSGA.get(i).getObjective(0) * -1;
			data2NSGA[i] = populationNSGA.get(i).getObjective(1);
		}

		for (int i = 0; i < numberOfPointACO; i++) {
			data1ACO[i] = populationACO.get(i).getObjective(0) * -1;
			data2ACO[i] = populationACO.get(i).getObjective(1);
		}

		for (int i = 0; i < numberOfPointSA; i++) {
			data1SA[i] = populationSA.get(i).getObjective(0) * -1;
			data2SA[i] = populationSA.get(i).getObjective(1);
		}

		for (int i = 0; i < numberOfPointTS; i++) {
			data1TS[i] = populationTS.get(i).getObjective(0) * -1;
			data2TS[i] = populationTS.get(i).getObjective(1);
		}

		for (int i = 0; i < numberOfPointR; i++) {
			data1R[i] = populationR.get(i).getObjective(0) * -1;
			data2R[i] = populationR.get(i).getObjective(1);
		}

		/* Create List of Arrays with data */
		List<double[]> doubleArrayList = new ArrayList<>();
		doubleArrayList.add(data1NSGA);// red
		doubleArrayList.add(data2NSGA);

		doubleArrayList.add(data1ACO);// blue
		doubleArrayList.add(data2ACO);

		doubleArrayList.add(data1SA);// black
		doubleArrayList.add(data2SA);

		doubleArrayList.add(data1TS);// magenta
		doubleArrayList.add(data2TS);

		doubleArrayList.add(data1R);// yellow
		doubleArrayList.add(data2R);

		SetCoverage sc = new SetCoverage();
		List<PointSolution> referenceFrontSolutionList = FrontUtils.convertFrontToSolutionList(referenceFront);

		double setCoverageMetricACO = sc.evaluate(referenceFrontSolutionList,
				FrontUtils.convertFrontToSolutionList(frontACO));
		System.out.println("Set Coverage Metric ACO: " + setCoverageMetricACO);
		
		double setCoverageMetricTS = sc.evaluate(referenceFrontSolutionList,
				FrontUtils.convertFrontToSolutionList(frontTS));
		System.out.println("Set Coverage Metric TS: " + setCoverageMetricTS);

		
		double setCoverageMetricNSGAII = sc.evaluate(referenceFrontSolutionList,
				FrontUtils.convertFrontToSolutionList(frontNSGAII));
		System.out.println("Set Coverage Metric NSGAII: " + setCoverageMetricNSGAII);

		
		double setCoverageMetricSA = sc.evaluate(referenceFrontSolutionList,
				FrontUtils.convertFrontToSolutionList(frontSA));
		System.out.println("Set Coverage Metric SA: " + setCoverageMetricSA);

		double setCoverageMetricR = sc.evaluate(referenceFrontSolutionList,
				FrontUtils.convertFrontToSolutionList(frontR));
		System.out.println("Set Coverage Metric R: " + setCoverageMetricR);
		
		

		List<String> nameList = Arrays.asList("NSGA-II", "ACO", "SA", "TS", "R");
		GenerateScatterPlotChart example = new GenerateScatterPlotChart("Scatter Chart", doubleArrayList, nameList);
		example.setSize(1200, 800);
		example.setLocationRelativeTo(null);
		example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		example.setVisible(true);
	}

}
