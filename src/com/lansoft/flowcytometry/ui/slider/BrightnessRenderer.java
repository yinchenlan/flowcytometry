package com.lansoft.flowcytometry.ui.slider;

import java.awt.Component;

import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import com.lansoft.flowcytometry.ui.brightness.BrightnessValue;

public class BrightnessRenderer extends JSlider implements TableCellRenderer{

	public static final int MAX = 10;
	public static final int MIN = 1;
	
	public BrightnessRenderer() {
		super(SwingConstants.HORIZONTAL);
		setMaximum(MAX);
		setMinimum(MIN);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value == null) {
			return this;
		}
		if (value instanceof BrightnessValue) {
			setValue(((BrightnessValue) value).getBrightness());
		} else {
			setValue(0);
		}
		return this;
	}
}
