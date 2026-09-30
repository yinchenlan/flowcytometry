package com.lansoft.flowcytometry.ui.nextgen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

public class AntibodyDB implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2247276399739599794L;
	
	//Database is a TreeMap, with target as the key, and the value is 
	//an ArrayList of Antibody objects.
	private TreeMap <String, ArrayList <Antibody> > antibodies;
	private String name;
	
	public AntibodyDB ( String infile ) {
		antibodies = ExcelReader.readfile( infile );
	}
	
	public TreeMap<String, ArrayList<Antibody>> getAntibodies() {
		return antibodies;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<Antibody> getAntibodies ( String n ) {
		return antibodies.get( n );
	}
	
	public void addAntibody ( Antibody a ) {
		if ( antibodies.containsKey( a.getTarget() )) {
			antibodies.get( a.getTarget() ).add( a );
		} else {
			ArrayList <Antibody> antibodyArray = new ArrayList <Antibody>();
			antibodyArray.add( a );
			antibodies.put( a.getTarget(), antibodyArray );
		}
	}

}
