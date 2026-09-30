package com.lansoft.flowcytometry.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;

import java.awt.GridLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JScrollBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;

import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.xml.bind.JAXBException;

import com.lansoft.flowcytometry.model.BrightnessIndex;
import com.lansoft.flowcytometry.model.Configuration;
import com.lansoft.flowcytometry.model.Configurations;
import com.lansoft.flowcytometry.model.DetectorConjugates;
import com.lansoft.flowcytometry.model.Detectors;

import java.awt.Dialog.ModalityType;
import java.awt.Dialog.ModalExclusionType;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JComboBox;

import java.awt.Font;

import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * Configuration dialog.  Deprecated.
 * 
 * 
 * @author Chuck Lan
 * @deprecated
 */
public class ConfigurationDesignerDialog extends JDialog {
	private JTextField newNameField;
	private JTable table;
	private JTextField detectorNameField;
	private Configurations configurations;
	private JButton btnAdd;
	private JButton btnDel;
	private JButton btnDelDetector;
	private JList list;
	private JButton btnClose;
	private JButton commitButton;
	private JButton addDetectorButton;
	private JComboBox configurationsComboBox;
	private DefaultComboBoxModel<String> cbModel;
	private JSeparator separator_1;
	private JButton addConfButton;
	private JButton deleteConfButton;
	private JLabel lblDetectors;
	private JTextPane configurationDescriptionTextPane;
	private JLabel lblDescription;
	private JSeparator separator_3;
	private JScrollPane scrollPane_2;
	private JLabel lblBrightness;
	private JTable brightnessTable;
	private JButton addBrightnessButton;
	private JButton deleteBrightnessButton;
	private JLabel lblConfigurationName;
	private JLabel lblDescription_1;
	private JTextField configurationNameField;
	private JLabel lblName;
	private JButton configurationsCommitChangeBtn;
	private JSeparator separator_4;
	private JTextField brightnessConjugateName;
	private JButton brightnessCommitChangesButton;
	private boolean modified;
	private JSpinner brightnessSpinner;
	private DetectorSettingsDialog detectorSettingsDialog;
	private JComboBox comboBox;

	/**
	 * Create the dialog.
	 */
	public ConfigurationDesignerDialog() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ConfigurationDesignerDialog.class.getResource("/fcIcon.gif")));
		detectorSettingsDialog = new DetectorSettingsDialog(this);
		setResizable(false);
		try {
			// detectors = Detectors.getDetectors();
			configurations = Configurations.loadConfigurations();
		} catch (FileNotFoundException | JAXBException e1) {
			throw new RuntimeException(e1);
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e1);
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e1);
		}
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Designer Configurations");
		setBounds(100, 100, 950, 644);
		getContentPane().setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 0, 938, 561);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		AbstractTableModel mdl = new AbstractTableModel() {

			@Override
			public String getColumnName(int column) {
				if (column == 0)
					return "Name";
				else
					return "Conjugates";
			}

			@Override
			public int getRowCount() {
				List<DetectorConjugates> detectors = getDetectors();
				if (detectors != null) {
					return getDetectors().size();
				} else {
					return 0;
				}
			}

			@Override
			public int getColumnCount() {
				return 2;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				DetectorConjugates d = getDetectors().get(rowIndex);
				if (columnIndex == 0) {
					return d.getDetectorName();
				} else {
					StringBuffer sb = new StringBuffer();
					boolean first = true;
					for (String name : d.getNameList()) {
						if (first) {
							first = false;
						} else {
							sb.append(",");
						}
						sb.append(name);
					}
					return sb.toString();
				}
			}

		};
		
		

		JScrollPane jscp = new JScrollPane();
		jscp.setBounds(4, 237, 451, 81);
		panel_1.add(jscp);
		
				table = new JTable();
				jscp.setViewportView(table);
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setModel(mdl);
				table.setShowVerticalLines(true);
				table.setShowHorizontalLines(true);
				table.setFillsViewportHeight(true);
				
						ListSelectionModel selectionModel = table.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int idx = table.getSelectedRow();
				if (idx > -1) {
					DetectorConjugates detector = getDetectors().get(idx);
					detectorNameField.setText(detector.getDetectorName());
					DefaultListModel<String> mdl = (DefaultListModel<String>) list
							.getModel();
					mdl.clear();
					for (String name : detector.getNameList()) {
						mdl.addElement(name);
					}
					detectorNameField.setEnabled(false);
					list.setEnabled(true);
					newNameField.setEnabled(true);
				}
			}
		});

		JLabel lblAddConjugate = new JLabel("Add Conjugate");
		lblAddConjugate.setBounds(481, 302, 91, 16);
		panel_1.add(lblAddConjugate);

		newNameField = new JTextField();
		newNameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				commitButton.setEnabled(true);
			}
		});
		newNameField.setBounds(603, 296, 92, 28);
		panel_1.add(newNameField);
		newNameField.setColumns(10);
		// panel_1.add(list);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(817, 275, 100, 70);
		panel_1.add(scrollPane);
		
				list = new JList();
				scrollPane.setViewportView(list);
				list.setBorder(new LineBorder(new Color(0, 0, 0)));
				list.setValueIsAdjusting(true);
				list.setVisibleRowCount(3);
				list.setModel(new DefaultListModel<String>());

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
					JOptionPane.showMessageDialog(ConfigurationDesignerDialog.this,
						    "Conjugate Name '" + newName + "' is already used." ,
						    "Conjugate Name Exists",
						    JOptionPane.ERROR_MESSAGE);
					newNameField.requestFocus();
				}
				modified = true;
			}			
		});
		btnAdd.setBounds(730, 275, 62, 28);
		panel_1.add(btnAdd);

		btnDel = new JButton("<");
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultListModel<String> model = (DefaultListModel<String>) list
						.getModel();
				int idx = list.getSelectedIndex();
				if (idx > -1)
					model.remove(idx);
				commitButton.setEnabled(true);
				modified = true;
			}
		});
		btnDel.setBounds(730, 315, 62, 28);
		panel_1.add(btnDel);

		JLabel lblDetector = new JLabel("Detector Name");
		lblDetector.setBounds(481, 242, 91, 16);
		panel_1.add(lblDetector);

		btnDelDetector = new JButton("Delete");
		btnDelDetector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(ConfigurationDesignerDialog.this,
						"Are you sure?", "Confirmation",
						JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					int selectedRow = table.getSelectedRow();
					if (selectedRow > -1) {
						getDetectors().remove(selectedRow);
						((AbstractTableModel) table.getModel())
								.fireTableDataChanged();
						clearDetectorForm();
					}
					modified = true;
				}
			}
		});
		btnDelDetector.setBounds(354, 330, 99, 28);
		panel_1.add(btnDelDetector);

		detectorNameField = new JTextField();
		detectorNameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				commitButton.setEnabled(true);
			}
		});
		detectorNameField.setBounds(603, 236, 92, 28);
		panel_1.add(detectorNameField);
		detectorNameField.setColumns(10);

		JSeparator separator = new JSeparator();
		separator.setBounds(4, 367, 934, 2);
		panel_1.add(separator);

		addDetectorButton = new JButton("Add");
		addDetectorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//detectorSettingsDialog.setVisible(true);
				clearDetectorForm();
				detectorNameField.requestFocus();
			}
		});
		addDetectorButton.setBounds(257, 330, 90, 28);
		panel_1.add(addDetectorButton);

		JLabel lblNewLabel = new JLabel("Detector Settings");
		lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblNewLabel.setBounds(481, 214, 105, 16);
		panel_1.add(lblNewLabel);

		commitButton = new JButton("Done");
		commitButton.setEnabled(false);
		commitButton.setBounds(817, 236, 101, 28);
		panel_1.add(commitButton);
		
		AbstractTableModel bMdl = new AbstractTableModel() {

			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
				Configuration conf = getConfiguration();
				if (conf != null) {
					BrightnessIndex bi = conf.getBrightnessIndices().get(rowIndex);
					if (columnIndex == 0) {
						bi.setConjugateName((String)aValue);
					} else {
						bi.setBrightness(new Float((String)aValue));
					}
				}
			}
			
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
		        return false;
		    }
			
			@Override
			public String getColumnName(int column) {
				if (column == 0)
					return "Conjugate";
				else
					return "Brightness";
			}

			@Override
			public int getRowCount() {
				Configuration conf = getConfiguration();
				if (conf != null) {
					return getConfiguration().getBrightnessIndices().size();
				} else {
					return 0;
				}
			}

			@Override
			public int getColumnCount() {
				return 2;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				Configuration conf = getConfiguration();
				if (conf != null) {
					BrightnessIndex bi = conf.getBrightnessIndices().get(rowIndex);
									
					if (columnIndex == 0) {
						return bi.getConjugateName();
					} else {
						return bi.getBrightness();
					}
				} else {
					return null;
				}
			}

		};

		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(4, 415, 451, 87);
		panel_1.add(scrollPane_2);
		brightnessTable = new JTable();
		scrollPane_2.setViewportView(brightnessTable);
		brightnessTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		brightnessTable.setModel(bMdl);
		brightnessTable.setShowVerticalLines(true);
		brightnessTable.setShowHorizontalLines(true);
		brightnessTable.setFillsViewportHeight(true);
		ListSelectionModel bSelectionModel = brightnessTable.getSelectionModel();
		bSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int idx = brightnessTable.getSelectedRow();
				if (idx > -1) {
					BrightnessIndex bi = getBrightnessIndices().get(idx);
					brightnessConjugateName.setText(bi.getConjugateName());
					brightnessSpinner.setValue(bi.getBrightness());
					brightnessConjugateName.setEnabled(false);
					brightnessSpinner.setEnabled(true);
				}
			}
		});
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(698, 69, 219, 58);
		panel_1.add(scrollPane_1);
		configurationDescriptionTextPane = new JTextPane();
		configurationDescriptionTextPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				configurationsCommitChangeBtn.setEnabled(true);
			}
		});
		scrollPane_1.setViewportView(configurationDescriptionTextPane);
		configurationDescriptionTextPane.setEnabled(false);
		cbModel = new DefaultComboBoxModel<String>();
		configurationsComboBox = new JComboBox(cbModel);
		configurationNameField = new JTextField();
		configurationNameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				configurationsCommitChangeBtn.setEnabled(true);
			}
		});
		configurationNameField.setEnabled(false);
		configurationNameField.setBounds(481, 69, 122, 28);
		panel_1.add(configurationNameField);
		configurationNameField.setColumns(10);
		configurationsComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((AbstractTableModel) table.getModel())
				.fireTableDataChanged();
				((AbstractTableModel) brightnessTable.getModel())
				.fireTableDataChanged();
				configurationDescriptionTextPane.setText(getConfiguration().getDescription());
				configurationDescriptionTextPane.setEnabled(true);
				configurationNameField.setText(getConfiguration().getName());
				clearDetectorForm();
				disableDetectorForm();
			}
		});
		configurationsComboBox.setBounds(4, 69, 210, 26);
		panel_1.add(configurationsComboBox);
		loadConfigurations();

		JLabel lblConfigurations = new JLabel("Configurations");
		lblConfigurations.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblConfigurations.setBounds(4, 13, 90, 16);
		panel_1.add(lblConfigurations);

		separator_1 = new JSeparator();
		separator_1.setBounds(4, 554, 934, 2);
		panel_1.add(separator_1);

		addConfButton = new JButton("Add");
		addConfButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearConfigurationForm();
				configurationNameField.setEnabled(true);
				configurationDescriptionTextPane.setEnabled(true);
				configurationNameField.requestFocus();
			}
		});
		addConfButton.setBounds(253, 70, 90, 28);
		panel_1.add(addConfButton);

		deleteConfButton = new JButton("Delete");
		deleteConfButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(ConfigurationDesignerDialog.this,
						"Are you sure?", "Confirmation",
						JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					String selectedItem = (String) cbModel.getSelectedItem();
					if (selectedItem != null) {
						cbModel.removeElement(selectedItem);
						removeConfiguration(selectedItem);
					}
					modified = true;
				}
			}

		});
		deleteConfButton.setBounds(355, 69, 100, 28);
		panel_1.add(deleteConfButton);

		lblDetectors = new JLabel("Detectors");
		lblDetectors.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblDetectors.setBounds(4, 209, 67, 16);
		panel_1.add(lblDetectors);
		
		lblDescription = new JLabel("Configuration Settings");
		lblDescription.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblDescription.setBounds(481, 13, 134, 16);
		panel_1.add(lblDescription);
		
		
		
		
		
		
		lblBrightness = new JLabel("Brightness");
		lblBrightness.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblBrightness.setBounds(6, 387, 90, 16);
		panel_1.add(lblBrightness);
		
		addBrightnessButton = new JButton("Add");
		addBrightnessButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enableBrightnessForm();
				clearBrightnessForm();
			}
		});
		addBrightnessButton.setBounds(257, 514, 90, 28);
		panel_1.add(addBrightnessButton);

		deleteBrightnessButton = new JButton("Delete");
		deleteBrightnessButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(ConfigurationDesignerDialog.this,
						"Are you sure?", "Confirmation",
						JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					int selectedRow = brightnessTable.getSelectedRow();
					if (selectedRow > -1) {
						getBrightnessIndices().remove(selectedRow);
						((AbstractTableModel) brightnessTable.getModel())
								.fireTableDataChanged();
						clearBrightnessForm();
					}
					modified = true;
				}
			}
		});
		deleteBrightnessButton.setBounds(354, 514, 101, 28);
		panel_1.add(deleteBrightnessButton);
		
		lblConfigurationName = new JLabel("Configuration Name");
		lblConfigurationName.setBounds(7, 48, 123, 16);
		panel_1.add(lblConfigurationName);
		
		lblDescription_1 = new JLabel("Description");
		lblDescription_1.setBounds(698, 48, 105, 16);
		panel_1.add(lblDescription_1);
		
		
		lblName = new JLabel("Name");
		lblName.setBounds(481, 48, 55, 16);
		panel_1.add(lblName);
		
		configurationsCommitChangeBtn = new JButton("Done");
		configurationsCommitChangeBtn.setEnabled(false);
		configurationsCommitChangeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Configuration conf = new Configuration();
				conf.setName(configurationNameField.getText());
				conf.setDescription(configurationDescriptionTextPane.getText());
				boolean found = false;
				for (Configuration c : configurations.getConfigurationsList()) {
					if (c.getName().equals(conf.getName())) {
						found = true;
						c.setDescription(configurationDescriptionTextPane.getText());
						break;
					}
				}
				if (!found) {
					configurations.getConfigurationsList().add(conf);
					cbModel.addElement(conf.getName());
				}
				disableConfigurationForm();
				cbModel.setSelectedItem(configurationNameField.getText());
				configurationsCommitChangeBtn.setEnabled(false);
				modified = true;
			}
		});
		configurationsCommitChangeBtn.setBounds(817, 145, 100, 28);
		panel_1.add(configurationsCommitChangeBtn);
		
		separator_4 = new JSeparator();
		separator_4.setBounds(4, 373, 934, 2);
		panel_1.add(separator_4);
		
		brightnessConjugateName = new JTextField();
		brightnessConjugateName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				brightnessCommitChangesButton.setEnabled(true);
			}
		});
		brightnessConjugateName.setEnabled(false);
		brightnessConjugateName.setBounds(795, 426, 122, 28);
		panel_1.add(brightnessConjugateName);
		brightnessConjugateName.setColumns(10);
		
		JLabel lblConjugate = new JLabel("Conjugate");
		lblConjugate.setBounds(647, 432, 75, 16);
		panel_1.add(lblConjugate);
		
		JLabel lblBrightness_1 = new JLabel("Brightness");
		lblBrightness_1.setBounds(647, 474, 75, 16);
		panel_1.add(lblBrightness_1);
		
		brightnessCommitChangesButton = new JButton("Done");
		brightnessCommitChangesButton.setEnabled(false);
		brightnessCommitChangesButton.setBounds(817, 514, 100, 28);
		brightnessCommitChangesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = brightnessConjugateName.getText();
				Float brightness = (Float) brightnessSpinner.getValue();
				boolean isValid = addBrightnessIndex(name, brightness);
				if (!isValid) {
					JOptionPane.showMessageDialog(ConfigurationDesignerDialog.this,
						    "Brightness for conjugate '" + name + "' is already defined.",
						    "Conjugate Brightness Already Defined",
						    JOptionPane.ERROR_MESSAGE);
					brightnessConjugateName.requestFocus();
				}
				modified = true;
			}
		});
		panel_1.add(brightnessCommitChangesButton);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(4, 187, 934, 2);
		panel_1.add(separator_2);
		
		JSeparator separator_6 = new JSeparator();
		separator_6.setBounds(4, 181, 934, 2);
		panel_1.add(separator_6);
		
		
		brightnessSpinner = new JSpinner(new SpinnerNumberModel(new Float(5), new Float(1), new Float(10), new Float(1)));
		brightnessSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				brightnessCommitChangesButton.setEnabled(true);
			}
		});
		brightnessSpinner.setBounds(795, 466, 122, 34);
		panel_1.add(brightnessSpinner);
		
		comboBox = new JComboBox();
		comboBox.setBounds(484, 336, 210, 26);
		panel_1.add(comboBox);
		commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (detectorNameField.getText() == null
						|| detectorNameField.getText().trim().equals(""))
					return;
				DetectorConjugates detector = new DetectorConjugates();
				detector.setDetectorName(detectorNameField.getText());
				detector.setNameList(new ArrayList<String>());
				DefaultListModel<String> model = (DefaultListModel<String>) list
						.getModel();
				for (int idx = 0; idx < model.getSize(); idx++) {
					detector.getNameList().add(model.getElementAt(idx));
					// Also create brightness index defaulted to 5
					addBrightnessIndex(model.getElementAt(idx), new Float(5));
				}
				boolean found = false;
				List<DetectorConjugates> detectors = getDetectors();
				for (int idx = 0; idx < detectors.size(); idx++) {
					DetectorConjugates det = getDetectors().get(idx);
					if (det.getDetectorName()
							.equals(detector.getDetectorName())) {
						detectors.remove(idx);
						detectors.add(idx, detector);
						found = true;
						break;
					}
				}
				if (!found)
					detectors.add(detector);
				((AbstractTableModel) table.getModel()).fireTableDataChanged();
				clearDetectorForm();
				disableDetectorForm();
				commitButton.setEnabled(false);
				modified = true;
			}
		});

		JButton btnAddDetector = new JButton("Save");
		btnAddDetector.setBounds(730, 573, 91, 28);
		getContentPane().add(btnAddDetector);

		btnClose = new JButton("Close");
		btnClose.setBounds(833, 573, 90, 28);
		getContentPane().add(btnClose);
		
		separator_3 = new JSeparator();
		separator_3.setBounds(461, 232, 1, 2);
		getContentPane().add(separator_3);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (modified) {
					int n = JOptionPane.showConfirmDialog(
						    ConfigurationDesignerDialog.this,
						    "Configuration has changed.  Continue?",
						    "Confirm Exit",
						    JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.YES_OPTION) {
						ConfigurationDesignerDialog.this.setVisible(false);
					}
				} else {
					ConfigurationDesignerDialog.this.setVisible(false);
				}
			}
		});
		btnAddDetector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Configurations.saveConfigurations(configurations);
				} catch (IOException | JAXBException e1) {
					throw new RuntimeException(e1);
				}
				modified = false;
				JOptionPane.showMessageDialog(ConfigurationDesignerDialog.this,
						"File Saved", "File Saved",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		disableDetectorForm();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	public JTextField getDetectorNameField() {
		return detectorNameField;
	}

	public JButton getBtnAdd() {
		return btnAdd;
	}

	public JButton getBtnDel() {
		return btnDel;
	}

	public JTextField getNewNameField() {
		return newNameField;
	}

	public JButton getBtnDelDetector() {
		return btnDelDetector;
	}

	public JList getList() {
		return list;
	}

	public JTable getTable() {
		return table;
	}

	public JButton getBtnClose() {
		return btnClose;
	}

	public void clearDetectorForm() {
		detectorNameField.setText("");
		detectorNameField.setEnabled(true);
		newNameField.setText("");
		newNameField.setEnabled(true);
		((DefaultListModel<String>) list.getModel()).clear();
		list.setEnabled(true);
	}
	
	public void clearConfigurationForm() {
		configurationNameField.setText("");
		configurationNameField.setEnabled(true);
		configurationDescriptionTextPane.setText("");
		configurationDescriptionTextPane.setEnabled(true);
	}
	
	public void disableConfigurationForm() {
		configurationNameField.setEnabled(false);
		//configurationDescriptionTextPane.setEnabled(false);
	}
	
	public void clearBrightnessForm() {
		brightnessConjugateName.setText("");
		brightnessConjugateName.setEnabled(true);
		brightnessSpinner.setValue(new Float(.5));
		brightnessSpinner.setEnabled(true);
	}

	public void disableDetectorForm() {
		detectorNameField.setEnabled(false);
		newNameField.setEnabled(false);
		list.setEnabled(false);
	}
	
	public void enableBrightnessForm() {
		brightnessConjugateName.setEnabled(true);
		brightnessConjugateName.setEditable(true);
		brightnessSpinner.setEnabled(true);
	}
	
	public void disableBrightnessForm() {
		brightnessConjugateName.setEnabled(false);
		brightnessSpinner.setEnabled(false);
	}

	public void loadConfigurations() {
		try {
			configurations = Configurations.loadConfigurations();
			for (Configuration conf : configurations.getConfigurationsList()) {
				cbModel.addElement(conf.getName());
			}
		} catch (JAXBException | IOException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	public JButton getCommitButton() {
		return commitButton;
	}

	public JButton getAddDetectorButton() {
		return addDetectorButton;
	}

	public JComboBox getConfigurationsComboBox() {
		return configurationsComboBox;
	}

	public JButton getAddConfButton() {
		return addConfButton;
	}

	public JButton getDeleteConfButton() {
		return deleteConfButton;
	}

	public List<DetectorConjugates> getDetectors() {
		Configuration conf = getConfiguration();
		if (conf != null) {
			return getConfiguration().getDetectorConjugatesList();
		} else {
			return null;
		}
	}
	
	public List<BrightnessIndex> getBrightnessIndices() {
		return getConfiguration().getBrightnessIndices();
	}
	
	public Configuration getConfiguration() {
		String currentConfName = (String) configurationsComboBox
				.getSelectedItem();
		Configuration retVal = null;
		for (Configuration conf : configurations.getConfigurationsList()) {
			if (conf.getName().equals(currentConfName)) {
				retVal = conf;
			}
		}
		return retVal;
	}	
	
	public JTextPane getConfigurationDescriptionTextPane() {
		return configurationDescriptionTextPane;
	}
	public JTable getBrightnessTable() {
		return brightnessTable;
	}
	public JButton getAddBrightnessButton() {
		return addBrightnessButton;
	}
	public JButton getDeleteBrightnessButton() {
		return deleteBrightnessButton;
	}
	public JTextField getConfigurationNameField() {
		return configurationNameField;
	}
	public JTextField getBrightnessConjugateName() {
		return brightnessConjugateName;
	}
	
	public JButton getBrightnessCommitChangesButton() {
		return brightnessCommitChangesButton;
	}
	public JButton getConfigurationsCommitChangeBtn() {
		return configurationsCommitChangeBtn;
	}

	private void removeConfiguration(String selectedItem) {
		int index = -1;
		for (int idx = 0; idx < configurations.getConfigurationsList().size(); idx++) {
			Configuration conf = configurations.getConfigurationsList().get(idx);
			if (conf.getName().equals(selectedItem)) {
				index = idx;
				break;
			}
		}
		configurations.getConfigurationsList().remove(index);
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
	
	private boolean validateBrightnessIndex(String name) {
		if (brightnessConjugateName.isEnabled()) {
			for (BrightnessIndex idx : getBrightnessIndices()) {
				if (name.equals(idx.getConjugateName())) {
					return false;
				}
			}
		}
		return true;
	}
	public JSpinner getBrightnessSpinner() {
		return brightnessSpinner;
	}

	public Configurations getConfigurations() {
		return configurations;
	}

	public void setConfigurations(Configurations configurations) {
		this.configurations = configurations;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}
	
	public boolean addBrightnessIndex(String name, Float brightness) {
		if (name == null
				|| name.trim().equals(""))
			return false;
		boolean isValid = validateBrightnessIndex(name);
		if (isValid) {
			BrightnessIndex bi = new BrightnessIndex();
			bi.setConjugateName(name);
			bi.setBrightness(brightness);
			boolean found = false;
			List<BrightnessIndex> bis = getBrightnessIndices();
			for (int idx = 0; idx < bis.size(); idx++) {
				BrightnessIndex bIndex = bis.get(idx);
				if (bIndex.getConjugateName()
						.equals(bi.getConjugateName())) {
					bis.remove(idx);
					bis.add(idx, bi);
					found = true;
					break;
				}
			}
			if (!found)
				bis.add(bi);
			((AbstractTableModel) brightnessTable.getModel()).fireTableDataChanged();
			clearBrightnessForm();
			disableBrightnessForm();
			brightnessCommitChangesButton.setEnabled(false);
		} else {
			return false;
		}
		return true;
	}
}
