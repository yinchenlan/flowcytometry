package com.lansoft.flowcytometry.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.xml.bind.JAXBException;

import com.google.common.collect.Sets;
import com.googlecode.concurrenttrees.common.Iterables;
import com.googlecode.concurrenttrees.radix.ConcurrentRadixTree;
import com.googlecode.concurrenttrees.radix.RadixTree;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.suffix.SuffixTree;
import com.lansoft.flowcytometry.model.Antibodies;
import com.lansoft.flowcytometry.model.Antibody;
import com.lansoft.flowcytometry.model.AntibodyDatabases;
import com.lansoft.flowcytometry.model.Configuration;
import com.lansoft.flowcytometry.model.Configurations;
import com.lansoft.flowcytometry.model.Conjugate;
import com.lansoft.flowcytometry.model.Conjugates;
import com.lansoft.flowcytometry.model.DetectorConjugates;
import com.lansoft.flowcytometry.model.FlowCytometer;
import com.lansoft.flowcytometry.model.FlowCytometers;
import com.lansoft.flowcytometry.model.TargetDefinition;
import com.lansoft.flowcytometry.model.TargetGroup;
import com.lansoft.flowcytometry.model.TargetsConfiguration;
import com.lansoft.flowcytometry.model.TargetsConfigurations;
import com.lansoft.flowcytometry.service.FlowCytometrySolver;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dialog.ModalityType;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.JProgressBar;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSeparator;
import javax.swing.JSlider;

import java.awt.Color;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class ChooseTargetsDialog extends JDialog implements PropertyChangeListener {

	private static final String TARGET_MOLECULE = "TARGET_MOLECULE";
	private static final String TARGET_CLONE = "TARGET_CLONE";
	private final JPanel contentPanel = new JPanel();
	private JTable targetDetailsList;
	AbstractTableModel targetDetailsListModel;
	private JComboBox conjugateSelectionDropDown;
	private JComboBox targetMoleculeDropDown;
	private DefaultComboBoxModel<String> conjugateSelectionDropDownModel;
	private DefaultComboBoxModel<String> targetMoleculeDropDownModel;
	private JRadioButton targetMoleculeRadioButton;
	private JRadioButton targetCloneIdRadioButton;
	private JButton addTargetGroupButton;
	private JButton deleteTargetGroupButton;
	private JList targetGroupList;
	private DefaultListModel<String> targetGroupModel;
	private JButton backButton;
	private JButton calculatePanelButton;
	private List<String> selectedDatabases;
	private String selectedFlowCytometer;
	private TargetsConfiguration targetGroups;
	ButtonGroup group;
	private JButton cancelButton;
	private JProgressBar calculationProgessBar;
	private CalculateTask calculateTask;
	private JLabel percentLabel;
	private JButton resultsButton;
	private JCheckBox isRequiredCheckBox;
	private JTextField coolingRateField;
	private JTextField targetMoleculeFinder;
	private JButton moveDownButton;
	private PanelResultsDialog panelResultsDialog;
	private List<List<List<Antibody>>> results;
	private boolean calculationFailed;
	private JSpinner densitySpinner;
	RadixTree<Integer> tree;
	private JSlider initTempSlider;
	private TargetGroup newTargetGroup;
	JLabel lblProgress;
	private boolean isCustomConfiguration;
	private DefaultListModel<String> targetDefListModel;
	private JList targetsConfigurationsList;
	private TargetsConfigurations targetsConfigurations;
	private int targetsConfigurationIndex;
	
	class ResultSet implements Comparable<ResultSet> {
		List<List<Antibody>> results;
		long score;
		String[] resultString;
		int size;
		double[] brightnesses;
		double[] densities;
		
		public int hashCode() {
			return results.hashCode();
		}
		
		public boolean equals(Object other) {
			if (!(other instanceof ResultSet)) return false;
			ResultSet oRs = (ResultSet) other;
			return oRs.results.hashCode() == results.hashCode();
		}

		@Override
		public int compareTo(ResultSet o) {
			return Long.compare(this.score, o.score);
		}
	}
	
	class CalculateTask extends SwingWorker<Void, Void> {
		
		Comparator<ResultSet> comp = new Comparator<ResultSet>() {

			@Override
			public int compare(ResultSet o1, ResultSet o2) {
				return Long.compare(o1.score, o2.score);
			}
		};
        /*
         * Calculate task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
			TargetGroup rtg = getRequiredTargetGroup();
			List<TargetGroup> tgs = getTargetGroups();
			List<ResultSet> temp = new ArrayList<ResultSet>();			
			results.clear();
			String[] bestRs = null;
			long bestScore = Long.MAX_VALUE;
			int bestSize = Integer.MAX_VALUE;
			ChooseTargetsDialog.this.lblProgress.setText("0/12");
			for (int idx = 0; idx < 12; idx++) {
				boolean isPenalizePanelCount = idx > 6;
				setProgress(0);
				if (tgs.size() == 0) {
					ResultSet rs = solve(
							rtg, Collections.EMPTY_LIST, bestRs, bestSize, isPenalizePanelCount);
					if (rs.score < bestScore) {
						bestSize = rs.size;	
						bestScore = rs.score;
						bestRs = rs.resultString;
					}
					temp.add(rs);
				} else {
					ResultSet rs = solve(rtg, tgs, bestRs, bestSize, isPenalizePanelCount);
					if (rs.score < bestScore) {
						bestSize = rs.size;	
						bestScore = rs.score;
						bestRs = rs.resultString;
					}
					temp.add(rs);
				}
				ChooseTargetsDialog.this.lblProgress.setText("" + (idx + 1) + "/12");
			}
			ChooseTargetsDialog.this.lblProgress.setText("Progress");
			// Remove if it already exists as a solution
			ResultSet lrs = temp.get(temp.size() - 1);
			boolean isSame = true;
			for (int idx = 0; idx < temp.size(); idx++) {
				ResultSet rs = temp.get(idx);
				for (int i = 0; i < rs.resultString.length; i++) {
					if (rs.resultString[i] == null && lrs.resultString[i] == null ) {
						continue;
					} else
					if ((rs.resultString[i] == null && lrs.resultString[i] != null) || 
							(rs.resultString[i] != null && lrs.resultString[i] == null) ||
							!rs.resultString[i].equals(lrs.resultString[i])) {
						isSame = false;
						break;
					}
				}
				if (!isSame) {
					break;
				}
			}
			if (isSame) {
				temp.remove(temp.size() - 1);
			}
			Collections.sort(temp, comp);
			for (ResultSet rp : temp) {
				System.out.println(rp.score);
				results.add(rp.results);
			}
			return null;
        }
        
        private Double getCoolingRate() {
        	return new Double(coolingRateField.getText());
        }
        
        private Double getInitTemp() {
        	return new Double(initTempSlider.getValue());
        	//return new Double(temperatureField.getText());
        }
        
        public ResultSet solve(
        		TargetGroup requiredTargetGroup, 
        		List<TargetGroup> targetGroups, 
        		String[] resultString, 
        		int size,
        		boolean isPenalizePanelCount) {
			ResultSet results = new ResultSet();
			FlowCytometrySolver fcs = null;
			try {
	        	fcs = new FlowCytometrySolver(
	        			requiredTargetGroup, 
	        			targetGroups, 
	        			getSelectedDatabases(), 
	        			getConfiguration(), 
	        			getInitTemp(), 
	        			getCoolingRate(), 
	        			5, 
	        			isPenalizePanelCount);
				fcs.setTemperatureChangeListener(new TemperatureChangeListener() {					
					@Override
					public void onTemperatureChange(double temp) {
						CalculateTask.this.setProgress((int)(100 - temp * 100));
					}
				});
				calculationProgessBar.setValue(0);
				percentLabel.setText("0%");
				if (resultString == null) 
					results.results = fcs.solve();
					else 
					results.results = fcs.solve(resultString, size);
				results.score = fcs.getScore();
				results.resultString = fcs.getResult();
				results.size = fcs.getResultSize();
			} catch (CalculationException e) {
				JOptionPane.showMessageDialog(ChooseTargetsDialog.this,
						e.getMessage(),
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
				calculationFailed = true;
				calculatePanelButton.setEnabled(true);
				return results;
			}
			calculationFailed = false;
			System.out.println(results);
			fcs.prettyPrint();
			return results;
        }
        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
        	if (!calculationFailed) {
	            Toolkit.getDefaultToolkit().beep();
	            calculatePanelButton.setEnabled(true);
	            calculationProgessBar.setValue(0);
	            percentLabel.setText("0%");
	            resultsButton.setEnabled(true);
	            panelResultsDialog.setResults(results);
	            panelResultsDialog.setVisible(true);
            }
        }
	}

	/**
	 * Create the dialog.
	 */
	@SuppressWarnings("unchecked")
	public ChooseTargetsDialog() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ChooseTargetsDialog.class.getResource("/fcIcon.gif")));
		getContentPane().setBackground(Color.WHITE);		
		results = new ArrayList<List<List<Antibody>>>();
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setTitle("Target Selection");
		setBounds(100, 100, 638, 742);
		try {
			loadTargetsConfigurations();
		} catch (FileNotFoundException e) {
			//create a new object
			targetsConfigurations = new TargetsConfigurations();
		} catch (Exception e) {
			//FIXME: handle error properly
			e.printStackTrace();
		}
		List<TargetsConfiguration> tcs = targetsConfigurations.getTargetsConfigurations();
		/*if (tcs.size() == 0) {
			targetGroups = new TargetsConfiguration();
			targetGroups.setName("new");
			targetsConfigurations.getTargetsConfigurations().add(targetGroups);
		} else {
			targetGroups = tcs.get(0);
		}*/
		
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 630, 681);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		{
			targetMoleculeDropDownModel = new DefaultComboBoxModel<String>();
			targetMoleculeDropDown = new JComboBox(targetMoleculeDropDownModel);
			targetMoleculeDropDown.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String targetId = getSelectedTargetId();
					Set<String> conjugates;
					try {
						conjugates = getConjugates(targetId, isTargetMolecule());
					} catch (FileNotFoundException | InstantiationException
							| IllegalAccessException | JAXBException e1) {
						throw new RuntimeException(e1);
					}
					List<String> sorted = new ArrayList<String>(conjugates);
					Collections.sort(sorted);
					conjugateSelectionDropDownModel.removeAllElements();
					conjugateSelectionDropDownModel.addElement(Conjugate.CONJUGATE_AUTO_SELECT);
					for (String conj : sorted) {
						conjugateSelectionDropDownModel.addElement(conj);
					}
				}
			});
			targetMoleculeDropDown.setBounds(158, 315, 115, 26);
			contentPanel.add(targetMoleculeDropDown);
		}
		{
			conjugateSelectionDropDownModel = new DefaultComboBoxModel<String>();
			conjugateSelectionDropDownModel.addListDataListener(new ListDataListener() {
				
				@Override
				public void intervalRemoved(ListDataEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void intervalAdded(ListDataEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void contentsChanged(ListDataEvent e) {
					if (targetMoleculeDropDownModel.getSize() == 0)
						moveDownButton.setEnabled(false);
					else moveDownButton.setEnabled(true);
				}
			})
			;
			conjugateSelectionDropDown = new JComboBox(conjugateSelectionDropDownModel);
			conjugateSelectionDropDown.setBounds(285, 315, 130, 26);
			contentPanel.add(conjugateSelectionDropDown);
		}
		
		JLabel lblGroup = new JLabel("Target Groups");
		lblGroup.setBounds(340, 21, 89, 16);
		contentPanel.add(lblGroup);
		
		addTargetGroupButton = new JButton("Add");
		addTargetGroupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isRequiredGroup = false;
				String name = (String)JOptionPane.showInputDialog(
	                    ChooseTargetsDialog.this,
	                    "Please enter target group name",
	                    "Target Group Name",
	                    JOptionPane.PLAIN_MESSAGE,
	                    null,
	                    null,
	                    null);
				if (!requiredGroupExists()) {
					int n = JOptionPane.showConfirmDialog(
							ChooseTargetsDialog.this,
						    "Is this group required?",
						    "Required Group",
						    JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.YES_OPTION) {
						isRequiredGroup = true;
					}
				}
				
				if (name != null && name.trim().length() > 0) {
					TargetGroup tg = new TargetGroup();
					tg.setRequired(isRequiredGroup);
					tg.setName(name);
					targetGroups.getGroups().add(tg);
					targetGroupModel.addElement(name);
					targetGroupList.setSelectedValue(name, false);
				}
				calculatePanelButton.setEnabled(true);
			}
		});
		addTargetGroupButton.setBounds(407, 174, 90, 28);
		contentPanel.add(addTargetGroupButton);
		
		deleteTargetGroupButton = new JButton("Delete");
		deleteTargetGroupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selected = targetGroupList.getSelectedIndices();
				Set<TargetGroup> tgs = new HashSet<TargetGroup>();
				for (int idx = 0; idx < selected.length; idx++) {
					tgs.add(targetGroups.getGroups().get(selected[idx]));
					targetGroupModel.remove(selected[idx]);
				}
				targetGroups.getGroups().removeAll(tgs);
			}
		});
		deleteTargetGroupButton.setBounds(528, 174, 90, 28);
		contentPanel.add(deleteTargetGroupButton);
		targetDetailsListModel = new AbstractTableModel() {

			@Override
			public String getColumnName(int column) {
				if (column == 0)
					return "Target/Clone";
				else if (column == 1)
					return "Target Name";
				else if (column == 2)
					return "Conjugate Name";
				else if (column == 3)
					return "Density";
				else return null;
			}

			@Override
			public int getRowCount() {
				List<TargetDefinition> definitions = (targetGroupList.getSelectedValue() != null) ? getTargetDefinitions() : getNewTargetDefinitions();
				if (definitions != null) {
					return definitions.size();
				} else {
					return 0;
				}
			}

			@Override
			public int getColumnCount() {
				return 4;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				List<TargetDefinition> definitions = (targetGroupList.getSelectedValue() != null) ? getTargetDefinitions() : getNewTargetDefinitions();
				TargetDefinition d = definitions.get(rowIndex);
				if (columnIndex == 0) {
					if(d.isTargetMolecule()) return "Target";
						else return "Clone";
				} else if (columnIndex == 1){
					return d.getTargetId();
				} else if (columnIndex == 2) {
					return d.getConjugateId();
				} else if (columnIndex == 3) {
					return new Float(d.getDensity());
				} else {
					return null;
				}					
			}
		};
		{		
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(340, 49, 278, 113);
			contentPanel.add(scrollPane);
			targetGroupModel = new DefaultListModel<String>();
			targetGroupList = new JList(targetGroupModel);
			scrollPane.setViewportView(targetGroupList);
			targetGroupList.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					int targetGroupIdx = targetGroupList.getSelectedIndex();
					if (targetGroupIdx > -1) {
						TargetGroup tg = targetGroups.getGroups().get(targetGroupIdx);
						if (tg.isRequired()) {
							isRequiredCheckBox.setSelected(true);
						} else {
							isRequiredCheckBox.setSelected(false);
						}
					}
					targetDetailsListModel.fireTableDataChanged();
				}
			});
		}
		loadTargetGroups();
		JLabel lblNewLabel = new JLabel("Target Molecule");
		lblNewLabel.setBounds(158, 293, 190, 16);
		contentPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Conjugate Selection");
		lblNewLabel_1.setBounds(285, 293, 130, 16);
		contentPanel.add(lblNewLabel_1);
		
		JLabel lblDensity = new JLabel("Density");
		lblDensity.setBounds(429, 293, 55, 16);
		contentPanel.add(lblDensity);
		
		targetMoleculeRadioButton = new JRadioButton("Specificity");
		targetMoleculeRadioButton.setActionCommand(TARGET_MOLECULE);
		targetMoleculeRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initializePrefixTree();
				populateDropDowns();
			}
		});
		targetMoleculeRadioButton.setBounds(94, 259, 89, 18);
		contentPanel.add(targetMoleculeRadioButton);
		
		targetCloneIdRadioButton = new JRadioButton("Clone ID");
		targetCloneIdRadioButton.setActionCommand(TARGET_CLONE);
		targetCloneIdRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initializePrefixTree();
				populateDropDowns();
			}
		});
		targetCloneIdRadioButton.setBounds(195, 259, 115, 18);
		contentPanel.add(targetCloneIdRadioButton);
		
		group = new ButtonGroup();
		group.add(targetMoleculeRadioButton);
		group.add(targetCloneIdRadioButton);
		group.setSelected(targetMoleculeRadioButton.getModel(), true);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(17, 398, 606, 278);
		contentPanel.add(scrollPane);
		targetDetailsList = new JTable(targetDetailsListModel);
		scrollPane.setColumnHeaderView(targetDetailsList);
		targetDetailsList.setFillsViewportHeight(true);		
		
		
		JLabel lblTargets = new JLabel("Targets");
		lblTargets.setBounds(17, 370, 55, 16);
		contentPanel.add(lblTargets);
		
		moveDownButton = new JButton("+");
		moveDownButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addTargetDefinitions();
			}

		});
		moveDownButton.setBounds(360, 364, 55, 28);
		contentPanel.add(moveDownButton);
		
		JButton removeButton = new JButton("-");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = targetDetailsList.getSelectedRows();
				if (selectedRows == null || selectedRows.length == 0) return;
				List<TargetDefinition> toBeRemoved = new ArrayList<TargetDefinition>();
				List<TargetDefinition> tgs = (targetGroupList.getSelectedValue() != null) ? getTargetDefinitions() : getNewTargetDefinitions();
				for (int idx : selectedRows) {
					toBeRemoved.add(tgs.get(idx));
				}
				tgs.removeAll(toBeRemoved);
				targetDetailsListModel.fireTableDataChanged();
				populateDropDowns();
			}
		});
		removeButton.setBounds(429, 364, 55, 28);
		contentPanel.add(removeButton);
		
		isRequiredCheckBox = new JCheckBox("Required Group");
		isRequiredCheckBox.setBounds(483, 20, 135, 18);
		contentPanel.add(isRequiredCheckBox);
		
		
		JLabel lblTemperature = new JLabel("Calculation Strength");
		lblTemperature.setBounds(360, 219, 129, 16);
		contentPanel.add(lblTemperature);
		lblTemperature.setVisible(false);
		
		coolingRateField = new JTextField();
		coolingRateField.setText("0.00003");
		coolingRateField.setBounds(497, 253, 122, 28);
		contentPanel.add(coolingRateField);
		coolingRateField.setColumns(10);
		coolingRateField.setVisible(true);
		
		JLabel lblCoolingRate = new JLabel("Cooling Rate");
		lblCoolingRate.setBounds(395, 259, 80, 16);
		contentPanel.add(lblCoolingRate);
		lblCoolingRate.setVisible(true);
		
		targetMoleculeFinder = new JTextField();
		targetMoleculeFinder.addKeyListener(new KeyAdapter()  {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			@Override
			public void keyReleased(KeyEvent e) {
				populateDropDowns();
				//applyFilter();
			}
		});
		targetMoleculeFinder.setBounds(17, 313, 129, 28);
		contentPanel.add(targetMoleculeFinder);
		targetMoleculeFinder.setColumns(10);
		
		densitySpinner = new JSpinner();
		densitySpinner.setModel(new SpinnerNumberModel(new Float(5), new Float(1), new Float(10), new Float(1)));
		densitySpinner.setBounds(425, 315, 89, 26);
		contentPanel.add(densitySpinner);
		
		JLabel lblQuickSearch = new JLabel("Quick Search");
		lblQuickSearch.setBounds(17, 293, 89, 18);
		contentPanel.add(lblQuickSearch);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(17, 353, 606, 5);
		contentPanel.add(separator);
		
		JLabel lblTargetType = new JLabel("Target Type:");
		lblTargetType.setBounds(16, 259, 80, 18);
		contentPanel.add(lblTargetType);
		
		initTempSlider = new JSlider();
		initTempSlider.setSnapToTicks(true);
		initTempSlider.setPaintLabels(true);
		initTempSlider.setPaintTicks(true);
		initTempSlider.setValue(500000);
		initTempSlider.setMinorTickSpacing(10000);
		initTempSlider.setMaximum(100000);
		initTempSlider.setMinimum(10000);
		initTempSlider.setBounds(501, 217, 122, 24);
		contentPanel.add(initTempSlider);
		initTempSlider.setVisible(false);
		
		JButton btnAddTarget = new JButton("Add Target(s)");
		btnAddTarget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addTargetGroup();
			}
		});
		btnAddTarget.setBounds(496, 363, 123, 30);
		contentPanel.add(btnAddTarget);
		
		JButton btnNewButton = new JButton("Quick Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addTargetDefinitions();
				addTargetGroup();
			}
		});
		btnNewButton.setBounds(526, 313, 93, 30);
		contentPanel.add(btnNewButton);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(17, 49, 280, 113);
		contentPanel.add(scrollPane_1);
		
		targetDefListModel = new DefaultListModel<String>();
		
		targetsConfigurationsList = new JList(targetDefListModel);
		targetsConfigurationsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int idx = targetsConfigurationsList.getSelectedIndex();
				if (idx > -1) {
					targetGroups = targetsConfigurations.getTargetsConfigurations().get(idx);
					focusTargetsConfiguration(targetGroups);
				}
			}
		});
		scrollPane_1.setViewportView(targetsConfigurationsList);
		
		JLabel lblTargetDefinitions = new JLabel("Targets Configurations");
		lblTargetDefinitions.setBounds(17, 21, 166, 16);
		contentPanel.add(lblTargetDefinitions);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int idx = targetsConfigurationsList.getSelectedIndex();
				targetsConfigurations.getTargetsConfigurations().remove(idx);
				targetDefListModel.remove(idx);
				clearTargetsConfiguration();
			}
		});
		btnDelete.setBounds(125, 175, 70, 26);
		contentPanel.add(btnDelete);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNewTargetsConfiguration();
			}
		});
		
		btnAdd.setBounds(27, 174, 61, 26);
		contentPanel.add(btnAdd);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(227, 175, 70, 26);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					saveTargetsConfigurations();
				} catch (Exception ex) {
					//FIXME: proper error handling
					ex.printStackTrace();
				}
			}
		});
		contentPanel.add(btnSave);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 678, 630, 38);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			
			lblProgress = new JLabel("Progress");
			buttonPane.add(lblProgress);
			
			calculationProgessBar = new JProgressBar();
			buttonPane.add(calculationProgessBar);
			
			percentLabel = new JLabel("0%");
			buttonPane.add(percentLabel);
			
			resultsButton = new JButton("Results");
			buttonPane.add(resultsButton);
			resultsButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					panelResultsDialog.setVisible(true);
				}
			});
			resultsButton.setEnabled(false);
			{
				backButton = new JButton("Back");
				buttonPane.add(backButton);
				backButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ChooseTargetsDialog.this.setVisible(false);
					}
				});
				backButton.setActionCommand("OK");
				getRootPane().setDefaultButton(backButton);
			}
			{
				calculatePanelButton = new JButton("Calc");
				buttonPane.add(calculatePanelButton);
				calculatePanelButton.setEnabled(false);
				calculatePanelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						calculatePanelButton.setEnabled(false);
						calculateTask = new CalculateTask();
						calculateTask.addPropertyChangeListener(ChooseTargetsDialog.this);
						calculateTask.execute();
					}
				});
				calculatePanelButton.setActionCommand("Cancel");
			}
			
			cancelButton = new JButton("Close");
			buttonPane.add(cancelButton);
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ChooseTargetsDialog.this.setVisible(false);
				}
			});
		}
		panelResultsDialog = new PanelResultsDialog();
		initializeTargetsConfigurations();
		focusTargetsConfiguration(targetGroups);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}
	
	private void initializeTargetsConfigurations() {
		for (TargetsConfiguration tc : targetsConfigurations.getTargetsConfigurations()) {
			targetDefListModel.addElement(tc.getName());
		}
	}

	private void focusTargetsConfiguration(TargetsConfiguration targetGroups) {
		targetGroupModel.clear();
		if (targetGroups != null) {
			for (TargetGroup group : targetGroups.getGroups()) {
				targetGroupModel.addElement(group.getName());
			}
			targetDetailsListModel.fireTableDataChanged();
			targetMoleculeFinder.setText("");
			targetMoleculeFinder.requestFocus();
			calculatePanelButton.setEnabled(true);
		}
	}

	private void clearTargetsConfiguration() {
		targetGroupModel.clear();
		targetDetailsListModel.fireTableDataChanged();
		targetMoleculeFinder.setText("");
		targetMoleculeFinder.requestFocus();
		calculatePanelButton.setEnabled(true);
	}
	private void loadTargetsConfigurations() 
			throws FileNotFoundException, InstantiationException, IllegalAccessException, JAXBException  {
		targetsConfigurations = TargetsConfigurations.loadTargetsConfigurations();
	}

	protected String getGroupName() {
		List<TargetDefinition> tds = (newTargetGroup == null) ? null : (newTargetGroup.getDefinitions() == null) ? null : newTargetGroup.getDefinitions();
		StringBuffer buf = new StringBuffer();
		boolean first = true;
		if (tds != null) {
			for (TargetDefinition td : tds) {
				if (!first) {
					buf.append("-");
				} else {
					first = false;
				}
				buf.append(td.getTargetId());
			}
		}
		return buf.toString();
	}

	public void initializePrefixTree() {
		tree = new ConcurrentRadixTree<Integer>(new DefaultCharArrayNodeFactory());
		int idx = 1;
		for (String target : getTargetIds()) {
			 tree.put(target, idx++);
		}
	}
	
	protected boolean requiredGroupExists() {
		for (TargetGroup tg : targetGroups.getGroups()) {
			if (tg.isRequired()) {
				return true;
			}
		}
		return false;
	}
	
	private List<TargetDefinition> getNewTargetDefinitions() {
		if (newTargetGroup == null) {
			newTargetGroup = new TargetGroup();
		}
		List<TargetDefinition> tds = newTargetGroup.getDefinitions();
		if (tds == null) {
			tds = new ArrayList<TargetDefinition>();
			newTargetGroup.setDefinitions(tds);
		}
		return tds;
	}
	
	protected List<TargetDefinition> getTargetDefinitions() {
		TargetGroup tg = getTargetGroup();
		if (tg != null)
			return tg.getDefinitions();
		else return null;
	}
	
	protected List<TargetDefinition> getAllTargetDefinitions() {
		List<TargetDefinition> retVal = new ArrayList<TargetDefinition>();
		if (targetGroups != null) {
			for (TargetGroup tg : targetGroups.getGroups()) {
				retVal.addAll(tg.getDefinitions());
			}
			if (newTargetGroup != null && newTargetGroup.getDefinitions() != null) {
				retVal.addAll(newTargetGroup.getDefinitions());
			}
		}
		return retVal;
	}
	
	protected TargetGroup getTargetGroup() {
		String targetGroupName = (String) targetGroupList.getSelectedValue();
		for (TargetGroup tg : targetGroups.getGroups()) {
			if (tg.getName().equals(targetGroupName)) {
				return tg;
			}
		}
		return null;
	}

	private void loadTargetGroups() {
	}

	public JTable getTargetDetailsList() {
		return targetDetailsList;
	}
	public JComboBox getConjugateSelectionDropDown() {
		return conjugateSelectionDropDown;
	}
	public JComboBox getTargetMoleculeDropDown() {
		return targetMoleculeDropDown;
	}
	public JRadioButton getTargetMoleculeRadioButton() {
		return targetMoleculeRadioButton;
	}
	public JRadioButton getTargetCloneIdRadioButton() {
		return targetCloneIdRadioButton;
	}
	public JButton getAddTargetGroupButton() {
		return addTargetGroupButton;
	}
	public JButton getDeleteTargetGroupButton() {
		return deleteTargetGroupButton;
	}
	public JList getTargetGroupList() {
		return targetGroupList;
	}
	public JButton getOkButton() {
		return backButton;
	}

	public List<Antibodies> getSelectedDatabases() {
		List<Antibodies> abdb = new ArrayList<Antibodies>();
		List<Antibodies> absList = null;
		try {
			absList = AntibodyDatabases.getAntibodyDatabases().getAntibodies();
		} catch (FileNotFoundException | JAXBException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		for (String dbName : selectedDatabases) {
			for (Antibodies abs : absList) {
				if (abs.getName().equals(dbName)) {
					abdb.add(abs);
				}
			}
		}
		return abdb;
	}

	public void setSelectedDatabases(List<String> selectedDatabases) {
		this.selectedDatabases = selectedDatabases;
	}

	public String getSelectedConfigurationName() {
		return selectedFlowCytometer;
	}

	public void setSelectedFlowCytometer(String selectedFlowCytometer) {
		this.selectedFlowCytometer = selectedFlowCytometer;
	}
	
	public boolean isTargetMolecule() {
		return (group.getSelection() != null) ? group.getSelection().getActionCommand().equals(TARGET_MOLECULE) : true;
	}
	
	public List<Antibodies> getAntibodies() {
		List<Antibodies> retVal = new ArrayList<Antibodies>();
		Set<String> dbNames = new HashSet<String>();
		dbNames.addAll(selectedDatabases);
		try {
			AntibodyDatabases adb = AntibodyDatabases.getAntibodyDatabases();
			for (Antibodies ab : adb.getAntibodies()) {
				if (dbNames.contains(ab.getName())) {
					retVal.add(ab);
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
		return retVal;
	}
	
	public List<Antibody> getAntibodyList() {
		List<Antibody> retVal = new ArrayList<Antibody>();
		for (Antibodies abs : getAntibodies()) {
			for (Antibody ab : abs.getAntibodiesList()) {
				retVal.add(ab);
			}
		}
		return retVal;
	}
	
	public Set<String> getTargetIds() {
		Set<String> retVal = new HashSet<String>();
		boolean itm = isTargetMolecule();
		for (Antibody antibody : getAntibodyList()) {
			if (itm) {
				String name = antibody.getTargetName();
				if (name.trim().length() == 0) continue;
				retVal.add(antibody.getTargetName());
			} else {
				String name = antibody.getClone();
				if (name.trim().length() == 0) continue;
				retVal.add(antibody.getClone());
			}
		}
		Set<String> usedTargets = new HashSet<String>();
		Set<String> usedConjugates = new HashSet<String>();
		for (TargetDefinition tg : getAllTargetDefinitions()) {
			usedTargets.add(tg.getTargetId());
			usedConjugates.add(tg.getConjugateId());
		}
		for (TargetDefinition tg : getAllTargetDefinitions()) {
			if (usedConjugates.contains(tg.getConjugateId())) {
				usedTargets.add(tg.getTargetId());
			}
		}
		retVal.removeAll(usedTargets);
		return retVal;
	}
	
	public Configuration getConfiguration() {
		Configuration conf = null;
		try {
			if (isCustomConfiguration()) {
				Configurations confs = Configurations.loadConfigurations();
				for (Configuration c : confs.getConfigurationsList()) {
					if (getSelectedConfigurationName().equals(c.getName())) {
						conf = c;
					}
				}
			} else {
				List<TargetDefinition> defs = getAllTargetDefinitions();
				FlowCytometer fc = getSelectedFlowCytometer();	
				conf = fc.getConfiguration(defs);
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
		return conf;
	}
	
	public boolean isCustomConfiguration() {
		return isCustomConfiguration;
	}
	
	public void setIsCustomConfiguration(boolean val) {
		isCustomConfiguration = val;
	}

	private FlowCytometer getSelectedFlowCytometer() 
			throws FileNotFoundException, InstantiationException, IllegalAccessException, JAXBException {
		for (FlowCytometer fc : FlowCytometers.loadFlowCytometers().getFlowCytometers()) {
			if (fc.getName().equals(getSelectedConfigurationName())) {
				return fc;
			}
		}
		return null;
	}

	public Set<String> getConjugates(String targetId, boolean isTargetModule) 
			throws FileNotFoundException, InstantiationException, IllegalAccessException, JAXBException {
		Set<String> detectorConjugates = new HashSet<String>();
		for (Conjugate conj : Conjugates.loadConjugates().getConjugatesList()) {
			detectorConjugates.add(conj.getName());
		}
		Set<String> antibodiesConjugates = new HashSet<String>();
		for (Antibody antibody : getAntibodyList()) {
			if (isTargetModule) {
				if (antibody.getTargetName().equals(targetId)) {
					antibodiesConjugates.add(antibody.getConjugateName());
				}
			} else {
				if (antibody.getClone().equals(targetId)) {
					antibodiesConjugates.add(antibody.getConjugateName());
				}
			}
		}
		Set<String> usedConjugates = new HashSet<String>();
		for (TargetDefinition tg : getAllTargetDefinitions()) {
			usedConjugates.add(tg.getConjugateId());
		}
		Set<String> result = new HashSet<String>(); 
		Sets.intersection(antibodiesConjugates, detectorConjugates).copyInto(result);
		result.removeAll(usedConjugates);
		return result;
	}
	
	public String getSelectedTargetId() {
		return (String) targetMoleculeDropDownModel.getSelectedItem();
	}
	
	public TargetDefinition createTargetDefinition() {
		TargetDefinition td = new TargetDefinition();
		td.setTargetMolecule(isTargetMolecule());
		td.setTargetId(getTargetId());
		td.setConjugateId(getConjugateId());
		td.setDensity(getDensity());
		return td;
	}

	private float getDensity() {
		return (Float) densitySpinner.getValue();
	}

	private String getTargetId() {
		return (String)targetMoleculeDropDown.getSelectedItem();
	}

	private String getConjugateId() {
		return (String)conjugateSelectionDropDown.getSelectedItem();
	}
	
	private Set<String> getAllTargetDefinitionIds() {
		List<TargetDefinition> all = getAllTargetDefinitions();
		Set<String> retVal = new HashSet<String>();
		for (TargetDefinition td : all) {
			retVal.add(td.getTargetId());
		}
		return retVal;
	}
	
	public void populateDropDowns() {
		String pattern = targetMoleculeFinder.getText().trim();
		targetMoleculeDropDownModel.removeAllElements();
		Set<CharSequence> vals = Iterables.toSet(tree.getKeysStartingWith(pattern));
		vals.removeAll(getAllTargetDefinitionIds());
		for (CharSequence tid : vals) {
			targetMoleculeDropDownModel.addElement((String) tid);
		}
	}
	
	protected TargetGroup getRequiredTargetGroup() {
		for (TargetGroup tg : targetGroups.getGroups()) {
			if (tg.isRequired()) return tg;
		}
		return new TargetGroup();
	}
	
	protected List<TargetGroup> getTargetGroups() {
		List<TargetGroup> retVal = new ArrayList<TargetGroup>();
		for (TargetGroup tg : targetGroups.getGroups()) {
			if (!tg.isRequired()) retVal.add(tg);
		}
		return retVal;
	}
	public JButton getCancelButton() {
		return cancelButton;
	}
	public JProgressBar getCalculationProgessBar() {
		return calculationProgessBar;
	}
	public JButton getCalculatePanelButton() {
		return calculatePanelButton;
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		 if ("progress" == evt.getPropertyName()) {
	            int progress = (Integer) evt.getNewValue();
	            calculationProgessBar.setValue(progress);
	            percentLabel.setText(progress + "%");
	     } 
	}
	public JLabel getPercentLabel() {
		return percentLabel;
	}
	public JButton getResultsButton() {
		return resultsButton;
	}
	public JCheckBox getIsRequiredCheckBox() {
		return isRequiredCheckBox;
	}
	public JTextField getCoolingRateField() {
		return coolingRateField;
	}
	public JTextField getTargetMoleculeFinder() {
		return targetMoleculeFinder;
	}
	public void applyFilter() {
		Set<String> removeElements = new HashSet<String>();
		String pattern = targetMoleculeFinder.getText().trim();
		for(int idx = 0; idx < targetMoleculeDropDownModel.getSize(); idx++) {
			String val = targetMoleculeDropDownModel.getElementAt(idx);
			if (!val.contains(pattern)) {
				removeElements.add(val);
			}
		}
		for (String val : removeElements) {
			targetMoleculeDropDownModel.removeElement(val);
		}
	}
	public JButton getMoveDownButton() {
		return moveDownButton;
	}
	public JSpinner getDensitySpinner() {
		return densitySpinner;
	}
	public JSlider getInitTempSlider() {
		return initTempSlider;
	}

	void addTargetDefinitions() {
		TargetDefinition td = createTargetDefinition();
		List<TargetDefinition> tds = (targetGroupList.getSelectedValue() != null) ? getTargetDefinitions() : getNewTargetDefinitions();
		tds.add(td);
		targetDetailsListModel.fireTableDataChanged();	
		populateDropDowns();
		targetMoleculeFinder.setText("");
		targetMoleculeFinder.requestFocus();
	}

	void addTargetGroup() {
		boolean isRequiredGroup = false;
		String name = getGroupName();
		if (!requiredGroupExists()) {
			int n = JOptionPane.showConfirmDialog(
					ChooseTargetsDialog.this,
				    "Is this group required?",
				    "Required Group",
				    JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				isRequiredGroup = true;
			}
		}				
		if (name != null && name.trim().length() > 0) {
			newTargetGroup.setRequired(isRequiredGroup);
			if (isRequiredGroup) {
				name = "REQ:" + name;
			}
			newTargetGroup.setName(name);
			targetGroups.getGroups().add(newTargetGroup);
			targetGroupModel.addElement(name);
			newTargetGroup = null;
			targetDetailsListModel.fireTableDataChanged();
		}
		targetMoleculeFinder.setText("");
		targetMoleculeFinder.requestFocus();
		calculatePanelButton.setEnabled(true);
	}
	
	void addNewTargetsConfiguration() {
		String name = (String)JOptionPane.showInputDialog(
                ChooseTargetsDialog.this,
                "Please enter configuration name",
                "Configuration Name",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);
		targetGroups = new TargetsConfiguration();
		targetGroups.setName(name);
		targetsConfigurations.getTargetsConfigurations().add(targetGroups);
		targetDefListModel.addElement(name);
	}
	
	private void saveTargetsConfigurations() throws FileNotFoundException, JAXBException, IOException {
		TargetsConfigurations.saveTargetsConfigurations(targetsConfigurations);
	}
}
