package com.lansoft.flowcytometry.ui.nextgen;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is represents a target.  Each target has
 * density value.
 * @author jennychien
 */

public class Target implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5555009650611671388L;
	private int density;
	private String name;
	private String required;
	private ArrayList <String> group;
	
	public Target( String n, Integer d ) {
		setName(n);
		setDensity(d);
		group = new ArrayList <String> ();
	}

	public int getDensity() {
		return density;
	}

	public void setDensity(int density) {
		this.density = density;
	}

	public String getName() {
		return name;
	}

	public void setName(String n) {
		this.name = n;
	}

	public String isRequired() {
		return required;
	}

	public void setRequired(String s) {
		this.required = s;
	}

	public ArrayList <String> getGroup() {
		return group;
	}

	public void addGroup(String s) {
		this.group.add( s );
	}
	
	public void removeGroup(String s) {
		this.group.remove( s );
	}
	
}
