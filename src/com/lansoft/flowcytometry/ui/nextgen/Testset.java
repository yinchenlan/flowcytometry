package com.lansoft.flowcytometry.ui.nextgen;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * This class represents at Target Testset.  Each testset
 * contains multiple targets, and targets can be organized 
 * into subgroups.  A subgroup can be "required" (which means
 * it must be contained in each solution panel).  
 * @author jennychien
 */

public class Testset implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1975656731332577079L;
	//name is the name of the Testset
	//targets is a TreeMap of target objects, with the target name as key
	//preset is the TreeMap of assigned target to conjugate pairs, with target as key.
	//requiredGroup is a String arrayList of all groups that are required
	//targetGroups is a TreeMap of groups, key is the name of the group and value is arrayList of target names
	private String name;
	private TreeMap <String, Target> targets;
	private TreeMap <String, ArrayList<String>> targetGroups; 
	private ArrayList <String> requiredGroups;
	private TreeMap <String, String> preset;

	public Testset( String n ) {
		setName(n);
		targets = new TreeMap <String, Target> ();
		preset = new TreeMap <String, String> ();
		targetGroups = new TreeMap <String, ArrayList<String>> ();
		requiredGroups = new ArrayList <String> ();
	}
	public void addTarget(Target t) {
		targets.put( t.getName(), t );
	}
	
	public TreeMap <String, Target> getTargets () {
		return targets;
	}
	
	public void assignGroups( String n, ArrayList<String> t ) {
		targetGroups.put(n, t);
		//For all targets in arrayList, add groupname to group arrayList
		for (int i=0; i<t.size(); i++ ){
			this.targets.get( t.get(i) ).addGroup( n );;
		}
	}
	
	public void assignRequired( String n) {
		requiredGroups.add( n );
		//For all targets in group, set as required
		for (int i=0; i<targetGroups.get(n).size(); i++ ){
			targets.get( targetGroups.get(i) ).setRequired( "Yes" );
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	//Checks to see if the targets in the testset include arguement
	public boolean contains( String n ) {
		if ( targets.containsKey(n) ) {
			return true;
		} else {
			return false;
		}
	}
	
	
	//Removies target from targets TreeMap
	public void remove( String n ) {
		targets.remove( n );
	}
	
	//Add to the preset TreeMap an assigned pair of target & conjugate
	public void assign( String t, String c ) {
		preset.put(t, c);
	}
	
	public TreeMap<String, String> getPreset () {
		return preset;
	}
	
	public TreeMap<String, ArrayList<String>> getTargetGroups() {
		return targetGroups;
	}
	
	public void removeGroup ( String s ) {
		targetGroups.remove(s);
	}

}
