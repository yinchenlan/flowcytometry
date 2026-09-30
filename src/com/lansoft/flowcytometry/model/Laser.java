package com.lansoft.flowcytometry.model;

import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "laser")
@XmlType(propOrder = { "name", "wavelength", "detectorList"})
public class Laser {
	
	public static final Comparator<? super Laser> ASCENDING;
	
	static {
		ASCENDING = new Comparator<Laser>() {

			@Override
			public int compare(Laser o1, Laser o2) {
				return o1.wavelength - o2.wavelength;
			}
			
		};
	}
	
	private String name;
	private List<Detector> detectorList;
	private int wavelength;
	private transient int minWl;
	private transient int maxWl;
	
	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElementWrapper(name = "detectors")
	@XmlElement(name = "detector")
	public List<Detector> getDetectorList() {
		return detectorList;
	}
	public void setDetectorList(List<Detector> detectorList) {
		this.detectorList = detectorList;
	}
	
	@XmlAttribute(name = "wavelength")
	public int getWavelength() {
		return wavelength;
	}
	public void setWavelength(int wavelength) {
		this.wavelength = wavelength;
	}
	
	@XmlTransient
	public int getMinWl() {
		return minWl;
	}
	public void setMinWl(int minWl) {
		this.minWl = minWl;
	}
	
	@XmlTransient
	public int getMaxWl() {
		return maxWl;
	}
	public void setMaxWl(int maxWl) {
		this.maxWl = maxWl;
	}
	
	
}
