package com.lansoft.flowcytometry.service;

public class Group {
	int g;
	
	public Group() {
		g = 0;
	}
	
	public Group (int g) {
		this.g = 1 << g;
	}
	
	public Group addMember(int member) {
		g = g | 1 << member;
		return this;
	}
	
	public boolean containsGroup(Group gp) {
		return gp.g == (g & gp.g);
	}
	
	public boolean containsMember(int member) {
		return g == (g & (1 << member));
	}
	
	public static void main(String[] args) {
		Group gp1 = new Group(1);
		Group gp2 = new Group(2);
		System.out.println(gp2.containsGroup(gp1));
		gp2.addMember(1);
		System.out.println(gp2.containsGroup(gp1));
		gp2.removeMember(1);
		System.out.println(gp2.containsGroup(gp1));
	}

	public Group removeMember(int member) {
		g = g ^(1 << member);
		return this;
	}
}