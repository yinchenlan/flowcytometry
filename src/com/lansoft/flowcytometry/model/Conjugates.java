package com.lansoft.flowcytometry.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "conjugates")
public class Conjugates {
	private List<Conjugate> conjugatesList;

	public Conjugates() {
		conjugatesList = new ArrayList<Conjugate>();
	}

	@XmlElement(name = "conjugate")
	public List<Conjugate> getConjugatesList() {
		return conjugatesList;
	}

	public void setConjugatesList(List<Conjugate> conjugates) {
		this.conjugatesList = conjugates;
	}
	
	public static Conjugates loadConjugates() 
			throws JAXBException, FileNotFoundException, InstantiationException, IllegalAccessException {
		return (Conjugates) ModelDAO.loadObject(Conjugates.class);
	}
		
	public static void saveConjugates(Conjugates conjugates) 
			throws JAXBException, FileNotFoundException, IOException {
		ModelDAO.saveObject(conjugates);
	}
	
	public static void main(String[] args) throws Exception {
		List<Conjugate> cList = new ArrayList<Conjugate>();
		
		Conjugate c1 = new Conjugate();
		c1.setName("Alexa Fluor 488");
		c1.setMaxExcitationWl(490);
		c1.setMaxEmissionWl(519);
		c1.setBrightness(6);
		cList.add(c1);
		
		Conjugate c2 = new Conjugate();
		c2.setName("Alexa Fluor 647");
		c2.setMaxExcitationWl(650);
		c2.setMaxEmissionWl(668);
		c2.setBrightness(10);
		cList.add(c2);
		
		Conjugate c3 = new Conjugate();
		c3.setName("APC");
		c3.setMaxExcitationWl(650);
		c3.setMaxEmissionWl(660);
		c3.setBrightness(10);
		cList.add(c3);
		
		Conjugate c4 = new Conjugate();
		c4.setName("APC-Alexa Fluor 750");
		c4.setMaxExcitationWl(650);
		c4.setMaxEmissionWl(775);
		c4.setBrightness(4);
		cList.add(c4);
		
		Conjugate c5 = new Conjugate();
		c5.setName("APC-eFluor 780");
		c5.setMaxExcitationWl(633);
		c5.setMaxEmissionWl(780);
		c5.setBrightness(4);
		cList.add(c5);
		
		Conjugate c6 = new Conjugate();
		c6.setName("FITC");
		c6.setMaxExcitationWl(490);
		c6.setMaxEmissionWl(525);
		c6.setBrightness(5);
		cList.add(c6);
		
		Conjugate c7 = new Conjugate();
		c7.setName("Oregon Green");
		c7.setMaxExcitationWl(501);
		c7.setMaxEmissionWl(526);
		c7.setBrightness(9);
		cList.add(c7);
		
		Conjugate c8 = new Conjugate();
		c8.setName("PE");
		c8.setMaxExcitationWl(496);
		c8.setMaxEmissionWl(575);
		c8.setBrightness(10);
		cList.add(c8);
		
		Conjugate c9 = new Conjugate();
		c9.setName("PerCP");
		c9.setMaxExcitationWl(482);
		c9.setMaxEmissionWl(678);
		c9.setBrightness(8);
		cList.add(c9);
		
		Conjugate c10 = new Conjugate();
		c10.setName("R-PE");
		c10.setMaxExcitationWl(488);
		c10.setMaxEmissionWl(578);
		c10.setBrightness(10);
		cList.add(c10);
		
		Conjugate c11 = new Conjugate();
		c11.setName("Cy5");
		c11.setMaxExcitationWl(649);
		c11.setMaxEmissionWl(670);
		c11.setBrightness(5);
		cList.add(c11);
		
		Conjugate c12 = new Conjugate();
		c12.setName("APC-Cy7");
		c12.setMaxExcitationWl(650);
		c12.setMaxEmissionWl(785);
		c12.setBrightness(10);
		cList.add(c12);
		
		Conjugates conjugates = new Conjugates();
		conjugates.setConjugatesList(cList);
		Conjugates.saveConjugates(conjugates);
		
	}

	public Map<String, Conjugate> getMap() {
		Map<String, Conjugate> retVal = new HashMap<String, Conjugate>();
		for (Conjugate conj : conjugatesList) {
			retVal.put(conj.getName(), conj);
		}
		return retVal;
	}
}
