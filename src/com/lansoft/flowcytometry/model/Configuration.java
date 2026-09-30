package com.lansoft.flowcytometry.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "configuration")
@XmlType(propOrder = { "name", "description", "detectorConjugatesList", "brightnessIndices" })
public class Configuration {
	
	private String name;
	
	private String description;

	private List<DetectorConjugates> detectorConjugatesList;
	
	private List<BrightnessIndex> brightnessIndices;
	
	public Configuration() {
		detectorConjugatesList = new ArrayList<DetectorConjugates>();
		brightnessIndices = new ArrayList<BrightnessIndex>();
	}

	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElementWrapper( name="detectorConjugatesList" )
	@XmlElement(name = "detectorConjugates")
	public List<DetectorConjugates> getDetectorConjugatesList() {
		return detectorConjugatesList;
	}

	public void setDetectorConjugatesList(List<DetectorConjugates> detectorConjugatesList) {
		this.detectorConjugatesList = detectorConjugatesList;
	}
	
	@XmlElementWrapper( name="brightnessIndices" )
	@XmlElement(name = "brightnessIndex")
	public List<BrightnessIndex> getBrightnessIndices() {
		return brightnessIndices;
	}

	public void setBrightnessIndices(List<BrightnessIndex> brightnessIndices) {
		this.brightnessIndices = brightnessIndices;
	}

	@XmlElement(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Set<String> getAllConjugateNames() {
		Set<String> retVal = new HashSet<String>();
		List<DetectorConjugates> detectorConjugates = getDetectorConjugatesList();
		for (DetectorConjugates det : detectorConjugates) {
			retVal.addAll(det.getNameList());
		}
		return retVal;
	}
}
