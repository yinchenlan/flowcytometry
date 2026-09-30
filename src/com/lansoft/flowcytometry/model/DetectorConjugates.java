package com.lansoft.flowcytometry.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "detector")
@XmlType(propOrder = { "detectorName", "nameList" })
public class DetectorConjugates {
	private String detectorName;
	private List<String> nameList;
	
	public DetectorConjugates() {
		nameList = new ArrayList<String>();
	}
	
	public String getDetectorName() {
		return detectorName;
	}
	public void setDetectorName(String detectorName) {
		this.detectorName = detectorName;
	}
	@XmlElementWrapper(name = "names")
	// XmlElement sets the name of the entities
	@XmlElement(name = "name")
	public List<String> getNameList() {
		return nameList;
	}
	public void setNameList(List<String> nameList) {
		this.nameList = nameList;
	}
}
