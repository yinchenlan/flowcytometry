package com.lansoft.flowcytometry.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.xml.bind.JAXBException;

import com.lansoft.flowcytometry.model.Antibodies;
import com.lansoft.flowcytometry.model.Antibody;
import com.lansoft.flowcytometry.model.AntibodyDatabases;

import java.awt.Color;

public class AntibodiesConfiguration extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JButton btnClose;
	private JTable antibodiesTable;
	private JButton csvImportButton;
	JFileChooser fc = new JFileChooser();
	private AntibodyDatabases antibodyDatabases;
	AbstractTableModel abMdl;
	private JButton excelImportButton;
	private JComboBox databaseNameBox;
	private DefaultComboBoxModel<String> cbModel;
	private JButton deleteButton;
	private boolean modified;
	private JButton saveButton;
	
	/**
	 * Create the dialog.
	 */
	public AntibodiesConfiguration(Frame frame, String title) {
		super(frame, title);
		setIconImage(Toolkit.getDefaultToolkit().getImage(AntibodiesConfiguration.class.getResource("/fcIcon.gif")));
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Antibodies Configurations");
		/*
		 * addConjugatesDialog.getBtnClose().addActionListener(new
		 * ActionListener() { public void actionPerformed(ActionEvent e) { } });
		 */
		setBounds(100, 100, 1071, 603);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (modified) {
					int n = JOptionPane.showConfirmDialog(
						    AntibodiesConfiguration.this,
						    "Database configuration has changed.  Continue?",
						    "Confirm Exit",
						    JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.YES_OPTION) {
						AntibodiesConfiguration.this.setVisible(false);
					}
				} else {
					AntibodiesConfiguration.this.setVisible(false);
				}
			}
		});
		btnClose.setBounds(971, 536, 89, 27);
		contentPanel.add(btnClose);

		try {
			antibodyDatabases = AntibodyDatabases.getAntibodyDatabases();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		cbModel = new DefaultComboBoxModel<String>();
		abMdl = new AbstractTableModel() {

			@Override
			public void setValueAt(Object aValue, int rowIndex,
					int columnIndex) {
				Antibodies antibodies = getSelectedAntibodies();
				Antibody ab = antibodies.getAntibodiesList().get(rowIndex);
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return true;
			}

			@Override
			public String getColumnName(int column) {
				if (column == 0)
					return "Conjugate";
				else if (column == 1)
					return "Target";
				else if (column == 2)
					return "Target Species";
				else if (column == 3)
					return "Company";
				else if (column == 4)
					return "Catalog #";
				else if (column == 5)
					return "Lot #";
				else if (column == 6)
					return "Clone";
				else if (column == 7)
					return "Isotype";
				else if (column == 8)
					return "AUPS (ul)";
				else
					return null;
			}

			@Override
			public int getRowCount() {
				Antibodies abs = getSelectedAntibodies();
				if (abs != null) {
					return getSelectedAntibodies().getAntibodiesList().size();
				} else {
					return 0;
				}
			}

			@Override
			public int getColumnCount() {
				return 9;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				Antibody ab = getSelectedAntibodies().getAntibodiesList().get(rowIndex);
				if (columnIndex == 0) {
					return ab.getConjugateName();
				} else if (columnIndex == 1) {
					return ab.getTargetName();
				} else if (columnIndex == 2) {
					return ab.getTargetSpecies();
				} else if (columnIndex == 3) {
					return ab.getCompany();
				} else if (columnIndex == 4) {
					return ab.getCatalogNum();
				} else if (columnIndex == 5) {
					return ab.getLotNum();
				} else if (columnIndex == 6) {
					return ab.getClone();
				} else if (columnIndex == 7) {
					return ab.getIsotype();
				} else if (columnIndex == 8) {
					return ab.getAups();
				} else {
					return null;
				}
			}

		};
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(6, 156, 1053, 368);
			contentPanel.add(scrollPane);
			antibodiesTable = new JTable();
			scrollPane.setViewportView(antibodiesTable);
			antibodiesTable.setAutoCreateRowSorter(true);
			antibodiesTable.setFillsViewportHeight(true);
			
						antibodiesTable.setModel(abMdl);
			
			{

			}
		}
		{
			JLabel lblAntibodies = new JLabel("Antibodies");
			lblAntibodies.setBounds(6, 131, 79, 16);
			contentPanel.add(lblAntibodies);
		}
		{
			csvImportButton = new JButton("Import CSV");
			csvImportButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					FileNameExtensionFilter filter = new FileNameExtensionFilter(
							"Comma Separated Values", "csv");
					fc.setFileFilter(filter);
					int returnVal = fc
							.showOpenDialog(AntibodiesConfiguration.this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File f = fc.getSelectedFile();
						if (f != null) {
							String s = (String)JOptionPane.showInputDialog(
				                    AntibodiesConfiguration.this,
				                    "Please enter Database Name",
				                    "Database Name",
				                    JOptionPane.PLAIN_MESSAGE,
				                    null,
				                    null,
				                    null);
							if (s != null) {
								Antibodies antibodies = Antibodies.importCsvFile(f);
								antibodies.setName(s);
								antibodyDatabases.getAntibodies().add(antibodies);
								cbModel.addElement(s);
								cbModel.setSelectedItem(s);
								try {
									AntibodyDatabases.saveAntibodyDatabases(antibodyDatabases);
								} catch (Exception exc) {
									throw new RuntimeException(exc);
								}
								abMdl.fireTableDataChanged();
							}
						}
					}

				}
			});
			csvImportButton.setBounds(756, 536, 108, 28);
			contentPanel.add(csvImportButton);
		}		
		databaseNameBox = new JComboBox(cbModel);
		databaseNameBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abMdl.fireTableDataChanged();
			}
		});
		databaseNameBox.setBounds(6, 66, 227, 26);
		loadDatabases();
		contentPanel.add(databaseNameBox);

		JLabel lblDatabaseName = new JLabel("Database Name");
		lblDatabaseName.setBounds(6, 38, 119, 16);
		contentPanel.add(lblDatabaseName);

		excelImportButton = new JButton("Import Excel");
		excelImportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Excel Workbook", "xls");
				fc.setFileFilter(filter);
				int returnVal = fc.showOpenDialog(AntibodiesConfiguration.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					if (f != null) {
						String s = (String)JOptionPane.showInputDialog(
			                    AntibodiesConfiguration.this,
			                    "Please enter Database Name",
			                    "Database Name",
			                    JOptionPane.PLAIN_MESSAGE,
			                    null,
			                    null,
			                    null);
						if (s != null) {
							Antibodies antibodies = Antibodies.importExcelFile(f);
							antibodies.setName(s);
							antibodyDatabases.getAntibodies().add(antibodies);
							cbModel.addElement(s);
							cbModel.setSelectedItem(s);
							try {
								AntibodyDatabases.saveAntibodyDatabases(antibodyDatabases);
							} catch (Exception exc) {
								throw new RuntimeException(exc);
							}
							abMdl.fireTableDataChanged();
						}
					}
				}
			}
		});
		excelImportButton.setBounds(636, 536, 108, 28);
		contentPanel.add(excelImportButton);
		
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(AntibodiesConfiguration.this,
						"Are you sure?", "Confirmation",
						JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					String selectedItem = (String) cbModel.getSelectedItem();
					if (selectedItem != null) {
						String dbName = (String) cbModel.getSelectedItem();
						int removeIdx = -1;
						List<Antibodies> abs = antibodyDatabases.getAntibodies();
						for (int idx = 0; idx < abs.size(); idx++) {
							Antibodies ab = abs.get(idx);
							if (ab.getName().equals(dbName)) {
								removeIdx = idx;
								break;
							}
						}
						if (removeIdx > -1) {
							abs.remove(removeIdx);
							modified = true;
							cbModel.removeElementAt(removeIdx);
						}
					}
					modified = true;
				}
			}
		});
		deleteButton.setBounds(242, 64, 93, 30);
		contentPanel.add(deleteButton);
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					AntibodyDatabases.saveAntibodyDatabases(antibodyDatabases);
					modified = false;
				} catch (JAXBException | IOException e1) {
					throw new RuntimeException(e1);
				}
			}
		});
		saveButton.setBounds(875, 537, 85, 25);
		contentPanel.add(saveButton);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	public JButton getBtnClose() {
		return btnClose;
	}

	public JTable getAntibodiesTable() {
		return antibodiesTable;
	}

	public JButton getBtnImport() {
		return csvImportButton;
	}

	public JButton getExcelImportButton() {
		return excelImportButton;
	}

	public JComboBox getDatabaseNameBox() {
		return databaseNameBox;
	}

	public void loadDatabases() {
		try {
			AntibodyDatabases adb = AntibodyDatabases.getAntibodyDatabases();
			for (Antibodies ab : adb.getAntibodies()) {
				cbModel.addElement(ab.getName());
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
	
	public Antibodies getSelectedAntibodies() {
		String name = (String) cbModel.getSelectedItem();
		for (Antibodies ab : antibodyDatabases.getAntibodies()) {
			if (ab.getName() != null && ab.getName().equals(name)) {
				return ab;
			}
		}
		return null;
	}
	public JButton getDeleteButton() {
		return deleteButton;
	}
	public JButton getSaveButton() {
		return saveButton;
	}
}
