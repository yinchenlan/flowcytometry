package com.lansoft.flowcytometry.model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "flowCytometer")
@XmlType(propOrder = { "name", "laserList" })
public class FlowCytometer {
	
	/**
	 * Window to include conjugate
	 */
	private static final int WINDOW = 35;

	private List<Laser> laserList;
	
	private String name;
	
	public FlowCytometer() {
		laserList = new ArrayList<Laser>();
	}
	
	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElementWrapper(name = "lasers")
	@XmlElement(name = "laser")
	public List<Laser> getLaserList() {
		return laserList;
	}
	
	public void setLaserList(List<Laser> laserList) {
		this.laserList = laserList;
	}
	
	/**
	 * Given a list of target definitions and list of antibodies,
	 * return a {@link Configuration} object.
	 * 
	 * @param defs
	 * @return
	 * @throws JAXBException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws FileNotFoundException 
	 */
	public Configuration getConfiguration(List<TargetDefinition> defs) 
			throws FileNotFoundException, InstantiationException, IllegalAccessException, JAXBException {
		Configuration retVal = new Configuration();
		List<Laser> sll = new ArrayList<Laser>(laserList);
		// sort array of laser in descending order by wavelength
		Collections.sort(sll, Laser.ASCENDING);
		updateLasers(sll);
		for (Conjugate conj : Conjugates.loadConjugates().getConjugatesList()) {
			BrightnessIndex bi = new BrightnessIndex();
			bi.setConjugateName(conj.getName());
			bi.setBrightness((float) conj.getBrightness());
			retVal.getBrightnessIndices().add(bi);
		}
		Set<String> usedConjugates = new HashSet<String>();
		for (Laser laser : sll) {
			List<Detector> dets = new ArrayList<Detector>(laser.getDetectorList());
			Collections.sort(dets, Detector.DESCENDING);
			for (Detector detector : dets) {
				DetectorConjugates dc = new DetectorConjugates();
				retVal.getDetectorConjugatesList().add(dc);
				dc.setDetectorName(laser.getName() + "-" + detector.getName());
				try {
					List<String> nameList = getConjugateNameList(laser, detector, defs, usedConjugates);
					dc.setNameList(nameList);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}
		return retVal;
	}

	/**
	 * Update laser min and max wavelengths.
	 * 
	 * @param sll expecting a sorted list
	 */
	public void updateLasers(List<Laser> sll) {
		for (int idx = 0; idx < sll.size(); idx++) {
			Laser l = sll.get(idx);
			int wl = l.getWavelength();
			l.setMinWl(wl - WINDOW);
			l.setMaxWl(wl + WINDOW);
		}
	}

	/**
	 * Returns a list of conjugate names who can be excited by 
	 * the laser and can be detected by the detector.
	 * 
	 * @param laser
	 * @param detector
	 * @param defs
	 * @return
	 * @throws JAXBException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws FileNotFoundException 
	 */
	public List<String> getConjugateNameList(Laser laser, Detector detector,
			List<TargetDefinition> defs, Set<String> usedConjugates) 
					throws FileNotFoundException, InstantiationException, IllegalAccessException, JAXBException {
		List<String> retVal = new ArrayList<String>();
		Conjugates conjugates = Conjugates.loadConjugates();
		Map<String, Conjugate> conjugateMap = conjugates.getMap();
		AntibodyDatabases database = AntibodyDatabases.getAntibodyDatabases();
		Map<String, Set<Antibody>> antibodyMap = database.getMap();
		for (TargetDefinition td : defs) {
			String targetId = td.getTargetId();
			Set<Antibody> abs = antibodyMap.get(targetId);
			for (Antibody ab : abs) {
				String conjugateId = ab.getConjugateName();				
				Conjugate conj = conjugateMap.get(conjugateId);
				// Skip antibody if conjugate is not found
				if (conj == null) {
					continue;
				}
				if (isAcceptableConjugate(laser, detector, conj)) {
					if (!usedConjugates.contains(conj.getName())) {
						retVal.add(conj.getName());
						usedConjugates.add(conj.getName());
					}
				}
			}
		}
		return retVal;
	}

	public boolean isAcceptableConjugate(Laser laser, Detector detector,
			Conjugate conj) {
		if (conj.getMaxExcitationWl() >= laser.getMinWl() &&
				conj.getMaxExcitationWl() <= laser.getMaxWl()) {
			if (detector.getName().endsWith("LP") && conj.getMaxEmissionWl() > detector.getWl()) {
				return true;
			} else if (!detector.getName().endsWith("LP") && conj.getMaxEmissionWl() >= detector.getWl() - detector.getWlWindow() / 2 &&
				conj.getMaxEmissionWl() <= detector.getWl() + detector.getWlWindow() / 2) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
