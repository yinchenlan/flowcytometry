package com.lansoft.flowcytometry.ui.brightness;

public class BrightnessValue {
	
	/**
	 * Brightness value from 1 - 10
	 */
	private int brightness;

	public BrightnessValue(int val) {
		setBrightness(val);
	}
	
	public int getBrightness() {
		return brightness;
	}

	public void setBrightness(int val) {
		this.brightness = val < 0 ? 1 : val > 10 ? 10 : val;
	}
	
	
}
