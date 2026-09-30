package com.lansoft.flowcytometry.model;

import java.util.Comparator;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "antibody")
@XmlType(propOrder = { "conjugateName", "targetName", "targetSpecies", "company", "catalogNum", "lotNum", "clone", "isotype", "aups" })
public class Antibody implements Cloneable {
	private String conjugateName;
	private String targetName;
	private String company;
	private String catalogNum;
	private String comments;
	private String targetSpecies;
	private String lotNum;
	private String clone;
	private String isotype;
	private String aups;
	private transient boolean isRequired;
	private double brightness;
	private double density;
	
	@XmlTransient
	public double getBrightness() {
		return brightness;
	}

	public void setBrightness(double brightness) {
		this.brightness = brightness;
	}

	@XmlTransient
	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
	}

	@XmlTransient
	public boolean isRequired() {
		return isRequired;
	}
	
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}
	@XmlAttribute(name="conjugateName")
	public String getConjugateName() {
		return conjugateName;
	}
	public void setConjugateName(String name) {
		this.conjugateName = name;
	}
	
	@XmlAttribute(name="company")
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	@XmlAttribute(name="catalogNum")
	public String getCatalogNum() {
		return catalogNum;
	}
	public void setCatalogNum(String catalogNum) {
		this.catalogNum = catalogNum;
	}
	
	@XmlAttribute(name="comments")
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	@XmlAttribute(name="targetName")
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	
	@XmlAttribute(name="targetSpecies")
	public String getTargetSpecies() {
		return targetSpecies;
	}
	
	public void setTargetSpecies(String targetSpecies) {
		this.targetSpecies = targetSpecies;
	}
	
	@XmlAttribute(name="lotNum")
	public String getLotNum() {
		return lotNum;
	}
	
	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	
	@XmlAttribute(name="clone")
	public String getClone() {
		return clone;
	}
	
	public void setClone(String clone) {
		this.clone = clone;
	}
	
	@XmlAttribute(name="aups")
	public String getAups() {
		return aups;
	}
	
	public void setAups(String aups) {
		this.aups = aups;
	}
	
	@XmlAttribute(name="isotype")
	public String getIsotype() {
		return isotype;
	}
	public void setIsotype(String isotype) {
		this.isotype = isotype;
	}	
	
	public int hashCode() {
		return 37 * targetName.hashCode() + conjugateName.hashCode();
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof Antibody)) return false;
		Antibody o = (Antibody) other;
		return o.targetName.equals(targetName) && o.conjugateName.equals(conjugateName);
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("target=>").append(targetName).append(":conjugate=>").append(conjugateName);
		buf.append(":target species=>").append(targetSpecies).append(":company=>").append(company);
		buf.append(":catalog #=>").append(catalogNum).append(":lot #=>").append(lotNum);
		buf.append(":clone=>").append(clone).append(":isotype=>").append(isotype);
		buf.append(":aups=>").append(aups);
		return buf.toString();
	}
	
	@Override
	public Object clone() {
		Antibody ab = new Antibody();
		ab.setAups(this.aups);
		ab.setCatalogNum(this.catalogNum);
		ab.setClone(this.clone);
		ab.setComments(this.comments);
		ab.setCompany(this.company);
		ab.setConjugateName(this.conjugateName);
		ab.setIsotype(this.isotype);
		ab.setLotNum(this.lotNum);
		ab.setTargetName(this.targetName);
		ab.setTargetSpecies(this.targetSpecies);
		return ab;
	}
}
