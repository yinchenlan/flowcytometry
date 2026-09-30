package com.lansoft.flowcytometry.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "detectors")
public class Detectors {
	private static final String FLOW_CYTOMETRY_FOLDER = "flowcytometry";
	private static final String DETECTOR_FILE = "detector-jaxb.xml";
	private List<DetectorConjugates> detectorConjugatesList;
	
	public Detectors() {
		detectorConjugatesList = new ArrayList<DetectorConjugates>();
	}

	@XmlElement(name = "detector")
	public List<DetectorConjugates> getDetectorConjugatesList() {
		return detectorConjugatesList;
	}

	public void setDetectorConjugatesList(List<DetectorConjugates> detectorConjugatesList) {
		this.detectorConjugatesList = detectorConjugatesList;
	}
	
	public static Detectors getDetectors() throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(Detectors.class);
	    Unmarshaller um = context.createUnmarshaller();
	    File detectorFile = new File(getFileName());
	    Detectors detectors = null;
	    if (detectorFile.exists()) {
	    	detectors = (Detectors) um.unmarshal(new FileReader(getFileName()));
	    } else {
	    	detectors = new Detectors();
	    }	    
	    return detectors;
	}
	
	public static String getFileName() {
		return getConfigDirectoryName() + File.separator + DETECTOR_FILE;
	}
	
	public static String getConfigDirectoryName() {
        String userHome = System.getenv("USERPROFILE");
        if (userHome == null) {
            userHome = System.getProperty("user.home");
        }
        return userHome + File.separator + FLOW_CYTOMETRY_FOLDER;
    }
	
	public static void saveDetectors(Detectors detectors) throws JAXBException, FileNotFoundException, IOException {
		JAXBContext context = JAXBContext.newInstance(Detectors.class);
		Marshaller m = context.createMarshaller();
	    m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	    File file = new File(getFileName());
	    if (!file.exists()) {
	    	new File(getConfigDirectoryName()).mkdirs();
	    	file.createNewFile();
	    } 
	    m.marshal(detectors, new File(getFileName()));	
	}
}
