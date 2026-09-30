package com.lansoft.flowcytometry.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "target")
@XmlType(propOrder = { "name", "density" })
public class Target {
	private String name;
	private int density;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDensity() {
		return density;
	}
	public void setDensity(int density) {
		this.density = density;
	}	
}
