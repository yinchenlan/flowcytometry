package com.lansoft.flowcytometry.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "configurations")
public class Configurations {
	private List<Configuration> configurations;
	
	public Configurations() {
		configurations = new ArrayList<Configuration>();
	}
	@XmlElement(name = "configuration")
	public List<Configuration> getConfigurationsList() {
		return configurations;
	}

	public void setConfigurationsList(List<Configuration> configurations) {
		this.configurations = configurations;
	}
	
	public static Configurations loadConfigurations() 
			throws JAXBException, FileNotFoundException, InstantiationException, IllegalAccessException {
		return (Configurations) ModelDAO.loadObject(Configurations.class);
	}
		
	public static void saveConfigurations(Configurations configurations) 
			throws JAXBException, FileNotFoundException, IOException {
		ModelDAO.saveObject(configurations);
	}
	
	public static void main(String[] args) throws Exception {
		DetectorConjugates det1 = new DetectorConjugates();
		det1.setDetectorName("DET1");
		List<String> nameList = new ArrayList<String>();
		nameList.add("CONJ1");
		nameList.add("CONJ2");
		det1.setNameList(nameList);
		Configuration configuration = new Configuration();
		configuration.setName("CONF1");
		configuration.setDescription("Test Configurations");
		List<DetectorConjugates> detectorList = new ArrayList<DetectorConjugates>();
		detectorList.add(det1);
		configuration.setDetectorConjugatesList(detectorList);
		List<BrightnessIndex> brightnessIndices = new ArrayList<BrightnessIndex>();
		BrightnessIndex bi = new BrightnessIndex();
		bi.setConjugateName("CONJ1");
		bi.setBrightness(.2F);
		brightnessIndices.add(bi);
		configuration.setBrightnessIndices(brightnessIndices);
		Configurations configurations = new Configurations();
		List<Configuration> configurationsList = new ArrayList<Configuration>();
		configurationsList.add(configuration);
		configurations.setConfigurationsList(configurationsList);
		saveConfigurations(configurations);
	}
}
