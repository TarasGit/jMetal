package org.uma.jmetal.util.experiment.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;


public class AllFrontFileNames {
	private static AllFrontFileNames instance = null;
	private List<String> frontFileNames = new ArrayList<>();
	public Set<String> setOfAllFileNames = new LinkedHashSet<>();
	public Set<String> referenceFront = new HashSet<>();
	public Multimap<String, Long> timeMap = ArrayListMultimap.create();
	public String actualTag = "";
	public int numberOfRuns = 0;
	
	public void put(String name) {
		frontFileNames.add(name);
	}

	public String get(int index) {
		return frontFileNames.get(index);
	}

	public String remove(int index) {
		return frontFileNames.remove(index);
	}

	public int size() {
		return frontFileNames.size();
	}

	public List<String> getAllFileNames() {
		return frontFileNames;
	}

	public void deleteAllFileNames() {
		int size = frontFileNames.size();
		for (int i = 0; i < size; i++) {
			remove(0);
		}
	}
	
	

	protected AllFrontFileNames() {
		// Exists only to defeat instantiation.
	}

	public static AllFrontFileNames getInstance() {
		if (instance == null) {
			instance = new AllFrontFileNames();
		}
		return instance;
	}

}
