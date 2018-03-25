package org.uma.jmetal.util.experiment.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import com.google.common.collect.Multimap;

/**
 * Class defining tasks for the execution of algorithms in parallel.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class ExperimentAlgorithm<S extends Solution<?>, Result> {
	private Algorithm<Result> algorithm;
	private String algorithmTag;
	private String problemTag;

	/**
	 * Constructor
	 */
	public ExperimentAlgorithm(Algorithm<Result> algorithm, String algorithmTag, String problemTag) {
		this.algorithm = algorithm;
		this.algorithmTag = algorithmTag;
		this.problemTag = problemTag;
	}

	public ExperimentAlgorithm(Algorithm<Result> algorithm, String problemTag) {
		this(algorithm, algorithm.getName(), problemTag);
	}

	public void runAlgorithm(int id, Experiment<?, ?> experimentData) {
		AllFrontFileNames frontNames = AllFrontFileNames.getInstance();// added by Taras / Singleton.
		
		String outputDirectoryName = experimentData.getExperimentBaseDirectory() + "/data/" + algorithmTag + "/"
				+ problemTag;

		File timeOutputFile = new File(outputDirectoryName + "_ExecutionTime");
		File outputDirectory = new File(outputDirectoryName);
		if (!outputDirectory.exists()) {
			boolean result = new File(outputDirectoryName).mkdirs();
			if (result) {
				JMetalLogger.logger.info("Creating " + outputDirectoryName);
			} else {
				JMetalLogger.logger.severe("Creating " + outputDirectoryName + " failed");
			}
		}

		String funFile = outputDirectoryName + "/FUN" + id + ".tsv";
		String varFile = outputDirectoryName + "/VAR" + id + ".tsv";
		JMetalLogger.logger.info(" Running algorithm: " + algorithmTag + ", problem: " + problemTag + ", run: " + id
				+ ", funFile: " + funFile);

		/*TODO: use HashMap to add Time to Tag -> "TS" <-> "3ms, 2ms, 5ms.."*/
		long startTime = System.currentTimeMillis();
		algorithm.run();
		Result population = algorithm.getResult();
		long endTime = System.currentTimeMillis();

		long time = endTime - startTime;
		frontNames.timeMap.put(algorithmTag, time);
		System.out.println("Added to Map:" + algorithmTag + "/" + time);
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(timeOutputFile));
			writer.write(String.valueOf(time));
			writer.close();
			JMetalLogger.logger.info("Time: " + time + " written to File: " + timeOutputFile.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		new SolutionListOutput((List<S>) population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext(varFile))
				.setFunFileOutputContext(new DefaultFileOutputContext(funFile)).print();
	}

	public Algorithm<Result> getAlgorithm() {
		return algorithm;
	}

	public String getAlgorithmTag() {
		return algorithmTag;
	}

	public String getProblemTag() {
		return problemTag;
	}
}
