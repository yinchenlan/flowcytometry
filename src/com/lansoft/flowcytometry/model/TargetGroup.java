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

/**
 * A group for target definitions, selected by the user for analysis.
 * 
 * @author Chuck Lan
 *
 */
@XmlRootElement(name = "targetGroup")
@XmlType(propOrder = { "name", "required", "definitions" })
public class TargetGroup {
	private List<TargetDefinition> definitions;
	private boolean required;	
	private String name;

	public TargetGroup() {
		definitions = new ArrayList<TargetDefinition>();
	}
	
	@XmlElementWrapper(name = "definitions")
	@XmlElement(name = "definition")
	public List<TargetDefinition> getDefinitions() {
		return definitions;
	}

	public void setDefinitions(List<TargetDefinition> definitions) {
		this.definitions = definitions;
	}
	
	@XmlAttribute(name = "required")
	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean isRequired) {
		this.required = isRequired;
	}
	
	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Set<String> getTargets() {
		Set<String> retVal = new HashSet<String>();
		for (TargetDefinition td : definitions) {
			retVal.add(td.getTargetId());
		}
		return retVal;
	}
	
	public int hashCode() {
		if (name == null) {
			return 1;
		} else {
			return name.hashCode();
		}
	}
	
	public boolean equals(Object o) {
		return (o instanceof TargetGroup && ((TargetGroup) o).name != null) ? ((TargetGroup) o).name.equals(this.name) : false;
	}
}
