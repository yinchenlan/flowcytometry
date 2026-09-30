package com.lansoft.flowcytometry.service;

import java.util.ArrayList;
import java.util.List;

import com.lansoft.flowcytometry.model.Detector;
import com.lansoft.flowcytometry.model.FlowCytometer;
import com.lansoft.flowcytometry.model.FlowCytometers;
import com.lansoft.flowcytometry.model.Laser;

public class FlowCytometerUtil {
	
	public static void main(String[] args) throws Exception {
		FlowCytometer fc = new FlowCytometer();
		fc.setName("CantoI");
		
		Laser laser1 = new Laser();
		laser1.setName("Blue");
		laser1.setWavelength(488);
		
		Detector det1 = new Detector();
		det1.setName("780/60");
		det1.setWl(780);
		det1.setWlWindow(60);

		Detector det2 = new Detector();
		det2.setName("670LP");
		det2.setWl(670);
		det2.setWlWindow(Integer.MAX_VALUE);
		
		Detector det3 = new Detector();
		det3.setName("585/42");
		det3.setWl(585);
		det3.setWlWindow(42);
		
		Detector det4 = new Detector();
		det4.setName("530/30");
		det4.setWl(530);
		det4.setWlWindow(30);
		
		Detector det5 = new Detector();
		det5.setName("488/20");
		det5.setWl(488);
		det5.setWlWindow(20);
		
		Laser laser2 = new Laser();
		laser2.setName("Red");
		laser2.setWavelength(633);
		
		Detector det6 = new Detector();
		det6.setName("780/60");
		det6.setWl(780);
		det6.setWlWindow(60);
		
		Detector det7 = new Detector();
		det7.setName("660/20");
		det7.setWl(660);
		det7.setWlWindow(20);
		
		FlowCytometers flowCytometers = new FlowCytometers();
		List<FlowCytometer> fcList = new ArrayList<FlowCytometer>();
		flowCytometers.setFlowCytometers(fcList);
		fcList.add(fc);
		List<Laser> laserList = new ArrayList<Laser>();
		fc.setLaserList(laserList);
		
		laserList.add(laser1);
		List<Detector> detectorList = new ArrayList<Detector>();
		laser1.setDetectorList(detectorList);
		detectorList.add(det1);
		detectorList.add(det2);
		detectorList.add(det3);
		detectorList.add(det4);
		detectorList.add(det5);
		
		laserList.add(laser2);
		detectorList = new ArrayList<Detector>();
		laser2.setDetectorList(detectorList);
		detectorList.add(det6);
		detectorList.add(det7);
		
		FlowCytometers.saveFlowCytometers(flowCytometers);
	}
}
