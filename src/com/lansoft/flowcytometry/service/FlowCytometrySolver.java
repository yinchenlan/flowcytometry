package com.lansoft.flowcytometry.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lansoft.flowcytometry.model.Antibodies;
import com.lansoft.flowcytometry.model.Antibody;
import com.lansoft.flowcytometry.model.BrightnessIndex;
import com.lansoft.flowcytometry.model.Configuration;
import com.lansoft.flowcytometry.model.Conjugate;
import com.lansoft.flowcytometry.model.DetectorConjugates;
import com.lansoft.flowcytometry.model.TargetDefinition;
import com.lansoft.flowcytometry.model.TargetGroup;
import com.lansoft.flowcytometry.ui.CalculationException;
import com.lansoft.flowcytometry.ui.TemperatureChangeListener;

public class FlowCytometrySolver {

	Set<String> allConjugateNames;
	private List<List<Integer>> swaps;
	private Map<Integer, Set<TargetGroup>> panelToGroups;
	/**
	 * Maps a target to a set of targets within the group that it belongs to
	 */
	private Map<String, Set<String>> targetToGroup;
	/**
	 * Maps a target to a Group
	 */
	private Map<String, Group> targetToGroup2;
	/**
	 * An index for each detector, with the value of a set that contains all 
	 * possible targets for that detector
	 */
	private List<Set<String>> possibleTargets;
	/**
	 * An index for each detector, with the value consisting of a map from 
	 * name of conjugate to the brightness
	 */
	private List<Map<String, Double>> brightnesses;
	private List<Map<String, Antibody>> possibleAntibodies;
	private Set<String> allTargets;
	private Map<String, Double> density;
	private int swapIdx1;
	private int swapIdx2;
	/**
	 * Penalize based on panel count size
	 */
	private boolean isPenalizePanelCount;
	/**
	 * Set of required targets
	 */
	private Set<String> required;
	/**
	 * Non required targets
	 */
	private Set<String> nonRequired;
	private TargetGroup requiredTargetGroup;
	private List<TargetGroup> targetGroups;
	private List<Antibodies> antibodyDatabases;
	private Configuration configuration;
	double temp;//default 10000
	double coolingRate; //default 0.000003
	TemperatureChangeListener temperatureChangeListener;
	/**
	 * Caches the score component to reduce CPU utilizations
	 */
	Map<Integer, Map<String, Long>> scoreCompCache;
	
	Map<String, Integer> targetIndex;
	/**
	 * The current score of the result
	 */
	long score;
	
	String[] result;
	
	int resultSize;
	
	/**
	 * To track which targets has been
	 * used in each panel
	 */
	Group[] panelToGroup;
	
	/**
	 * Track required target and assigned detectors.
	 * Used to ensure that required targets are 
	 * assigned to the same detectors across panels.
	 */
	Group[] panelToRequiredTargetAndDetector;
	
	public String[] getResult() {
		return result;
	}
	
	public void setResult(String[] result) {
		this.result = result;
	}

	public int getResultSize() {
		return resultSize;
	}

	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}

	/**
	 * The current score of the result
	 */
	public long getScore() {
		return score;
	}
	
	public void setScore(long score) {
		this.score = score;
	}

	public Comparator<String[]> comp = new Comparator<String[]>() {

		@Override
		public int compare(String[] o1, String[] o2) {
			int retVal = 0;
			double score1 = getScore(o1, false, 1, null);
			double score2 = getScore(o2, false, 1, null);
			if (score1 > score2) {
				retVal = 1;
			} else if (score1 < score2) {
				retVal = -1;
			}
			return retVal;
		}

	};

	public FlowCytometrySolver(
			TargetGroup requiredTargetGroup, 
			List<TargetGroup> targetGroups, 
			List<Antibodies> antibodyDatabases,
			Configuration configuration, 
			double temperature,
			double coolingRate,
			int queueSize,
			boolean isPenalizePanelCount) {
		this.isPenalizePanelCount = isPenalizePanelCount;
		//this.panelToTargets = new HashMap<Integer, Set<String>>();
		scoreCompCache = new HashMap<Integer, Map<String, Long>>();
		this.panelToGroups = new HashMap<Integer, Set<TargetGroup>>();
		this.swaps = new ArrayList<List<Integer>>();
		this.targetToGroup = new HashMap<String, Set<String>>();
		this.targetToGroup2 = new HashMap<String, Group>();
		this.nonRequired = new HashSet<String>();
		this.temp = temperature;
		this.coolingRate = coolingRate;
		this.requiredTargetGroup = requiredTargetGroup;
		this.targetGroups = targetGroups;
		this.antibodyDatabases = antibodyDatabases;
		this.configuration = configuration;
		validateTargetGroups(requiredTargetGroup, targetGroups);
		for (TargetGroup tg : targetGroups) {
			Set<String> targets = new HashSet<String>();
			for (TargetDefinition td : tg.getDefinitions()) {
				String targetId = td.getTargetId();
				targets.add(targetId);
			}
			for (String target : targets) {
				targetToGroup.put(target, targets);
			}
		}
		Set<String> targets = new HashSet<String>();
		// Do the same for required target
		for (TargetDefinition td : requiredTargetGroup.getDefinitions()) {
			String targetId = td.getTargetId();
			targets.add(targetId);
		}
		for (String target : targets) {
			targetToGroup.put(target, targets);
		}
		processAllTargetDefinitions();
		allTargets = new HashSet<String>();
		for (Set<String> s : possibleTargets) {
			allTargets.addAll(s);
		}
		// Instantiate targetIndex
		int targetIdx = 0;
		targetIndex = new HashMap<String, Integer>();
		for (String target : allTargets) {
			targetIndex.put(target, targetIdx++);
			if (!nonRequired.contains(target)) {
				nonRequired.add(target);
			}
		}
		
		for (TargetGroup tg : targetGroups) {
			Group g = new Group();
			Set<String> tgs = new HashSet<String>();
			for (TargetDefinition td : tg.getDefinitions()) {
				String targetId = td.getTargetId();
				tgs.add(targetId);
				g.addMember(targetIndex.get(targetId));
			}
			for (String target : tgs) {
				targetToGroup2.put(target, g);
			}
		}
		Group g2 = new Group();
		Set<String> targets2 = new HashSet<String>();
		// Do the same for required target
		for (TargetDefinition td : requiredTargetGroup.getDefinitions()) {
			String targetId = td.getTargetId();
			targets2.add(targetId);
			g2.addMember(targetIndex.get(targetId));
		}
		for (String target : targets2) {
			targetToGroup2.put(target, g2);
		}				
	}
	
	/**
	 * Validates that target definitions are properly defined.  For example,
	 * ensure that assigned conjugates do not fall in same detector.
	 * 
	 * @param requiredTargetGroup
	 * @param targetGroups
	 * @throws CalculationException
	 */
	private void validateTargetGroups(TargetGroup requiredTargetGroup,
			List<TargetGroup> targetGroups) throws CalculationException {
		// TODO Auto-generated method stub
		
	}

	public String[] getInitialSolutionSet(Set<String> startingTargets,
			Set<String> required, Group[] panelGroupMembers) {
		String[] retVal = new String[possibleTargets.size() * getNumPanels()];
		Set<String> rts = new HashSet<String>();
		int detSize = possibleTargets.size();
		int numPanels = retVal.length / detSize;
		Set<TargetGroup> tgs = new HashSet<TargetGroup>(targetGroups);
		// insert required group
		if (requiredTargetGroup != null && requiredTargetGroup.getTargets().size() > 0) {
			for (int idx = 0; idx < numPanels; idx++) {
				int cnt = 0;
				while(!insertGroup(retVal, requiredTargetGroup, idx)) {
					cnt++;
					if (cnt == 5) {
						throw new CalculationException("5 retries attempted to insert required target group.");
					}
				}
				for (String target: requiredTargetGroup.getTargets()) {
					panelGroupMembers[idx].addMember(targetIndex.get(target));
				}
			}
		}
		
		for (int idx = 0; idx < numPanels; idx++) {
			addToPanelGroups(idx, null);
			boolean isInserted = false;
			Set<TargetGroup> removed = new HashSet<TargetGroup>();
			for (TargetGroup tg : tgs) {
				isInserted = insertGroup(retVal, tg, idx);
				if (isInserted) {
					addToPanelGroups(idx, tg);
					removed.add(tg);
					for (String target: tg.getTargets()) {
						panelGroupMembers[idx].addMember(targetIndex.get(target));
					}
				}
			}
			tgs.removeAll(removed);
		}
		return retVal;
	}

	public void change(String[] input, Set<String> required, int size, Group[] panelGroupMembers) {
		int pos1 = (int) (size * Math.random());
		int setSize = possibleTargets.size();
		List<Integer> pd = new ArrayList<Integer>(getPossibleDestinations(input, pos1, required, size));
		Collections.shuffle(pd);
		for (Integer pos : pd) {
			if (input[pos1] == null && input[pos] == null) continue;
			Set<Integer> pd2 = getPossibleDestinations(input, pos, required, size);
			if (pd2.contains(pos1)) {
				swap(input, pos1, pos, setSize, panelGroupMembers);
				swapIdx1 = pos1;
				swapIdx2 = pos;
				break;
			}
		}
	}
	
	public void addToPanelGroups(int idx, TargetGroup targetGroup) {
		Set<TargetGroup> targetGroups = panelToGroups.get(idx);
		if (targetGroups == null) {
			targetGroups = new HashSet<TargetGroup>();
			panelToGroups.put(idx, targetGroups);
		}
		if (targetGroup != null) {
			targetGroups.add(targetGroup);	
		}
	}
	
	public void removeFromPaneGroups(int idx, TargetGroup targetGroup) {
		Set<TargetGroup> targetGroups = panelToGroups.get(idx);
		if (targetGroups != null) {
			targetGroups.remove(targetGroup);		
		}
	}
	
	private boolean insertGroup(String[] input, TargetGroup targetGroup, int destPanelIdx) {
		String[] clone = Arrays.copyOf(input, input.length);
		boolean retVal = false;
		Set<String> targets = targetGroup.getTargets();
		for (String target : targets) {
			List<Integer> pd = 
					getPossibleDestinations(clone, target, destPanelIdx);
			if (pd.size() > 0) {
				int dest = pd.get((int) (Math.random() * pd.size()));
				clone[dest] = target;
				retVal = true;
			} else {
				retVal = false;
				break;
			}
		}
		if (retVal) {
			for (int idx = 0; idx < input.length; idx++) {
				input[idx] = clone[idx];
			}
		}
		return retVal;
	}

	public List<Integer> getPossibleDestinations(
			String[] input, String target, int srcPanelIdx, int destPanelIdx) {
		List<Integer> retVal = new ArrayList<Integer>();
		int panelSize = possibleTargets.size();
		// inclusive
		int startPos = destPanelIdx * panelSize;
		// exclusive
		int endPos = (destPanelIdx + 1) * panelSize; 
		for (int idx = startPos; idx < endPos; idx++) {
			Set<String> pt = possibleTargets.get(idx % panelSize);
			if (pt.contains(target)) {
				if (!required.contains(target) || srcPanelIdx == destPanelIdx) {
					int destIdx = idx;
					if (required.contains(input[destIdx]) && srcPanelIdx != destPanelIdx) {
						continue;
					} else {
						retVal.add(destIdx);
					}
				}
			}
		}
		return retVal;
	}
	
	public List<Integer> getPossibleDestinations(
			String[] input, String target, int destPanelIdx) {
		List<Integer> retVal = new ArrayList<Integer>();
		int panelSize = possibleTargets.size();
		// inclusive
		int startPos = destPanelIdx * panelSize;
		// exclusive
		int endPos = (destPanelIdx + 1) * panelSize; 
		for (int idx = startPos; idx < endPos; idx++) {
			if (input[idx] != null) {
				continue;
			}
			Set<String> pt = possibleTargets.get(idx % possibleTargets.size());
			if (pt.contains(target)) {				
				retVal.add(idx);
			}
		}
		return retVal;
	}

	public int getTargetIndex(String target) {
		if (target == null) {
			return -1;
		} else {
			return targetIndex.get(target);
		}
	}
	
	private void swap(String[] input, int origPos, int newPos, int setSize, Group[] panelGroupMembers) {
		int origPanelIdx = getPanelIdx(origPos);
		int newPanelIdx = getPanelIdx(newPos);
		String temp = input[origPos];
		int origTargetIdx = getTargetIndex(temp);
		int newTargetIdx = getTargetIndex(input[newPos]);
		input[origPos] = input[newPos];
		input[newPos] = temp;
		if (origPanelIdx != newPanelIdx) {
			if (newTargetIdx > -1) {
				panelGroupMembers[origPanelIdx].addMember(newTargetIdx);
				panelGroupMembers[newPanelIdx].removeMember(newTargetIdx);
			}
			if (origTargetIdx > -1) {
				panelGroupMembers[origPanelIdx].removeMember(origTargetIdx);
				panelGroupMembers[newPanelIdx].addMember(origTargetIdx);
			}
		}
	}
	
	private int getPanelIdx(int origPos) {
		return origPos / possibleTargets.size();
	}

	public Set<Integer> getPossibleDestinations(String[] input, int currPos, Set<String> required, int size) {
		Set<Integer> retVal = new HashSet<Integer>();
		int setSize = possibleTargets.size();
		int currSetIdx = currPos/setSize;
		int numPanels = size / setSize;
		for (int didx = 0; didx < setSize; didx++) {
			Set<String> pt = possibleTargets.get(didx);
			if (input[currPos] == null || pt.contains(input[currPos])) {
				for (int idx2 = 0; idx2 < numPanels; idx2++) {
					if (!required.contains(input[currPos]) || currSetIdx == idx2) {
						int destIdx = didx + idx2 * setSize;
						if (required.contains(input[destIdx]) && currSetIdx != idx2) {
							continue;
						} else if (destIdx != currPos) {
							retVal.add(destIdx);
						}
					}
				}
			}
		}
		return retVal;
	}
	
	public Set<String> getTargetIds(TargetGroup tg) {
		Set<String> targetIds = new HashSet<String>();
		for (TargetDefinition td : tg.getDefinitions()) {
			targetIds.add(td.getTargetId());
		}
		return targetIds;
	}
	
	public Set<String> getTargetsByPanel(String[] targets, int panelIdx) {
		int detSize = possibleTargets.size();
		Set<String> retVal = new HashSet<String>();
		for (int idx = detSize * panelIdx; idx < detSize * (panelIdx + 1); idx++) {
			String target = targets[idx];
			if (target != null) {
				retVal.add(target);
			}
		}
		return retVal;
	}
	
	public boolean isTargetGroupInPanel(String[] targets, int panelIdx, Set<String> targetGroup) {
		int detSize = possibleTargets.size();
		int initSize = targetGroup.size();
		for (int idx = detSize * panelIdx; idx < detSize * (panelIdx + 1); idx++) {
			String target = targets[idx];
			if (target != null) {
				if (targetGroup.contains(target)) {
					initSize--;
				}
				if (initSize == 0) {
					break;
				}
			}
		}
		return initSize == 0;
	}
	
	public boolean isTargetGroupInPanel2(int panelIdx, Group[] panelGroupMembers, Group panelGroupMembers2) {
		return panelGroupMembers[panelIdx].containsGroup(panelGroupMembers2);
	}
		
	/**
	 * Scores target allocation across panels.
	 * 
	 * @param targets
	 * @param printFormula
	 * @param penaltyFactor
	 * @param panelGroupMembers TODO
	 * @return
	 */
	public long getScore(String[] targets, boolean printFormula, int penaltyFactor, Group[] panelGroupMembers) {
		long score = 0;
		int detSize = possibleTargets.size();
		Map<String, Set<Integer>> targetLocations = new HashMap<String, Set<Integer>>();
		int panelId = -1;
		long factor = 1;
		long variance = getVariance(targets);
		for (int idx = 0; idx < targets.length; idx++) {
			int relativePosition = idx % detSize;
			if (required.contains(targets[idx])) {
				Set<Integer> locations = targetLocations.get(targets[idx]);
				if (locations == null) {
					locations = new HashSet<Integer>();
					targetLocations.put(targets[idx], locations);
				}
				locations.add(relativePosition);
			}
			int currPanelId = idx / detSize;
			if (currPanelId != panelId) {
				if (!isPenalizePanelCount) {
					factor = factor * detSize;
				}
				panelId = currPanelId;
			}
			String target = targets[idx];
			Double br = 0d;
			if (target != null) {
				br = (double) brightnesses.get(relativePosition).get(target);
				boolean panelContainsTargets = 
						isTargetGroupInPanel2(panelId, panelGroupMembers, targetToGroup2.get(target));
				Map<String, Long> pCache = scoreCompCache.get(relativePosition);
				if (pCache == null) {
					pCache = new HashMap<String, Long>();
					scoreCompCache.put(relativePosition, pCache);
				}
				Long scoreComp = pCache.get(target);
				if (scoreComp == null) {
					scoreComp = 10 + (long) Math.pow(Math.abs(10 - (br + density.get(target))), 2);	
					pCache.put(target, scoreComp);
				}
				if (possibleTargets.get(relativePosition).contains(target) && panelContainsTargets) {
					score = score + factor * scoreComp;
					if (printFormula) {
						System.out
								.println("||brightness = "
										+ br
										+ ", density = " + density.get(target)
										+ ", score = " + score + "||");
					}
				} else {
					score = score + factor * detSize * scoreComp;
				}
			}
		}
		score += factor * variance * 10;
		return score;
	}

	private long getVariance(String[] targets) {
		int panelSize = possibleTargets.size();
		Map<String, Set<Integer>> uniqueCount = new HashMap<String, Set<Integer>>();
		for (int idx = 0; idx < targets.length; idx++) {
			if (required.contains(targets[idx])) {
				int index = idx % panelSize;
				Set<Integer> s = uniqueCount.get(targets[idx]);
				if (s == null) {
					s = new HashSet<Integer>();
					uniqueCount.put(targets[idx], s);
				}
				s.add(index);
			}
		}
		int retVal = 0;
		for (String target : uniqueCount.keySet()) {
			retVal += uniqueCount.get(target).size() - 1;
		}
		return retVal;
	}

	public double acceptanceProbability(long energy, long newEnergy,
			double temperature) {
		// If the new solution is better, accept it
		if (newEnergy < energy) {
			return 1.0;
		}
		// If the new solution is worse, calculate an acceptance probability
		return Math.exp((energy - newEnergy) / temperature);
	}

	public int getNumPanels() {
		int retVal = 0;
		for (Set<String> tgs : possibleTargets) {
			int size = tgs.size();
			if (size > retVal)
				retVal = size;
		}
		return retVal;
	}

	public boolean isValidSolution(String[] input) {		
		// Check for group allocation correctness
		List<TargetGroup> tgs = new ArrayList<TargetGroup>(targetGroups);
		tgs.add(requiredTargetGroup);
		int groupCount = tgs.size();
		for(TargetGroup tg : tgs) {
			int panelSize = possibleTargets.size();
			int numPanels = input.length / panelSize;
			for (int idx = 0; idx < numPanels; idx++) {
				Set<String> targetsInGroup = tg.getTargets();
				int size = targetsInGroup.size();
				for (int idx2 = idx * panelSize; idx2 < panelSize * (idx + 1); idx2++) {
					targetsInGroup.remove(input[idx2]);
				}
				if (targetsInGroup.size() == 0) {
					groupCount--;
					break;
				} else if (targetsInGroup.size() == size) {
					continue;
				} else {
					return false;
				}
			}
		}
		if (groupCount > 0) {
			return false;
		}
		// Check for target allocation correctness
		for (int idx = 0; idx < input.length; idx++) {
			String target = input[idx];
			if (target != null) {
				int panelIdx = idx % possibleTargets.size();
				if (!possibleTargets.get(panelIdx).contains(target)) {
					return false;
				}
			}
		}		
		return true;
	}
	
	public List<List<Antibody>> solve() {
		Set<String> initialTargets = getTargets();
		Group[] panelGroupMembers = getGroupMemberPanels();
		String[] start = getInitialSolutionSet(initialTargets, required, panelGroupMembers);
		int size = getInputSize(start);
		return solve(start, size);
	}
	
	public List<List<Antibody>> solve(String[] curr, int size) {
		int penaltyFactor = 2;
		Set<String> initialTargets = getTargets();
		if (!isSolvable()) {
			return new ArrayList<List<Antibody>>();
		}
		double startingTemp = temp;
		String[] best = null;

		int retryCount = 20;
		Group[] panelGroupMembers = getGroupMemberPanels(curr);

		best = Arrays.copyOf(curr, curr.length);
		boolean isEnergyChanged = true;
		long currentEnergy = 0;
		temp = startingTemp;
		double lastTemp = temp;
		double maxTemp = temp;
		
		while (temp > 1) {
			if (isEnergyChanged) {
				// Get energy of solutions
				currentEnergy = getScore(curr, false, penaltyFactor, panelGroupMembers);
				isEnergyChanged = false;
			}
			change(curr, required, size, panelGroupMembers);
			
			long neighbourEnergy = getScore(curr, false, penaltyFactor, panelGroupMembers);

			// Decide if we should accept the neighbour
			if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math
					.random()) {
				isEnergyChanged = true;
				//q.add(Arrays.copyOf(curr, curr.length));
			} else {
				swap(curr, panelGroupMembers);
			}

			// Keep track of the best solution found
			long sc = getScore(best, false, penaltyFactor, panelGroupMembers);
			if (currentEnergy < sc) {
				best = Arrays.copyOf(curr, curr.length);
			}

			// Cool system
			temp *= 1 - coolingRate;
			// call temperature change listener on every 1% change or greater
			if (temperatureChangeListener != null && Math.abs((lastTemp - temp) / maxTemp) >= .01) {
				temperatureChangeListener.onTemperatureChange(temp / maxTemp);
				lastTemp = temp;
			}
			if (temp <= 1 && retryCount-- > 0) {
				//if solution is not valid, raise temp and continue annealing
				if (!isValidSolution(best) || retryCount > 20) {
					size = getInputSize(curr);
					penaltyFactor = 1;
					temp = startingTemp;
					/*curr = getInitialSolutionSet(initialTargets, required);
					best = Arrays.copyOf(curr, curr.length);*/
					if (!isValidSolution(best) && size < curr.length) {
						panelGroupMembers = getGroupMemberPanels(curr);
						curr = getInitialSolutionSet(initialTargets, required, panelGroupMembers);
						best = Arrays.copyOf(curr, curr.length);
						size = size + possibleTargets.size();
					}
				}
			}
			if (size > curr.length || retryCount == 0) {
					throw new CalculationException("20 retries attempted, no solution found.");
			}
		}
		best = removeExtraResults(best);
		this.result = Arrays.copyOf(best, best.length);
		size = best.length;
		this.resultSize = size;
		this.score = getScore(best, false, penaltyFactor, panelGroupMembers);
		this.panelToGroup = panelGroupMembers;
		System.out.println("Score = " + this.score + ", size = " + size);
		List<List<Antibody>> retVal = convertToAntibodiesList(best);
		return retVal;
	}
	
	private Group[] getGroupMemberPanels(String[] curr) {
		Group[] retVal = new Group[curr.length / possibleTargets.size()];
		for (int idx = 0; idx < retVal.length; idx++) {
			retVal[idx] = new Group();
			for (int i = idx * possibleTargets.size(); i < possibleTargets.size() * (idx + 1); i++) {
				String target = curr[i];
				if (target != null) {
					int ti = targetIndex.get(target);
					retVal[idx].addMember(ti);
				}
			}
		}
		return retVal;
	}
	
	private Group[] getTargetDetectorMemberPanels(String[] curr) {
		Group[] retVal = new Group[curr.length / possibleTargets.size()];
		for (int idx = 0; idx < retVal.length; idx++) {
			retVal[idx] = new Group();
			for (int i = idx * possibleTargets.size(); i < possibleTargets.size() * (idx + 1); i++) {
				String target = curr[i];
				if (target != null) {
					int ti = targetIndex.get(target) + 1;
					int di = i % possibleTargets.size() + 1;
					retVal[idx].addMember(ti * di);
				}
			}
		}
		return retVal;
	}
	
	private Group[] getGroupMemberPanels() {
		Group[] retVal = new Group[getNumPanels()];
		for (int idx = 0; idx < retVal.length; idx++) {
			retVal[idx] = new Group();
		}
		return retVal;
	}

	private List<List<Antibody>> convertToAntibodiesList(String[] best) {
		int numDetectors = configuration.getDetectorConjugatesList().size();
		List<List<Antibody>> retVal = new ArrayList<List<Antibody>>();
		for (int setIdx = 0; setIdx < best.length / numDetectors; setIdx++) {
			List<Antibody> abList = new ArrayList<Antibody>();
			retVal.add(abList);
			for (int idx = setIdx * possibleTargets.size(); idx < possibleTargets.size() * (setIdx + 1); idx++) {
				String target = best[idx];
				if (target != null) {
					Map<String, Antibody> pa = possibleAntibodies.get(idx % numDetectors);
					Antibody ab = (Antibody) pa.get(target).clone();
					if (required.contains(target)) {
						ab.setRequired(true);
					} else {
						ab.setRequired(false);
					}
					ab.setBrightness(brightnesses.get(idx % numDetectors).get(target));
					ab.setDensity(density.get(target));
					abList.add(ab);
				}
			}
		}
		return retVal;
	}

	private String[] removeExtraResults(String[] result) {
		String[] newResult = new String[result.length];
		int requiredSize = required.size();
		int panelSize = possibleTargets.size();
		int numPanels = result.length / panelSize;
		int destIdx = 0;
		for (int idx = 0; idx < numPanels; idx++) {
			int targetCount = 0;
			for (int idx2 = idx * panelSize; idx2 < (idx + 1) * panelSize; idx2++) {
				if (result[idx2] != null) {
					targetCount++;
				}
			}
			if (idx != 0 && targetCount == requiredSize) {
				continue;
			} else {
				for (int idx2 = idx * panelSize; idx2 < (idx + 1) * panelSize; idx2++) {
					newResult[destIdx++] = result[idx2];
				}
			}			
		}
		int size = getInputSize(newResult);
		if (size > -1)
			return Arrays.copyOf(newResult, size);
		else return newResult;
	}
	
	private int getInputSize(String[] result) {
		int size = result.length;
		int possibleTargetSize = possibleTargets.size();
		if (result.length == possibleTargetSize) {
			return size;
		}
		outer:for (int setIdx = 1; setIdx < getNumPanels(); setIdx++) {
			boolean remove = true;
			for (int idx = setIdx * possibleTargetSize; idx < possibleTargetSize * (setIdx + 1); idx++) {
				if (idx >= result.length) {
					break outer;
				}
				if (result[idx] != null && !required.contains(result[idx])) {
					remove = false;
					break;
				}
			}
			if (remove) {
				size = possibleTargetSize * setIdx;
				break;
			}
		}
		return size;
	}
	
	private void swap(String[] curr, Group[] panelGroupMembers) {
		int origPanelIdx = getPanelIdx(swapIdx1);
		int newPanelIdx = getPanelIdx(swapIdx2);
		String temp = curr[swapIdx1];
		int origTargetIdx = getTargetIndex(temp);
		int newTargetIdx = getTargetIndex(curr[swapIdx2]);
		curr[swapIdx1] = curr[swapIdx2];
		curr[swapIdx2] = temp;
		if (origPanelIdx != newPanelIdx) {
			if (newTargetIdx > -1) {
				panelGroupMembers[origPanelIdx].addMember(newTargetIdx);
				panelGroupMembers[newPanelIdx].removeMember(newTargetIdx);
			}
			if (origTargetIdx > -1) {
				panelGroupMembers[origPanelIdx].removeMember(origTargetIdx);
				panelGroupMembers[newPanelIdx].addMember(origTargetIdx);
			}
		}
	}

	/**
	 * A solution is impossible if the following conditions exist: 1. A member
	 * REQ1 of the required set can be placed into only one detector DET1. 2. A
	 * member of the initial target set can be placed into only one detector
	 * DET1.  3.  Required members do not belong to the same detector.
	 * 
	 * @param initialTargets
	 * @param required
	 * @return
	 */
	public boolean isSolvable() {
		Set<String> initialTargets = getTargets();
		int possibleTargetSize = possibleTargets.size();
		if (!(allTargets.containsAll(initialTargets) && allTargets
				.containsAll(required))) {
			throw new CalculationException("Not all conjugates are assigned to a detector.");
		}
		Set<Integer> uniqueIndices = new HashSet<Integer>();
		// Identify required set members that can only go in one detector.
		for (String req : required) {
			int cnt = 0;
			int idx = -1;
			for (int detIdx = 0; detIdx < possibleTargetSize; detIdx++) {
				Set<String> ps = possibleTargets.get(detIdx);
				if (ps.contains(req)) {
					cnt++;
					idx = detIdx;
				}
			}
			if (cnt == 1) {
				// Required member occupies the same detector as an existing member.
				if (uniqueIndices.contains(idx)) {
					throw new CalculationException("Target '" + req + "' is allocated to an unavailable detector.");
				} else {
					uniqueIndices.add(idx);
				}
			}
		}
		for (TargetGroup tg : targetGroups) {
			for (String req : tg.getTargets()) {
				int cnt = 0;
				int idx = -1;
				for (int detIdx = 0; detIdx < possibleTargetSize; detIdx++) {
					Set<String> ps = possibleTargets.get(detIdx);
					if (ps.contains(req)) {
						cnt++;
						idx = detIdx;
					}
				}
				if (cnt == 1) {
					// Required member occupies the same detector as an existing member.
					if (uniqueIndices.contains(idx)) {
						throw new CalculationException("Target '" + req + "' is allocated to an unavailable detector.");
					} else {
						uniqueIndices.add(idx);
					}
				}
			}
		}
		// Check to see if any groups have number of targets > number of detectors
		
		for (TargetGroup tg : targetGroups) {
			if (tg.getDefinitions().size() > possibleTargets.size()) {
				throw new CalculationException("Number of targets is greater than number of detectors.");
			}
		}
		return true;
	}
	
	public List<TargetDefinition> getTargetDefinitions() {
		List<TargetDefinition> defs = new ArrayList<TargetDefinition>();
		defs.addAll(requiredTargetGroup.getDefinitions());
		for (TargetGroup targetGroup : targetGroups) {
			defs.addAll(targetGroup.getDefinitions());
		}
		return defs;
	}
	
	public void validateNewTarget(TargetDefinition td) {
		if (!allConjugateNames.contains(td.getConjugateId())) {
			throw new CalculationException("For target " + td.getTargetId() + 
					", conjugate " + td.getConjugateId() + " is undefined for all detectors."); 
		}
	}
	
	/**
	 * Allocate target ids across the detectors
	 */
	private void processAllTargetDefinitions() {
		init();
		populateDensity();
		this.required = new HashSet<String>(getRequiredTargets());
		List<TargetDefinition> defs = getTargetDefinitions();
		List<Antibody> antibodies = getAntibodies();
		List<DetectorConjugates> detectors = configuration.getDetectorConjugatesList();
		Map<String, Double> brightnessIndices = getBrightnessIndices();
		Map<Integer, String> requiredConjugate = new HashMap<Integer, String>();
		for (int detectorIdx = 0; detectorIdx < detectors.size(); detectorIdx++) {
			if (requiredConjugate.get(detectorIdx) != null) {
				continue;
			}
			DetectorConjugates detector = detectors.get(detectorIdx);
			Set<String> detectorConjugateNames = new HashSet<String>(detector.getNameList());
			for (Antibody ab : antibodies) {				
				String abConjugateName = ab.getConjugateName();
				for (TargetDefinition td : defs) {
					String abTargetName = null;
					if (td.isTargetMolecule()) {
						abTargetName = ab.getTargetName();
					} else {
						abTargetName = ab.getClone();
					}
					if (td.getTargetId().equals(abTargetName) && requiredConjugate.get(detectorIdx) == null) {
						if (td.getConjugateId().equals(Conjugate.CONJUGATE_AUTO_SELECT)) {
							if (detectorConjugateNames.contains(abConjugateName)) {
								possibleTargets.get(detectorIdx).add(abTargetName);
								//possibleAntibodies.get(detectorIdx).put(abTargetName, ab);
								Double currentBrightness = brightnesses.get(detectorIdx).get(abTargetName);
								float density = td.getDensity();
								long currScore = Long.MAX_VALUE;
								if (currentBrightness != null) {
									currScore = (long) Math.abs(10 - (density + currentBrightness));
								}
								Double antibodyBrightness = brightnessIndices.get(abConjugateName);
								//TODO: Default brightness if none was specified
								if (antibodyBrightness == null) antibodyBrightness = new Double(5);
								long abScore = (long) Math.abs(10 - (density + antibodyBrightness));
								
								if (currentBrightness == null || abScore < currScore) {
									brightnesses.get(detectorIdx).put(abTargetName, antibodyBrightness);
									possibleAntibodies.get(detectorIdx).put(abTargetName, ab);
								}
							} else {/*
								Double currentBrightness = brightnesses.get(detectorIdx).get(abTargetName);
								Double antibodyBrightness = brightnessIndices.get(abConjugateName);
								//TODO: Default brightness if none was specified
								if (antibodyBrightness == null) antibodyBrightness = new Double(5);
								if (currentBrightness == null || antibodyBrightness > currentBrightness) {
									brightnesses.get(detectorIdx).put(abTargetName, antibodyBrightness);
								}*/
							}							
						} else { // Inclue possible target only if conjugate on target definition matches
							// that on the antibody.
							if (td.getConjugateId().equals(abConjugateName)) {
								Double antibodyBrightness = brightnessIndices.get(abConjugateName);
								brightnesses.get(detectorIdx).put(abTargetName, antibodyBrightness);
								if (detectorConjugateNames.contains(abConjugateName)) {
									possibleTargets.get(detectorIdx).clear();
									possibleTargets.get(detectorIdx).add(abTargetName);
									requiredConjugate.put(detectorIdx, abConjugateName);
									possibleAntibodies.get(detectorIdx).put(abTargetName, ab);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private Set<String> getAllConjugateNames() {
		Set<String> retVal = new HashSet<String>();
		List<DetectorConjugates> detectors = configuration.getDetectorConjugatesList();
		for (DetectorConjugates det : detectors) {
			retVal.addAll(det.getNameList());
		}
		return retVal;
	}

	public void init() {
		possibleTargets = new ArrayList<Set<String>>();
		brightnesses = new ArrayList<Map<String, Double>>();
		possibleAntibodies = new ArrayList<Map<String, Antibody>>();
		for (int idx = 0; idx < configuration.getDetectorConjugatesList().size(); idx++) {
			possibleTargets.add(new HashSet<String>());
			brightnesses.add(new HashMap<String, Double>());
			possibleAntibodies.add(new HashMap<String, Antibody>());
		}
		allConjugateNames = getAllConjugateNames();
	}
	
	public void populateDensity() {
		density = new HashMap<String, Double>();
		for (TargetDefinition td: getTargetDefinitions()) {
			density.put(td.getTargetId(), (double) td.getDensity());
		}
		for (TargetDefinition td: getRequiredTargetDefinitions()) {
			density.put(td.getTargetId(), (double) td.getDensity());
		}
	}

	private List<TargetDefinition> getRequiredTargetDefinitions() {
		List<TargetDefinition> defs = requiredTargetGroup.getDefinitions();
		return defs;
	}

	private Map<String, Double> getBrightnessIndices() {
		Map<String, Double> retVal = new HashMap<String, Double>();
		List<BrightnessIndex> bis = configuration.getBrightnessIndices();
		for (BrightnessIndex bi : bis) {
			retVal.put(bi.getConjugateName(), (double) bi.getBrightness());
		}
		return retVal;
	}

	private List<Antibody> getAntibodies() {
		List<Antibody> retVal = new ArrayList<Antibody>();
		for (Antibodies ab :antibodyDatabases) {
			retVal.addAll(ab.getAntibodiesList());
		}
		return retVal;
	}
	
	private Set<String> getRequiredTargets() {
		Set<String> retVal = new HashSet<String>();
		for (TargetDefinition td : requiredTargetGroup.getDefinitions()) {
			retVal.add(td.getTargetId());			
		}
		return retVal;
	}
	
	private Set<String> getTargets() {
		Set<String> retVal = new HashSet<String>();
		for (TargetGroup targetGroup : targetGroups) {
			for (TargetDefinition td : targetGroup.getDefinitions()) {
				retVal.add(td.getTargetId());			
			}
		}
		return retVal;
	}
		
	public void prettyPrint() {
		String[] arr = result;
		arr = removeExtraResults(arr);
		boolean first = true;
		for (int idx = 0; idx < arr.length; idx++) {
			if (!first) {
				if (idx % possibleTargets.size() == 0) {
					System.out.print(") (");
				} else {
					System.out.print(", ");
				}
			} else {
				System.out.print("(");
				first = false;
			}
			System.out.print(arr[idx]);
		}
		System.out.println(") ==> Score : " + getScore(arr, false, 1, panelToGroup));
	}
	
	public TemperatureChangeListener getTemperatureChangeListener() {
		return temperatureChangeListener;
	}

	public void setTemperatureChangeListener(
			TemperatureChangeListener temperatureChangeListener) {
		this.temperatureChangeListener = temperatureChangeListener;
	}

	public static void main(String[] args) {
		// Setup configuration
		Configuration configuration = new Configuration();
		DetectorConjugates detector1 = new DetectorConjugates();
		detector1.setDetectorName("Detector 1");
		detector1.getNameList().add("red");
		configuration.getDetectorConjugatesList().add(detector1);
		DetectorConjugates detector2 = new DetectorConjugates();
		detector2.setDetectorName("Detector 2");
		detector2.getNameList().add("blue");
		configuration.getDetectorConjugatesList().add(detector2);
		DetectorConjugates detector3 = new DetectorConjugates();
		detector3.setDetectorName("Detector 3");
		detector3.getNameList().add("yellow");
		configuration.getDetectorConjugatesList().add(detector3);
		BrightnessIndex bi1 = new BrightnessIndex();
		bi1.setConjugateName("red");
		bi1.setBrightness(.1F);
		configuration.getBrightnessIndices().add(bi1);
		BrightnessIndex bi2 = new BrightnessIndex();
		bi2.setConjugateName("blue");
		bi2.setBrightness(.4F);
		configuration.getBrightnessIndices().add(bi2);
		BrightnessIndex bi3 = new BrightnessIndex();
		bi3.setConjugateName("yellow");
		bi3.setBrightness(.8F);
		configuration.getBrightnessIndices().add(bi3);
		
		// Setup antibody database
		List<Antibodies> antibodyDatabases = new ArrayList<Antibodies>();
		Antibodies ab = new Antibodies();
		ab.setName("DB 1");
		Antibody antibody = new Antibody();
		antibody.setTargetName("A");
		antibody.setConjugateName("red");
		ab.getAntibodiesList().add(antibody);
		Antibody antibody2 = new Antibody();
		antibody2.setTargetName("A");
		antibody2.setConjugateName("yellow");
		ab.getAntibodiesList().add(antibody2);
		Antibody antibody3 = new Antibody();
		antibody3.setTargetName("B");
		antibody3.setConjugateName("red");
		ab.getAntibodiesList().add(antibody3);
		Antibody antibody4 = new Antibody();
		antibody4.setTargetName("B");
		antibody4.setConjugateName("blue");
		ab.getAntibodiesList().add(antibody4);
		Antibody antibody5 = new Antibody();
		antibody5.setTargetName("C");
		antibody5.setConjugateName("blue");
		ab.getAntibodiesList().add(antibody5);
		/*Antibody antibody6 = new Antibody();
		antibody6.setTargetName("C");
		antibody6.setConjugateName("yellow");
		ab.getAntibodiesList().add(antibody6);*/
		antibodyDatabases.add(ab);
		
		List<TargetGroup> targetGroups = new ArrayList<TargetGroup>();
		
		// Setup target group
		TargetGroup targetGroup = new TargetGroup();
		targetGroup.setName("Target Group 1");
		TargetDefinition td = new TargetDefinition();
		td.setTargetMolecule(true);
		td.setTargetId("A");
		td.setConjugateId(Conjugate.CONJUGATE_AUTO_SELECT);
		td.setDensity(.9F);
		targetGroup.getDefinitions().add(td);
		TargetDefinition td2 = new TargetDefinition();
		td2.setTargetMolecule(true);
		td2.setTargetId("B");
		td2.setConjugateId(Conjugate.CONJUGATE_AUTO_SELECT);
		td2.setDensity(.4F);
		targetGroup.getDefinitions().add(td2);
		targetGroups.add(targetGroup);
		
		TargetGroup targetGroup2 = new TargetGroup();
		TargetDefinition td3 = new TargetDefinition();
		td3.setTargetMolecule(true);
		td3.setTargetId("C");
		td3.setConjugateId(Conjugate.CONJUGATE_AUTO_SELECT);
		td3.setDensity(.2F);
		targetGroup2.getDefinitions().add(td3);	
		targetGroups.add(targetGroup2);
		
		
		TargetGroup requiredTargetGroup = new TargetGroup();
		FlowCytometrySolver fcs = new FlowCytometrySolver(
				requiredTargetGroup, 
				targetGroups, antibodyDatabases,
				configuration, 10000d, 0.0003d, 5, true);
		System.out.println(fcs.solve());
		fcs.prettyPrint();
	}
}
