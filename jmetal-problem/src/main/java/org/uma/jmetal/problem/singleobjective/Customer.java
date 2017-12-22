package org.uma.jmetal.problem.singleobjective;

import java.util.List;
import java.util.ArrayList;

public class Customer {

	private int id;
	private int profit;
	private int numberOfRequests;
	private List<Integer> requirementList;
	
	public Customer() {
		this.requirementList = new ArrayList<>();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public double getProfit() {
		return profit;
	}
	
	public void setProfit(int profit) {
		this.profit = profit;
	}
	
	public int getNumberOfRequests() {
		return numberOfRequests;
	}
	
	public void setNumberOfRequests(int numberOfRequests) {
		this.numberOfRequests = numberOfRequests;
	}
	
	public Integer getFromRequirementList(int index) {
		return requirementList.get(index);
	}
	
	public void addToRequirementList(Integer value) {
		this.requirementList.add(value);
	}
	
	@Override
	public String toString() {
		return "Customer [id=" + id + ", profit=" + profit + ", numberOfRequests=" + numberOfRequests
				+ ", requirementList=" + requirementList + "]";
	}
}
