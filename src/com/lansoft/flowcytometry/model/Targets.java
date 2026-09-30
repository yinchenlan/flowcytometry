package com.lansoft.flowcytometry.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "targets")
@XmlAccessorType(XmlAccessType.FIELD)
public class Targets {
	@XmlElement(name = "target")
	private List<Target> targetsList;

	public List<Target> getTargetsList() {
		return targetsList;
	}

	public void setTargetsList(List<Target> targets) {
		this.targetsList = targets;
	}
	
}
