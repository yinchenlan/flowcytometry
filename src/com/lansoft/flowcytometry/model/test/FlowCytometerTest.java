package com.lansoft.flowcytometry.model.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lansoft.flowcytometry.model.Antibodies;
import com.lansoft.flowcytometry.model.AntibodyDatabases;
import com.lansoft.flowcytometry.model.Configuration;
import com.lansoft.flowcytometry.model.Conjugate;
import com.lansoft.flowcytometry.model.Conjugates;
import com.lansoft.flowcytometry.model.FlowCytometer;
import com.lansoft.flowcytometry.model.FlowCytometers;
import com.lansoft.flowcytometry.model.TargetDefinition;

public class FlowCytometerTest {
	private FlowCytometers flowCytometers;
	private Conjugates conjugates;
	private AntibodyDatabases abdb;

	@Before
	public void setUp() throws Exception {
		flowCytometers = FlowCytometers.loadFlowCytometers();
		conjugates = Conjugates.loadConjugates();
		abdb = AntibodyDatabases.getAntibodyDatabases();
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetConfiguration() {
		FlowCytometer fc = flowCytometers.getFlowCytometers().get(1);
		List<TargetDefinition> defs = getTargetDefinitions();
		try {
			Configuration conf = fc.getConfiguration(defs);
			System.out.println(conf);
		} catch (FileNotFoundException | InstantiationException
				| IllegalAccessException | JAXBException e) {
			fail("Exception is thrown while creating configuration.  " + e.getMessage());
		}
		
	}


	private List<TargetDefinition> getTargetDefinitions() {
		List<TargetDefinition> td = new ArrayList<TargetDefinition>();
		TargetDefinition cd4 = new TargetDefinition();
		cd4.setTargetId("CD4");
		cd4.setDensity(10);
		cd4.setConjugateId(Conjugate.CONJUGATE_AUTO_SELECT);
		td.add(cd4);
		
		TargetDefinition cd2 = new TargetDefinition();
		cd2.setTargetId("CD2");
		cd2.setDensity(3);
		cd2.setConjugateId(Conjugate.CONJUGATE_AUTO_SELECT);
		td.add(cd2);
		
		TargetDefinition cd40 = new TargetDefinition();
		cd40.setTargetId("CD40");
		cd40.setDensity(8);
		cd40.setConjugateId(Conjugate.CONJUGATE_AUTO_SELECT);
		td.add(cd40);
		
		TargetDefinition vegfr3 = new TargetDefinition();
		vegfr3.setTargetId("VEGFR-3");
		vegfr3.setDensity(2);
		vegfr3.setConjugateId(Conjugate.CONJUGATE_AUTO_SELECT);
		td.add(vegfr3);
		
		TargetDefinition cd117 = new TargetDefinition();
		cd117.setTargetId("CD117");
		cd117.setDensity(7);
		cd117.setConjugateId(Conjugate.CONJUGATE_AUTO_SELECT);
		td.add(cd117);
		
		TargetDefinition ecadherin = new TargetDefinition();
		ecadherin.setTargetId("E-Cadherin");
		ecadherin.setDensity(3);
		ecadherin.setConjugateId(Conjugate.CONJUGATE_AUTO_SELECT);
		td.add(ecadherin);
		
		return td;
	}

}
