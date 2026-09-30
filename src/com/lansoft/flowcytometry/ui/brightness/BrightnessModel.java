package com.lansoft.flowcytometry.ui.brightness;

import java.util.Arrays;

import javax.swing.table.AbstractTableModel;

public class BrightnessModel extends AbstractTableModel {
	
	String[] headers = { "Conjugate", "Brightness"};

	Class[] columnClasses = { String.class, BrightnessValue.class };

	Object[][] data;
	
	public BrightnessModel(Object[][] d) {
		data = new Object[d.length][];
		for (int i = 0; i < d.length; i++) {
			data[i] = Arrays.copyOf(d[i], d[i].length);
		}
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public int getColumnCount() {
		return headers.length;
	}

	@SuppressWarnings("unchecked")
	public Class getColumnClass(int c) {
	    return columnClasses[c];
	}
	
	public String getColumnName(int c) {
		return headers[c];
	}
	
	public boolean isCellEditable(int r, int c) {
		return c == 1 ? true : false;
	}
	
	@Override
	public Object getValueAt(int r, int c) {
		return data[r][c];
	}

	public void setValueAt(Object value, int r, int c) {
		if (c == 1) {
			((BrightnessValue) data[r][c]).setBrightness((Integer) value);
		} 
	}
}
