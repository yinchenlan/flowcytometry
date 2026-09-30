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

@XmlRootElement(name = "flowCytometers")
public class FlowCytometers {
	private List<FlowCytometer> flowCytometers;

	@XmlElement(name = "flowCytometer")
	public List<FlowCytometer> getFlowCytometers() {
		return flowCytometers;
	}

	public void setFlowCytometers(List<FlowCytometer> flowCytometers) {
		this.flowCytometers = flowCytometers;
	}
	
	public static FlowCytometers loadFlowCytometers() 
			throws JAXBException, FileNotFoundException, InstantiationException, IllegalAccessException {
		return (FlowCytometers) ModelDAO.loadObject(FlowCytometers.class);
	}
	
	
	public static void saveFlowCytometers(FlowCytometers flowCytometers) 
			throws JAXBException, FileNotFoundException, IOException {
		ModelDAO.saveObject(flowCytometers);
	}
	
	public static void main(String[] args) throws Exception {
		List<Detector> detectorList1 = new ArrayList<Detector>();
		Detector detector1 = new Detector();
		detector1.setWl(500);
		detector1.setWlWindow(30);
		detector1.setName("Detector 1");
		detectorList1.add(detector1);
		Detector detector2 = new Detector();
		detector2.setWl(600);
		detector2.setWlWindow(40);
		detector2.setName("Detector 2");
		detectorList1.add(detector2);
		Detector detector3 = new Detector();
		detector3.setWl(300);
		detector3.setWlWindow(10);
		detector3.setName("Detector 3");
		detectorList1.add(detector3);
		Laser laser1 = new Laser();
		laser1.setName("Laser 1");
		laser1.setDetectorList(detectorList1);
		List<Laser> laserList = new ArrayList<Laser>();
		laserList.add(laser1);
		FlowCytometer flowCytometer1 = new FlowCytometer();
		flowCytometer1.setName("Flow Cytometer 1");
		flowCytometer1.setLaserList(laserList);
		List<FlowCytometer> flowCytometersList = new ArrayList<FlowCytometer>();
		flowCytometersList.add(flowCytometer1);
		FlowCytometers flowCytometers = new FlowCytometers();
		flowCytometers.setFlowCytometers(flowCytometersList);
		saveFlowCytometers(flowCytometers);
	}
	
}
