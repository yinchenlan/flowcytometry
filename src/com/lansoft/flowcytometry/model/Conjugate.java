package com.lansoft.flowcytometry.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "conjugate")
@XmlType(propOrder = { "name", "brightness", "maxEmissionWl", "maxExcitationWl" })
public class Conjugate {
	private String name;
	private int brightness;
	private int maxEmissionWl;
	private int maxExcitationWl;
	/**
	 * Auto selection for conjugate
	 */
	public static final String CONJUGATE_AUTO_SELECT = "Auto Select";
	
	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlAttribute(name = "brightness")
	public int getBrightness() {
		return brightness;
	}
	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	@XmlAttribute(name = "maxEmissionWl")
	public int getMaxEmissionWl() {
		return maxEmissionWl;
	}
	public void setMaxEmissionWl(int maxEmissionWl) {
		this.maxEmissionWl = maxEmissionWl;
	}
	
	@XmlAttribute(name = "maxExcitationWl")
	public int getMaxExcitationWl() {
		return maxExcitationWl;
	}
	public void setMaxExcitationWl(int maxExcitationWl) {
		this.maxExcitationWl = maxExcitationWl;
	}	
	
	
	
}
