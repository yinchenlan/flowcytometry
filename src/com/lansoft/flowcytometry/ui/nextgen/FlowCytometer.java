package com.lansoft.flowcytometry.ui.nextgen;
import java.io.Serializable;
import java.util.ArrayList;

public class FlowCytometer implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList <Detector> detectors;
	private String name;
	private int arrayLength;
	
	public FlowCytometer ( String n) {
		setName(n);
		detectors = new ArrayList <Detector> ();
		setArrayLength(0);
	}

	public ArrayList <Detector> getDetectors() {
		return detectors;
	}

	public void addDetector(Detector detector) {
		this.detectors.add( detector);
		setArrayLength(getArrayLength() + 1);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getArrayLength() {
		return arrayLength;
	}

	public void setArrayLength(int arrayLength) {
		this.arrayLength = arrayLength;
	}

}
