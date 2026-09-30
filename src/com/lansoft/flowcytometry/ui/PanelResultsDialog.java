package com.lansoft.flowcytometry.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.lansoft.flowcytometry.model.Antibodies;
import com.lansoft.flowcytometry.model.Antibody;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import jxl.write.WriteException;
import javax.swing.JLabel;
import java.awt.SystemColor;

public class PanelResultsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable resultsTable;
	private AbstractTableModel antibodiesTableModel;
	private List<List<List<Antibody>>> results;
	private int currIdx;
	private int resultSetIdx;
	private JButton prevButton;
	private JButton nextButton;
	private JButton exportButton;
	private JButton closeButton;
	private JButton prevSetButton;
	private JButton nextSetButton;
	private JLabel lblNewLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PanelResultsDialog dialog = new PanelResultsDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PanelResultsDialog() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(PanelResultsDialog.class.getResource("/fcIcon.gif")));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				updateNavButtonsStatuses();
			}
		});
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Panel Results");
		setBounds(100, 100, 1102, 521);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 1076, 423);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		antibodiesTableModel = new AbstractTableModel() {

			@Override
			public void setValueAt(Object aValue, int rowIndex,
					int columnIndex) {
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
				else if (column == 9) 
					return "Brightness";
				else if (column == 10) 
					return "Density";
				else
					return null;
			}

			@Override
			public int getRowCount() {
				if (results != null && results.size() > 0) {
					if (results.get(resultSetIdx) != null && results.get(resultSetIdx).size() > 0) {
						if (results.get(resultSetIdx).get(currIdx) != null) {
							return results.get(resultSetIdx).get(currIdx).size();
						} else {
							return 0;
						}
					} else {
						return 0;
					}
				}
				else return 0;
			}

			@Override
			public int getColumnCount() {
				return 11;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				Antibody ab = results.get(resultSetIdx).get(currIdx).get(rowIndex);
				if (ab == null) {
					return null;
				}
				if (columnIndex == -1) {
					return Boolean.valueOf(ab.isRequired()).toString();
				} else if (columnIndex == 0) {
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
				} else if (columnIndex == 9) {
					return ab.getBrightness();
				} else if (columnIndex == 10) {
					return ab.getDensity();
				} else {
					return null;
				}
			}

		};
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(17, 32, 1053, 350);
		contentPanel.add(scrollPane);
		
		resultsTable = new JTable(antibodiesTableModel);
		resultsTable.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(resultsTable);
		resultsTable.setFillsViewportHeight(true);
		
		lblNewLabel = new JLabel("Required");
		lblNewLabel.setOpaque(true);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBackground(Color.BLACK);
		lblNewLabel.setBounds(17, 389, 61, 16);
		contentPanel.add(lblNewLabel);
		resultsTable.setDefaultRenderer(Object.class,  new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row, int col) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
				Boolean isRequired = Boolean.valueOf((String)table.getModel().getValueAt(row, -1));
				if (isRequired) {
					setBackground(Color.BLACK);
		            setForeground(Color.WHITE);
				} else {
					setBackground(table.getBackground());
		            setForeground(table.getForeground());
				}
				return this;
			}
		});
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(10, 435, 1066, 38);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			
			prevSetButton = new JButton("<<");
			buttonPane.add(prevSetButton);
			
			prevButton = new JButton("<");
			buttonPane.add(prevButton);
			
			nextButton = new JButton(">");
			buttonPane.add(nextButton);
			
			nextSetButton = new JButton(">>");
			buttonPane.add(nextSetButton);
			
			exportButton = new JButton("Export");
			buttonPane.add(exportButton);
			{
				closeButton = new JButton("Close");
				buttonPane.add(closeButton);
				closeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						PanelResultsDialog.this.setVisible(false);
					}
				});
				closeButton.setActionCommand("Cancel");
			}
			exportButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final JFileChooser fc = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter(
							"Excel", "xls");
					fc.setFileFilter(filter);
					int returnVal = fc.showSaveDialog(PanelResultsDialog.this);
					File f = fc.getSelectedFile();
					if (f != null)
					try {
						Antibodies.exportToExcel(f, results);
					} catch (WriteException | IOException e1) {
						// TODO Auto-generated catch block
						throw new RuntimeException();
					}
				}
			});
			nextSetButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					resultSetIdx++;
					currIdx = 0;
					antibodiesTableModel.fireTableDataChanged();
					updateNavButtonsStatuses();
				}
			});
			nextButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					currIdx++;
					antibodiesTableModel.fireTableDataChanged();
					updateNavButtonsStatuses();
				}
			});
			prevButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					currIdx--;
					antibodiesTableModel.fireTableDataChanged();
					updateNavButtonsStatuses();
				}
			});
			prevSetButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					resultSetIdx--;
					currIdx = 0;
					antibodiesTableModel.fireTableDataChanged();
					updateNavButtonsStatuses();
				}
			});
		}
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	public List<List<List<Antibody>>> getResults() {
		return results;
	}

	public void setResults(List<List<List<Antibody>>> results) {
		this.results = results;
	}
	public JTable getResultsTable() {
		return resultsTable;
	}
	public JButton getPrevButton() {
		return prevButton;
	}
	public JButton getNextButton() {
		return nextButton;
	}
	public JButton getExportButton() {
		return exportButton;
	}
	public JButton getCloseButton() {
		return closeButton;
	}
	public JButton getPrevSetButton() {
		return prevSetButton;
	}
	public JButton getNextSetButton() {
		return nextSetButton;
	}
	public void updateNavButtonsStatuses() {
		if (results == null || results.size() == 0) {
			prevButton.setEnabled(false);
			nextButton.setEnabled(false);
			prevSetButton.setEnabled(false);
			nextSetButton.setEnabled(false);
			return;
		} 
		
		if (currIdx == 0) {
			prevButton.setEnabled(false);
		} else {
			prevButton.setEnabled(true);
		}
		
		if (currIdx == results.get(resultSetIdx).size() - 1) {
			nextButton.setEnabled(false);
		} else {
			nextButton.setEnabled(true);
		}
		if (resultSetIdx == 0) {
			prevSetButton.setEnabled(false);
		} else {
			prevSetButton.setEnabled(true);
		}
		if (resultSetIdx == results.size() - 1) {
			nextSetButton.setEnabled(false);
		} else {
			nextSetButton.setEnabled(true);
		}
	}	
}
