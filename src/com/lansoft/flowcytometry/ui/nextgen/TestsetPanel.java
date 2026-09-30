package com.lansoft.flowcytometry.ui.nextgen;
/**
 * This class creates a Testset Panel.  The panel allows users
 * to create their testset, assigning densities to targets, preassigning
 * select targets to conjugated antibodies, and grouping their 
 * targets into subgroups.
 */

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.awt.event.ActionEvent;
import javax.swing.JTable;

public class TestsetPanel extends JPanel {
	private JFrame f;
	public JTable table;
	public JLabel lblTestsetName;
	public JTextField textField4,
						textField1;
	private ButtonGroup btnGroup1,
						btnGroup4;
		
	public JButton btnPrevious,
					btnNext,
					btnSave,
					btnHelp,
					btnEnter1,
					btnAdd2,
					btnAssign,
					btnDelete2,
					btnAdd,
					btnRemove,
					btnEnter4;
	
	public JRadioButton rdbtnNewTestset,
					rdbtnLoadTestset,
					rdbtnRequiredGroup,
					rdbtnNew,
					rdbtnDelete;
	private String [] columnNames;
	private String [][] data;
	private JScrollPane jpane;
	public JComboBox<String> comboBox1,
					comboBoxTarget,
					comboBox3Target,
					comboBox3Conjugate,
					comboBox4Target,
					comboBox4;
	JComboBox<Integer> comboBoxDensity;
	DefaultTableModel model,
					model4To;
	private JTable table_from;
	private JTable table_to;
	
	/**
	 * The constructor creates a panel.
	 * @param f:  the GoCytoFrame that this panel is associated with.
	 */
	public TestsetPanel( GoCytoFrame f) {
		
		ActionListener l = (ActionListener) new TestsetListener( this, f );
		JLabel lblTargetTestsetSetup = new JLabel("Target Testset Setup");
		lblTargetTestsetSetup.setFont(new Font("Lucida Grande", Font.BOLD, 15));
	
		JLabel lblCreateloadTestset = new JLabel("1. Create/Load testset:");
		lblCreateloadTestset.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		JLabel lblAdddeleteTarget = new JLabel("2. Add/Delete target and density:");
		lblAdddeleteTarget.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		this.f = f;
		
		
		//JComponents for #1.
		rdbtnNewTestset = new JRadioButton("New testset:");
		textField1 = new JTextField();
		textField1.setColumns(10);
		rdbtnLoadTestset = new JRadioButton("Load Testset:");
		comboBox1 = new JComboBox<String>();
		btnGroup1 = new ButtonGroup();
		btnGroup1.add( rdbtnLoadTestset );
		btnGroup1.add( rdbtnNewTestset );
		btnEnter1 = new JButton("Enter");
		
		//JComponents for #2.
		JLabel lblTarget = new JLabel("Target:");
		JLabel lblDensity = new JLabel("Density (1-10):");
		Integer [] densities = {1,2,3,4,5,6,7,8,9,10};
		Set <String> keys = f.getMyAntibodyDB().getAntibodies().keySet();
		String [] targets = (String[]) keys.toArray(new String [keys.size()] );
		comboBoxTarget = new JComboBox (targets);
		comboBoxDensity = new JComboBox ( densities );
		btnAdd2 = new JButton("Add");
		btnDelete2 = new JButton("Delete");
		
		//JComponents for #3.
		JLabel lblPreassignTarget = new JLabel("3. Preassign target to conjugate (optional):");
		lblPreassignTarget.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		JLabel lblNewLabel = new JLabel("Target:");
		comboBox3Target = new JComboBox<String>();
		JLabel lblConjugate = new JLabel("Conjugate:");
		comboBox3Conjugate = new JComboBox<String>();
		btnAssign = new JButton("Assign");
		
		//JComponenets for #4.
		//Add radio buttons for Assigning target groups into btnGroup
		JLabel lblAssignTarget = new JLabel("4. Assign target groups (optional):");
		lblAssignTarget.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		textField4 = new JTextField();
		textField4.setColumns(10);
		comboBox4 = new JComboBox<String> ();
		rdbtnNew = new JRadioButton("New group:");
		rdbtnDelete = new JRadioButton("Delete:");
		btnGroup4 = new ButtonGroup();
		btnGroup4.add( rdbtnNew );
		btnGroup4.add( rdbtnDelete );
		btnAdd = new JButton("Add");
		btnRemove = new JButton("Remove");
		rdbtnRequiredGroup = new JRadioButton("Required group");
		btnEnter4 = new JButton("Enter");
		JLabel lblTargetTable = new JLabel("5. Target Table: ");
		lblTargetTable.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblTestsetName = new JLabel("Testset Name");
		
		//Creating tables for #4
		table_to = new JTable();
		String[] columnNames4 = new String[]{"Target"};
		String[][] data4 = new String[][]{};
		model4To = new DefaultTableModel(data4, columnNames4){
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable( int row, int col) {
				return false;
			}
		};
		table_to = new JTable(model4To);
		JScrollPane jpaneFrom = new JScrollPane( table_to );
		
		comboBox4Target = new JComboBox<String>();
		
		//Creating table for #5
		columnNames = new String[] {"TARGET", "DENSITY", "ASSIGNED","GROUP NAME(S)", "REQUIRED"};
		data = new String[][] {};
		model = new DefaultTableModel( data, columnNames){
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable( int row, int col) {
				return false;
			}
		};
		table = new JTable( model );
		JScrollPane jpane = new JScrollPane( table );
		
		btnSave = new JButton("Save");
		btnNext = new JButton("Next");
		btnPrevious = new JButton("Previous");
		btnHelp = new JButton("Help");

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
					.addComponent(lblTargetTestsetSetup)
					.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblCreateloadTestset)
								.addComponent(lblAdddeleteTarget)
								.addGroup(groupLayout.createSequentialGroup()
									.addContainerGap()
									.addComponent(lblTarget)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(comboBoxTarget, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(5)
									.addComponent(lblDensity)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(comboBoxDensity, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)))
							.addGroup(groupLayout.createSequentialGroup()
								.addGap(52)
								.addComponent(btnAdd2)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnDelete2))
							.addGroup(groupLayout.createSequentialGroup()
								.addGap(11)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(rdbtnNewTestset)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(rdbtnLoadTestset)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(comboBox1, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnEnter1)))
						.addPreferredGap(ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
							.addGroup(groupLayout.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
									.addComponent(btnEnter4)
									.addGroup(groupLayout.createSequentialGroup()
										.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
											.addComponent(rdbtnRequiredGroup)
											.addGroup(groupLayout.createSequentialGroup()
												.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
													.addGroup(groupLayout.createSequentialGroup()
														.addComponent(comboBox4Target, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
														.addGap(27)
														.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
															.addComponent(btnRemove)
															.addComponent(btnAdd))
														.addPreferredGap(ComponentPlacement.UNRELATED))
													.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
														.addComponent(rdbtnDelete)
														.addPreferredGap(ComponentPlacement.RELATED)))
												.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
													.addComponent(comboBox4, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
													.addComponent(jpaneFrom, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE))
												.addGap(6)))
										.addPreferredGap(ComponentPlacement.RELATED)))
								.addGap(10))
							.addGroup(groupLayout.createSequentialGroup()
								.addGap(45)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addGroup(groupLayout.createSequentialGroup()
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
											.addGroup(groupLayout.createSequentialGroup()
												.addGap(12)
												.addComponent(lblNewLabel)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(comboBox3Target, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
											.addGroup(groupLayout.createSequentialGroup()
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
													.addComponent(lblAssignTarget)
													.addGroup(groupLayout.createSequentialGroup()
														.addGap(12)
														.addComponent(lblConjugate)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(comboBox3Conjugate, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
													.addGroup(groupLayout.createSequentialGroup()
														.addGap(6)
														.addComponent(rdbtnNew)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(textField4, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)))))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnAssign))
									.addComponent(lblPreassignTarget))
								.addContainerGap(125, Short.MAX_VALUE)))))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblTargetTable)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblTestsetName))
						.addComponent(jpane, GroupLayout.PREFERRED_SIZE, 487, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
					.addComponent(btnHelp)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnPrevious)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNext)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSave)
					.addGap(9))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(lblTargetTestsetSetup)
					.addGap(40)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblCreateloadTestset)
								.addComponent(lblPreassignTarget))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(comboBox3Target, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
								.addComponent(rdbtnNewTestset)
								.addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel))
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(6)
									.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
										.addComponent(rdbtnLoadTestset)
										.addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(comboBox3Conjugate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblConjugate)
										.addComponent(btnAssign))
									.addGap(12)
									.addComponent(lblAssignTarget)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
										.addComponent(rdbtnNew)
										.addComponent(textField4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(comboBox4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(rdbtnDelete))
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup()
											.addGap(18)
											.addComponent(btnAdd)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(btnRemove))
										.addGroup(groupLayout.createSequentialGroup()
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(jpaneFrom, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
										.addGroup(groupLayout.createSequentialGroup()
											.addGap(37)
											.addComponent(comboBox4Target, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(32)
									.addComponent(btnEnter1)
									.addGap(7)
									.addComponent(lblAdddeleteTarget)
									.addGap(39)
									.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblDensity)
										.addComponent(comboBoxDensity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
										.addComponent(btnAdd2)
										.addComponent(btnDelete2)))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(152)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblTarget)
								.addComponent(comboBoxTarget, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTargetTable)
						.addComponent(lblTestsetName))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addComponent(btnEnter4)
							.addPreferredGap(ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnNext)
								.addComponent(btnPrevious)
								.addComponent(btnHelp)
								.addComponent(btnSave))
							.addGap(14))
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jpane, GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
							.addContainerGap())))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(319)
					.addComponent(rdbtnRequiredGroup)
					.addContainerGap(143, Short.MAX_VALUE))
		);
		
		//Add ActionListener to JComponents for Step #1
		setLayout(groupLayout);
		btnNext.addActionListener(l);
		btnPrevious.addActionListener(l);
		btnHelp.addActionListener(l);
		btnSave.addActionListener(l);
		btnEnter1.addActionListener(l);
		
		//Step #2 JComponents
		btnAdd2.addActionListener(l);
		btnDelete2.addActionListener(l);
		
		//Step #3 JComponents
		btnAssign.addActionListener(l);
		comboBoxTarget.addActionListener(l);
		
		//Step #4 JComponents
		btnAdd.addActionListener(l);
		btnRemove.addActionListener(l);
		btnEnter4.addActionListener(l);
		rdbtnLoadTestset.addActionListener(l);
		rdbtnNew.addActionListener(l);
		rdbtnDelete.addActionListener(l);
		comboBox4.addActionListener(l);

		ItemListener itemListener = new ItemListener(){
			public void itemStateChanged( ItemEvent e ) {
			 if ( e.getSource().equals( comboBox3Target ) ) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					ArrayList <Antibody> antibodies = f.getMyAntibodyDB().getAntibodies((String)comboBox3Target.getSelectedItem());
					comboBox3Conjugate.removeAllItems();
						for ( int i=0; i<antibodies.size(); i++ ) {
							comboBox3Conjugate.addItem(antibodies.get(i).getConjugate());
						}
					}
				}
			}
		};
		//comboBox3Conjugate needs an itemListener
		comboBox3Target.addItemListener( itemListener );
	}
	
	public DefaultTableModel getModel() {
		return model;
	}
	
	//Removes all rows in model table (Step #5), and repopulates table with testset information
	public void repopulateTable() {
		model.setRowCount(0);
		Testset testset = ((GoCytoFrame) f).getMyTestset();
		for (Map.Entry<String, Target> entry : testset.getTargets().entrySet() ) {
			String key = entry.getKey();
			Target target1 = entry.getValue();
			String group = "";
			String preset = "";
			if (target1.getGroup().size() > 0 ) {
				group = target1.getGroup().toString();
			} 
			if ( testset.getPreset().containsKey(key)) {
				preset = testset.getPreset().get(key);
			} 
			model.addRow( new Object [] {key, target1.getDensity(), preset, group, target1.isRequired() });
		}
	}
}
