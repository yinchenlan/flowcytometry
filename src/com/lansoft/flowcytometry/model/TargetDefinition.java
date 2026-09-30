package com.lansoft.flowcytometry.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "targetDefinition")
@XmlType(propOrder = { "targetId", "targetMolecule", "conjugateId", "density"})
public class TargetDefinition {
	private boolean isTargetMolecule;
	private String targetId;
	private String conjugateId;
	private float density;
	
	@XmlAttribute(name = "targetMolecule")
	public boolean isTargetMolecule() {
		return isTargetMolecule;
	}
	public void setTargetMolecule(boolean isTargetMolecule) {
		this.isTargetMolecule = isTargetMolecule;
	}
	
	@XmlAttribute(name = "targetId")
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	
	@XmlAttribute(name = "conjugateId")
	public String getConjugateId() {
		return conjugateId;
	}
	public void setConjugateId(String conjugateId) {
		this.conjugateId = conjugateId;
	}
	
	@XmlAttribute(name = "density")
	public float getDensity() {
		return density;
	}
	public void setDensity(float density) {
		this.density = density;
	}
	
}
