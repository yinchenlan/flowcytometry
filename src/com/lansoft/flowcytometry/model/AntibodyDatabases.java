package com.lansoft.flowcytometry.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "antibodydatabases")
public class AntibodyDatabases {
	private List<Antibodies> antibodies;
	private Map<String, Set<Antibody>> map;
	private static PostLoadCallback updateMapCallback;
	
	static {
		updateMapCallback = new PostLoadCallback() {
			@Override
			public void doWork(Object dataModel) {
				if (dataModel instanceof AntibodyDatabases) {
					AntibodyDatabases abdbs = (AntibodyDatabases) dataModel;
					for (Antibodies abs : abdbs.getAntibodies()) {
						for (Antibody ab : abs.getAntibodiesList()) {
							String targetId = ab.getTargetName();
							Set<Antibody> abSet = abdbs.map.get(targetId);
							if (abSet == null) {
								abSet = new HashSet<Antibody>();
								abdbs.map.put(targetId, abSet);
								abSet.add(ab);
							} else {
								if (!abSet.contains(ab)) {
									abSet.add(ab);
								}
							}
						}
					}
				}
			}
		};
	}
	
	public AntibodyDatabases() {		
		antibodies = new ArrayList<Antibodies>();
		map = new HashMap<String, Set<Antibody>>();
	}

	@XmlElement(name = "antibodies")
	public List<Antibodies> getAntibodies() {
		return antibodies;
	}

	public void setAntibodies(List<Antibodies> antibodies) {
		this.antibodies = antibodies;
	}

	public static AntibodyDatabases getAntibodyDatabases() throws JAXBException,
			FileNotFoundException, InstantiationException, IllegalAccessException {
		return (AntibodyDatabases) ModelDAO.loadObject(AntibodyDatabases.class, updateMapCallback);
	}
	
	public static void saveAntibodyDatabases(AntibodyDatabases antibodyDatabases)
			throws JAXBException, FileNotFoundException, IOException {
		ModelDAO.saveObject(antibodyDatabases);
	}
	
	public static void main(String[] args) throws Exception {
		AntibodyDatabases adb = new AntibodyDatabases();
		Antibodies antibodies = new Antibodies();
		antibodies.setName("DB1");
		antibodies.setDescription("DESC");
		Antibody ab = new Antibody();
		ab.setConjugateName("CONJ1");
		ab.setTargetName("TG1");
		antibodies.getAntibodiesList().add(ab);
		adb.getAntibodies().add(antibodies);
		saveAntibodyDatabases(adb);
	}

	public Map<String, Set<Antibody>> getMap() {
		return map;
	}

}
