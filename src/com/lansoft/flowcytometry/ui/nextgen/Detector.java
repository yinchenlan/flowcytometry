package com.lansoft.flowcytometry.ui.nextgen;
/**
 * Detector contains a TreeMap of conjugates with its associated brightness.
 * A conjugate is String determined by its name, and the brighness is String
 * object as well.
 */


import java.io.Serializable;
import java.util.TreeMap;

public class Detector implements Serializable {

	/**
	 * conjugates is a TreeMap object that contains the conjugate and brightness associations.
	 */
	private static final long serialVersionUID = 1L;
	private TreeMap <String, String> conjugates;
	private String wavelength;
	private String name;
	
	public Detector (String n) {
		this.name = n;
		conjugates = new TreeMap<String, String> ();
	}
	
	public void addConjugate ( Object conjugate, Object brightness ) {
		conjugates.put( (String) conjugate, (String) brightness);
	}
	
	public TreeMap<String, String> getConjugate() {
		return conjugates;
	}

	public String getWavelength() {
		return wavelength;
	}

	public void setWavelength(String w) {
		this.wavelength = w;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
