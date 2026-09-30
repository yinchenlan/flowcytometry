package com.lansoft.flowcytometry.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "targetsConfigurations")
public class TargetsConfigurations {
	
	private List<TargetsConfiguration> targetsConfigurations;
	
	public TargetsConfigurations() {
		targetsConfigurations = new ArrayList<TargetsConfiguration>();
	}

	@XmlElement(name = "targetsConfiguration")
	public List<TargetsConfiguration> getTargetsConfigurations() {
		return targetsConfigurations;
	}

	public void setTargetsConfigurations(
			List<TargetsConfiguration> targetsConfigurations) {
		this.targetsConfigurations = targetsConfigurations;
	}
	
	public static TargetsConfigurations loadTargetsConfigurations() 
			throws JAXBException, FileNotFoundException, InstantiationException, IllegalAccessException {
		return (TargetsConfigurations) ModelDAO.loadObject(TargetsConfigurations.class);
	}
	
	public static void saveTargetsConfigurations(TargetsConfigurations targetsConfigurations) 
			throws JAXBException, FileNotFoundException, IOException {
		ModelDAO.saveObject(targetsConfigurations);
	}
	
	public static void main(String[] args) throws Exception {
		TargetDefinition td = new TargetDefinition();
		td.setTargetId("targetId1");
		td.setConjugateId("conjugateId1");
		td.setDensity(5f);
		td.setTargetMolecule(true);
		List<TargetDefinition> tdList = new ArrayList<TargetDefinition>();
		tdList.add(td);
		TargetGroup tg = new TargetGroup();
		tg.setDefinitions(tdList);
		tg.setName("targetGroup1");
		tg.setRequired(true);
		List<TargetGroup> tgList = new ArrayList<TargetGroup>();
		tgList.add(tg);
		TargetsConfiguration tc = new TargetsConfiguration();
		tc.setGroups(tgList);
		tc.setName("targetsConfiguration1");
		List<TargetsConfiguration> tcList = new ArrayList<TargetsConfiguration>();
		tcList.add(tc);
		TargetsConfigurations tcs = new TargetsConfigurations();
		tcs.setTargetsConfigurations(tcList);
		saveTargetsConfigurations(tcs);
	}
}
