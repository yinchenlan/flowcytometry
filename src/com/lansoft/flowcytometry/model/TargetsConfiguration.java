package com.lansoft.flowcytometry.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "targetsConfiguration")
public class TargetsConfiguration {
	private List<TargetGroup> groups;
	
	private String name;
	
	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TargetsConfiguration() {
		groups = new ArrayList<TargetGroup>();
	}
	
	@XmlElement(name = "targetGroup")
	public List<TargetGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<TargetGroup> groups) {
		this.groups = groups;
	}
}
