package com.lansoft.flowcytometry.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.xml.bind.JAXBException;

import com.lansoft.flowcytometry.model.Antibodies;
import com.lansoft.flowcytometry.model.AntibodyDatabases;
import com.lansoft.flowcytometry.model.Configuration;
import com.lansoft.flowcytometry.model.Configurations;
import com.lansoft.flowcytometry.model.FlowCytometer;
import com.lansoft.flowcytometry.model.FlowCytometers;

import java.awt.Dialog.ModalityType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.Color;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class AntibodyDatabaseAndConfigurationDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JComboBox configurationComboBox;
	private JComboBox availableDatabasesList;
	private JButton databaseAddButton;
	private JButton databaseRemoveButton;
	private JList selectedDatabaseList;
	private JButton nextButton;
	private DefaultComboBoxModel<String> flowCytometerComboBoxModel;
	private DefaultComboBoxModel<String> availableDatabasesListModel;
	private JButton cancelButton;
	private DefaultListModel<String> selectedDatabaseListModel;
	private JCheckBox chckbxCustomConfig;
	
	private ChooseTargetsDialog chooseTargetsDialog;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AntibodyDatabaseAndConfigurationDialog dialog = new AntibodyDatabaseAndConfigurationDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AntibodyDatabaseAndConfigurationDialog() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(AntibodyDatabaseAndConfigurationDialog.class.getResource("/fcIcon.gif")));
		addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {
				loadFlowCytometers();
				loadDatabases();
			}
			public void windowLostFocus(WindowEvent e) {
			}
		});
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Antibody Panel Configuration");
		setBounds(100, 100, 405, 320);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 397, 239);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		JLabel lblSelectDatabases = new JLabel("Select Databases");
		lblSelectDatabases.setBounds(6, 41, 132, 16);
		contentPanel.add(lblSelectDatabases);
		
		databaseAddButton = new JButton(">");
		databaseAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedDatabaseListModel.addElement((String) availableDatabasesListModel.getSelectedItem());
			}
		});
		databaseAddButton.setBounds(163, 69, 90, 28);
		contentPanel.add(databaseAddButton);
		
		databaseRemoveButton = new JButton("<");
		databaseRemoveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedDatabaseListModel.removeElement(selectedDatabaseList.getSelectedValue());
			}
		});
		databaseRemoveButton.setBounds(163, 109, 90, 28);
		contentPanel.add(databaseRemoveButton);
		
		availableDatabasesListModel = new DefaultComboBoxModel<String>();
		availableDatabasesList = new JComboBox(availableDatabasesListModel);
		availableDatabasesList.setBounds(6, 69, 145, 26);
		contentPanel.add(availableDatabasesList);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(265, 68, 119, 68);
		contentPanel.add(scrollPane);
		
		selectedDatabaseList = new JList();
		selectedDatabaseListModel = new DefaultListModel<String>();
		selectedDatabaseList.setModel(selectedDatabaseListModel);
		scrollPane.setViewportView(selectedDatabaseList);
		
		flowCytometerComboBoxModel = new DefaultComboBoxModel<String>();
		configurationComboBox = new JComboBox(flowCytometerComboBoxModel);
		configurationComboBox.setBounds(6, 213, 145, 26);
		contentPanel.add(configurationComboBox);
		
		JLabel lblNewLabel = new JLabel("Select Flow Cytometer");
		lblNewLabel.setBounds(6, 185, 145, 16);
		contentPanel.add(lblNewLabel);
		
		chckbxCustomConfig = new JCheckBox("Custom Config");
		chckbxCustomConfig.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					chooseTargetsDialog.setIsCustomConfiguration(true);
				} else {
					chooseTargetsDialog.setIsCustomConfiguration(false);
				}
				loadFlowCytometers();
			}
		});
		chckbxCustomConfig.setBounds(6, 152, 131, 18);
		contentPanel.add(chckbxCustomConfig);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(10, 237, 387, 40);
			getContentPane().add(buttonPane);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				nextButton = new JButton("Next");
				buttonPane.add(nextButton);
				nextButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						chooseTargetsDialog.setSelectedFlowCytometer(getSelectedFlowCytometer());
						chooseTargetsDialog.setSelectedDatabases(getSelectedDatabases());
						chooseTargetsDialog.initializePrefixTree();
						chooseTargetsDialog.populateDropDowns();
						chooseTargetsDialog.setVisible(true);
					}
				});
				nextButton.setActionCommand("Cancel");
			}
			
			cancelButton = new JButton("Close");
			buttonPane.add(cancelButton);
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selectedDatabaseListModel.clear();
					flowCytometerComboBoxModel.setSelectedItem(null);
					availableDatabasesListModel.setSelectedItem(null);
					AntibodyDatabaseAndConfigurationDialog.this.setVisible(false);
				}
			});
		}
		loadDatabases();
		chooseTargetsDialog = new ChooseTargetsDialog();
		loadFlowCytometers();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}
	protected List<String> getSelectedDatabases() {
		List<String> retVal = new ArrayList<String>(selectedDatabaseListModel.getSize());
		for (int idx = 0; idx < selectedDatabaseListModel.getSize(); idx++) {
			retVal.add(selectedDatabaseListModel.getElementAt(idx));
		}
		return retVal;
	}

	protected String getSelectedFlowCytometer() {
		return (String) flowCytometerComboBoxModel.getSelectedItem();
	}

	public JComboBox getConfigurationComboBox() {
		return configurationComboBox;
	}
	public JComboBox getAvailableDatabasesList() {
		return availableDatabasesList;
	}
	public JButton getDatabaseAddButton() {
		return databaseAddButton;
	}
	public JButton getDatabaseRemoveButton() {
		return databaseRemoveButton;
	}
	public JList getSelectedDatabaseList() {
		return selectedDatabaseList;
	}
	public JButton getNextButton() {
		return nextButton;
	}
	
	public void loadFlowCytometers() {
		try {
			flowCytometerComboBoxModel.removeAllElements();
			if (chooseTargetsDialog.isCustomConfiguration()) {
				Configurations configs = Configurations.loadConfigurations();
				for (Configuration c : configs.getConfigurationsList()) {
					flowCytometerComboBoxModel.addElement(c.getName());
				}
			} else {
				FlowCytometers fctmrs = FlowCytometers.loadFlowCytometers();
				for (FlowCytometer fctmr : fctmrs.getFlowCytometers()) {
					flowCytometerComboBoxModel.addElement(fctmr.getName());
				}
			}
		} catch (JAXBException | FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	public void loadDatabases() {
		try {
			availableDatabasesListModel.removeAllElements();
			AntibodyDatabases dbs = AntibodyDatabases.getAntibodyDatabases();
			for (Antibodies ab : dbs.getAntibodies()) {
				availableDatabasesListModel.addElement(ab.getName());
			}
		} catch (JAXBException | FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	public JButton getCancelButton() {
		return cancelButton;
	}
	public JCheckBox getChckbxCustomConfig() {
		return chckbxCustomConfig;
	}
}
