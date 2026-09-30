package com.lansoft.flowcytometry.ui.nextgen;
import java.io.Serializable;

/**
 * This class is an antibody entry from the antibody database.
 * Each antibody entry has a contains: conjugate, target, species,
 * company, catalog, lot, clone, isotype, and aups.
 * @author jennychien
 */

public class Antibody implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -707076929933292197L;
	
	private String conjugate;
	private String target;
	private String species;
	private String company;
	private String catalog;
	private String lot;
	private String clone;
	private String isotype;
	private Integer aups;
	
	public Antibody( String c, String t, String s, String com, String cat, String l, String clone, String iso, Integer aups ) {
		conjugate = c;
		target = t;
		species = s;
		company = com;
		catalog = cat;
		lot = l;
		this.clone = clone;
		isotype = iso;
		this.aups = aups;
	}
	
	public String getConjugate() {
		return conjugate;
	}
	public void setConjugate(String conjugate) {
		this.conjugate = conjugate;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getLot() {
		return lot;
	}
	public void setLot(String lot) {
		this.lot = lot;
	}
	public String getClone() {
		return clone;
	}
	public void setClone(String clone) {
		this.clone = clone;
	}
	public String getIsotype() {
		return isotype;
	}
	public void setIsotype(String isotype) {
		this.isotype = isotype;
	}
	public Integer getAups() {
		return aups;
	}
	public void setAups(Integer aups) {
		this.aups = aups;
	}
	
	public String toString() {
		return conjugate + " " + target + " " + species + " " + company + " " + catalog + " " + lot + " " + clone + " " + isotype + " " + aups + "\n";
	}
	
	
}
