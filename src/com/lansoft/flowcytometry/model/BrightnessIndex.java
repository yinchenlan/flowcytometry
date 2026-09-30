package com.lansoft.flowcytometry.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "brightnessIndex")
@XmlType(propOrder = { "conjugateName", "brightness" })
public class BrightnessIndex {
	
	private Float brightness;
	
	private String conjugateName;

	public BrightnessIndex() {
		// TODO Auto-generated constructor stub
	}
	
	@XmlAttribute(name = "brightness")
	public Float getBrightness() {
		return brightness;
	}

	public void setBrightness(Float brightness) {
		this.brightness = brightness;
	}

	@XmlAttribute(name = "conjugateName")
	public String getConjugateName() {
		return conjugateName;
	}

	public void setConjugateName(String conjugateName) {
		this.conjugateName = conjugateName;
	}	
}
