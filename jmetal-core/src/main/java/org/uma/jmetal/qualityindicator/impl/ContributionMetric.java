package org.uma.jmetal.qualityindicator.impl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.experiment.util.AllFrontFileNames;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.point.Point;

/**
 * This class implements the contribution metric for different reference fronts
 * to the global pareto front. Reference: Zhang2014.
 * 
 * @author Taras Iks <ikstaras@gmail.com>
 */
@SuppressWarnings("serial")
public class ContributionMetric<S extends Solution<?>> extends GenericIndicator<S> {

	/**
	 * Default constructor
	 */
	public ContributionMetric() {
	}

	/**
	 * Constructor
	 *
	 * @param referenceParetoFrontFile
	 * @throws FileNotFoundException
	 */
	public ContributionMetric(String referenceParetoFrontFile) throws FileNotFoundException {
		super(referenceParetoFrontFile);
	}

	/**
	 * Constructor
	 *
	 * @param referenceParetoFront
	 * @throws FileNotFoundException
	 */
	public ContributionMetric(Front referenceParetoFront) {
		super(referenceParetoFront);
	}

	/**
	 * Evaluate() method
	 * 
	 * @param solutionList
	 * @return
	 */
	@Override
	public Double evaluate(List<S> solutionList) {
		return contributionMetric(new ArrayFront(solutionList), referenceParetoFront);
	}

	/**
	 * Calculates the contribution. Given the pareto front, the true pareto front as
	 * <code>double []</code> and the number of objectives, the method return the
	 * value for the metric.
	 * 
	 * @param front
	 *            The front.
	 * @param referenceFront
	 *            The reference pareto front.
	 * @return the value of the generalized spread metric
	 **/
	static int count = 0;

	public double contributionMetric(Front front, Front referenceFront) {

		AllFrontFileNames frontNames = AllFrontFileNames.getInstance();// added by Taras / Singleton.
		Front newFront = null;
		FrontNormalizer frontNormalizer = null;
		Front normalizedReferenceFront = null;
		int number = 0;
		Front globalReferenceFront = null;
		double totalNumberOfPointsInReferenceFront = referenceFront.getNumberOfPoints();
		List<String> referenceFrontNames = new ArrayList<>(frontNames.referenceFront);
		System.out.println("Contribution: " + frontNames.get(count));

		try {
			globalReferenceFront = new ArrayFront(referenceFrontNames.get(0));
			newFront = new ArrayFront(frontNames.get(count++));
			frontNormalizer = new FrontNormalizer(globalReferenceFront);
			normalizedReferenceFront = frontNormalizer.normalize(newFront);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < normalizedReferenceFront.getNumberOfPoints(); i++) {
			Point p1 = normalizedReferenceFront.getPoint(i);
			for (int j = 0; j < referenceFront.getNumberOfPoints(); j++) {
				Point p2 = referenceFront.getPoint(j);
				if (p1.equals(p2)) {
					number++;
					break;
				}
			}
		}

		return number / totalNumberOfPointsInReferenceFront;
	}

	@Override
	public String getName() {
		return "Contribution";
	}

	@Override
	public String getDescription() {
		return "Contribution to the globale Pareto Front Indicator";
	}

	@Override
	public boolean isTheLowerTheIndicatorValueTheBetter() {
		return false;
	}
}
