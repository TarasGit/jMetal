package org.uma.jmetal.qualityindicator.impl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.experiment.util.AllFrontFileNames;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;

/**
 * This class implements the unique contribution metric for different reference
 * fronts to the global pareto front. Reference: Zhang2014.
 * 
 * @author Taras Iks <ikstaras@gmail.com>
 */
@SuppressWarnings("serial")
public class UniqueContributionMetric<S extends Solution<?>> extends GenericIndicator<S> {

	/**
	 * Default constructor
	 */
	public UniqueContributionMetric() {
	}

	/**
	 * Constructor
	 *
	 * @param referenceParetoFrontFile
	 * @throws FileNotFoundException
	 */
	public UniqueContributionMetric(String referenceParetoFrontFile) throws FileNotFoundException {
		super(referenceParetoFrontFile);
	}

	/**
	 * Constructor
	 *
	 * @param referenceParetoFront
	 * @throws FileNotFoundException
	 */
	public UniqueContributionMetric(Front referenceParetoFront) {
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
		return uniqueContribution(new ArrayFront(solutionList), referenceParetoFront);
	}

	/**
	 * Calculates the unique contribution. Given the pareto front, the true pareto
	 * front as <code>double []</code> and the number of objectives, the method
	 * return the value for the metric.
	 * 
	 * @param front
	 *            The front.
	 * @param referenceFront
	 *            The reference pareto front.
	 * @return the value of the generalized spread metric
	 **/
	int globalIndex = 0;

	public double uniqueContribution(Front front, Front referenceFront) {

		double totalNumberOfPointsInReferenceFront = referenceFront.getNumberOfPoints();
		AllFrontFileNames frontNames = AllFrontFileNames.getInstance();// added by Taras / Singleton.
		
		Front newFront = null;
		Front actualFront = null;
		Point actualPoint = null;
		Point tmpPoint = null;
		Front globalReferenceFront = null;
		boolean notUnique = false;
		boolean InRF = false;

		
		List<String> globalReferenceFrontNames = new ArrayList<>(frontNames.referenceFront);
		Set<String> nameSet = new HashSet<>();

		Set<String> uniqueFileNamesSet = frontNames.setOfAllFileNames;
		List<String> nameList = new ArrayList<String>(uniqueFileNamesSet);
		int numberOfFiles = nameList.size();
		
		String actualFileName = frontNames.get(globalIndex++);

		List<Point> uniqueResultList = new ArrayList<>();
		try {
			actualFront = new ArrayFront(actualFileName);
			globalReferenceFront = new ArrayFront(globalReferenceFrontNames.get(0));

			int numberOfElementsInActualFront = actualFront.getNumberOfPoints();
			int numberOfElementsInTmpFront = 0;
			Point tmpP;

			for (int j = 0; j < numberOfElementsInActualFront; j++) {
				nameSet.clear();
				nameSet.add(actualFileName);

				actualPoint = actualFront.getPoint(j);
				notUnique = false;
				InRF = false;

				for (int k = 0; k < globalReferenceFront.getNumberOfPoints(); k++) {
					tmpP = globalReferenceFront.getPoint(k);
					if (actualPoint.equals(tmpP)) {
						InRF = true;
						break;
					}
				}

				if (!InRF) {
					InRF = false;
					continue;
				}

				for (int i = 0; i < numberOfFiles; i++) {
					notUnique = false;
					String tmpName = nameList.get(i);
					if (!nameSet.contains(tmpName)) {

						nameSet.add(tmpName);
						newFront = new ArrayFront(tmpName);

						numberOfElementsInTmpFront = newFront.getNumberOfPoints();

						for (int k = 0; k < numberOfElementsInTmpFront; k++) {
							tmpPoint = newFront.getPoint(k);

							if (actualPoint.equals(tmpPoint)) {
								notUnique = true;
								break;
							}
						}
					}
					if(notUnique) {
						break;
					}
				}
				if (!notUnique) {
					uniqueResultList.add(actualPoint);
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		return uniqueResultList.size() / totalNumberOfPointsInReferenceFront;
	}

	@Override
	public String getName() {
		return "UniqueContribution";
	}

	@Override
	public String getDescription() {
		return "Unique Contribution to the globale Pareto Front Indicator";
	}

	@Override
	public boolean isTheLowerTheIndicatorValueTheBetter() {
		return false;
	}
}
