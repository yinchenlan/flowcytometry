package com.lansoft.flowcytometry.model;

import java.util.Comparator;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "detector")
@XmlType(propOrder = { "name", "wl", "wlWindow" })
public class Detector {
	
	public static final Comparator<? super Detector> DESCENDING;
	
	static {
		DESCENDING = new Comparator<Detector>() {

			@Override
			public int compare(Detector o1, Detector o2) {
				return o2.getWl() - o1.getWl();
			}
			
		};
	}
	
	private String name;
	private int wl;
	private int wlWindow;

	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "wavelength")
	public int getWl() {
		return wl;
	}
	
	public void setWl(int wl) {
		this.wl = wl;
	}
	
	/**
	 * If wlWindow == {@link Integer#MAX_VALUE}, then
	 * it is a longpass filter.
	 * 
	 * @return
	 */
	@XmlAttribute(name = "wavelengthWindow")
	public int getWlWindow() {
		return wlWindow;
	}
	
	public void setWlWindow(int wlWindow) {
		this.wlWindow = wlWindow;
	}
	
}
