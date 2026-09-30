package com.lansoft.flowcytometry.ui.slider;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.lansoft.flowcytometry.ui.brightness.BrightnessValue;

public class BrightnessEditor extends DefaultCellEditor {

	public static final int MAX = 10;
	public static final int MIN = 1;
	
	protected JSlider slider;

	public BrightnessEditor() {
		super(new JCheckBox());
		slider = new BrightnessRenderer();
		slider.setOpaque(true);
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if (isSelected) {
			slider.setForeground(table.getSelectionForeground());
			slider.setBackground(table.getSelectionBackground());
		} else {
			slider.setForeground(table.getForeground());
			slider.setBackground(table.getBackground());
		}
		slider.setValue(((BrightnessValue) value).getBrightness());

		return slider;
	}

	public Object getCellEditorValue() {
		return new Integer(slider.getValue());
	}

	public boolean stopCellEditing() {
		return super.stopCellEditing();
	}

	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
}