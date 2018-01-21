package org.uma.jmetal.problem.singleobjective;

public interface NRP {	
	double getCostsOfRequirement(int x, int y);
	int getNumberOfBitInVariable(int index);
	double getDistanceProfit(int x, int y);
}
