package com.lansoft.flowcytometry.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class ModelDAO {
	private static final String FLOW_CYTOMETRY_FOLDER = "flowcytometry";
	private static final String FILE_EXTENSION = ".xml";
	
	public static Object loadObject(Class clazz) 
			throws JAXBException, FileNotFoundException, InstantiationException, IllegalAccessException {
		JAXBContext context = JAXBContext.newInstance(clazz);
	    Unmarshaller um = context.createUnmarshaller();
	    File file = new File(getFileName(clazz.getSimpleName()));
	    Object obj = null;
	    if (file.exists()) {
	    	obj = um.unmarshal(new FileReader(file));
	    } else {
	    	obj = clazz.newInstance();
	    }	    
	    return obj;
	}
	
	public static Object loadObject(Class clazz, PostLoadCallback plcb) 
			throws JAXBException, FileNotFoundException, InstantiationException, IllegalAccessException {
		JAXBContext context = JAXBContext.newInstance(clazz);
	    Unmarshaller um = context.createUnmarshaller();
	    File file = new File(getFileName(clazz.getSimpleName()));
	    Object obj = null;
	    if (file.exists()) {
	    	obj = um.unmarshal(new FileReader(file));
	    } else {
	    	obj = clazz.newInstance();
	    }	    
	    plcb.doWork(obj);
	    return obj;
	}
	
	public static String getFileName(String fileName) {
		return getConfigDirectoryName() + File.separator + fileName + FILE_EXTENSION;
	}
	
	public static String getConfigDirectoryName() {
	    String userHome = System.getenv("USERPROFILE");
	    if (userHome == null) {
	        userHome = System.getProperty("user.home");
	    }
		return userHome + File.separator + FLOW_CYTOMETRY_FOLDER;
	}
	
	public static void saveObject(Object obj) throws JAXBException, FileNotFoundException, IOException {
		JAXBContext context = JAXBContext.newInstance(obj.getClass());
		Marshaller m = context.createMarshaller();
	    m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	    File file = new File(getFileName(obj.getClass().getSimpleName()));
	    if (!file.exists()) {
	    	new File(getConfigDirectoryName()).mkdirs();
	    	file.createNewFile();
	    } 
	    m.marshal(obj, new File(getFileName(obj.getClass().getSimpleName())));	
	}
}
