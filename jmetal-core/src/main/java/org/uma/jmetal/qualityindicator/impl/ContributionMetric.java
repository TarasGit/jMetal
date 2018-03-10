package org.uma.jmetal.qualityindicator.impl;

import java.io.FileNotFoundException;
import java.util.List;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
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
		return generalizedSpread(new ArrayFront(solutionList), referenceParetoFront);
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
	public double generalizedSpread(Front front, Front referenceFront) {

		double totalNumberOfPointsInFront = front.getNumberOfPoints();
		double totalNumberOfPointsInReferenceFront = referenceFront.getNumberOfPoints();
		Point frontPoint;
		Point tmpPoint;
		double count = 0;

		for (int i = 0; i < totalNumberOfPointsInFront; i++) {
			frontPoint = front.getPoint(i);
			for (int j = 0; j < totalNumberOfPointsInReferenceFront; j++) {
				tmpPoint = referenceFront.getPoint(j);
				if (frontPoint.getDimensionValue(0) == tmpPoint.getDimensionValue(0)
						&& frontPoint.getDimensionValue(1) == tmpPoint.getDimensionValue(1))
					count++;
			}
		}
		return count / totalNumberOfPointsInFront;
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
