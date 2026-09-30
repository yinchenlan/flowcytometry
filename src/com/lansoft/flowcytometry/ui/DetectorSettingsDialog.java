package com.lansoft.flowcytometry.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.JScrollPane;

import com.lansoft.flowcytometry.model.Configuration;
import com.lansoft.flowcytometry.model.DetectorConjugates;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.awt.Dialog.ModalityType;

public class DetectorSettingsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField newNameField;
	private JTextField detectorNameField;
	private JList list;
	private JButton btnAdd;
	private JButton btnDel;
	private ConfigurationDesignerDialog parentComp;
	private JButton commitButton;


	/**
	 * Create the dialog.
	 */
	public DetectorSettingsDialog(ConfigurationDesignerDialog parentComp) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(DetectorSettingsDialog.class.getResource("/fcIcon.gif")));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		this.parentComp = parentComp;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBounds(0, 0, 442, 231);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		JLabel label = new JLabel("Detector Settings");
		label.setFont(new Font("SansSerif", Font.BOLD, 12));
		label.setBounds(17, 42, 105, 16);
		contentPanel.add(label);
		
		JLabel label_1 = new JLabel("Detector Name");
		label_1.setBounds(17, 70, 91, 16);
		contentPanel.add(label_1);
		
		JLabel label_2 = new JLabel("Add Conjugate");
		label_2.setBounds(17, 130, 91, 16);
		contentPanel.add(label_2);
		
		newNameField = new JTextField();
		newNameField.setColumns(10);
		newNameField.setBounds(120, 124, 92, 28);
		contentPanel.add(newNameField);
		
		detectorNameField = new JTextField();
		detectorNameField.setColumns(10);
		detectorNameField.setBounds(120, 64, 92, 28);
		contentPanel.add(detectorNameField);
		
		btnAdd = new JButton(">");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newName = newNameField.getText();
				boolean isValid = validateConjugateName(newName);
				if (isValid) {
					if (newName != null && newName.trim().length() > 0) {
						DefaultListModel model = (DefaultListModel) list.getModel();
						model.add(0, newName.trim());
					}
					newNameField.setText("");
					commitButton.setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(DetectorSettingsDialog.this,
						    "Conjugate Name '" + newName + "' is already used." ,
						    "Conjugate Name Exists",
						    JOptionPane.ERROR_MESSAGE);
					newNameField.requestFocus();
				}
				DetectorSettingsDialog.this.parentComp.setModified(true);
			}			
		});
		btnAdd.setBounds(237, 105, 62, 28);
		contentPanel.add(btnAdd);
		
		btnDel = new JButton("<");
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultListModel<String> model = (DefaultListModel<String>) list
						.getModel();
				int idx = list.getSelectedIndex();
				if (idx > -1)
					model.remove(idx);
				commitButton.setEnabled(true);
				DetectorSettingsDialog.this.parentComp.setModified(true);
			}
		});
		btnDel.setBounds(237, 145, 62, 28);
		contentPanel.add(btnDel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(311, 105, 100, 70);
		contentPanel.add(scrollPane);
		
		list = new JList();
		scrollPane.setViewportView(list);
		list.setVisibleRowCount(3);
		list.setValueIsAdjusting(true);
		list.setModel(new DefaultListModel<String>());
		list.setEnabled(false);
		list.setBorder(new LineBorder(new Color(0, 0, 0)));
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.WHITE);
			buttonPane.setBounds(0, 231, 434, 37);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			
			commitButton = new JButton("Done");
			buttonPane.add(commitButton);
			commitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	public JList getList() {
		return list;
	}
	public JButton getBtnAdd() {
		return btnAdd;
	}
	public JButton getBtnDel() {
		return btnDel;
	}
	public JTextField getDetectorNameField() {
		return detectorNameField;
	}
	public JTextField getNewNameField() {
		return newNameField;
	}
	
	private boolean validateConjugateName(String newName) {
		for (DetectorConjugates detector : getDetectors()) {
			for (String name : detector.getNameList()) {
				if (name.equals(newName)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public List<DetectorConjugates> getDetectors() {
		Configuration conf = getConfiguration();
		if (conf != null) {
			return getConfiguration().getDetectorConjugatesList();
		} else {
			return null;
		}
	}
	
	public Configuration getConfiguration() {
		String currentConfName = (String) parentComp.getConfigurationsComboBox()
				.getSelectedItem();
		Configuration retVal = null;
		for (Configuration conf : parentComp.getConfigurations().getConfigurationsList()) {
			if (conf.getName().equals(currentConfName)) {
				retVal = conf;
			}
		}
		return retVal;
	}	
	public JButton getCommitButton() {
		return commitButton;
	}
}
