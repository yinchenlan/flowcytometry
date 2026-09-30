package com.lansoft.flowcytometry.ui.nextgen;
/**
 * This class creates an Antibody Database Panel. This panel
 * allows user to set up the database of conjugated antibodies
 * available.
 * @author jennychien
 */

import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;


public class AntibodyDBPanel extends JPanel {

	//JComponenets are public for the AntibodyDBListener to access.
	private static final long serialVersionUID = 1L;
	private GoCytoFrame f;
	public JTable dbTable,
				addTable;
	public ButtonGroup buttonGroup;
	public JButton btnNext,
				btnPrevious,
				btnSave,
				btnHelp,
				btnLoad,
				btnAdd,
				btnDelete;
	public JRadioButton rdbtnImport,
						rdbtnLoadDatabase;
	public JTextField textField,
					textField2;
	private DefaultTableModel model,
							model2;
	public JLabel lblLabel2;
	public JComboBox<String> comboBox;
	private JLabel lblAddAntibody;
	
	/**
	 * Create the panel. The constructor takes 1 parameter.
	 * @param f: GoCytoFrame which this panel is associated with.
	 */
	public AntibodyDBPanel (GoCytoFrame f) {
		this.f = f;
		ActionListener l = (ActionListener) new AntibodyDBListener( this, f );
		
		JLabel titleLabel = new JLabel("Antibody Database Setup");
		titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		JLabel loadImportLabel = new JLabel("1. Load/import database.");
		loadImportLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		JLabel editLabel = new JLabel("2. Database:");
		editLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		//Create table to display antibody database.  Add JTable to
		//JScrollPane
		String [] columnNames = {"CONJUGATE","TARGET","SPECIES","COMPANY","CATALOG#","LOT#","CLONE","ISOTYPE","AUPS"};
		String [][] data = {};
		model = new DefaultTableModel( data, columnNames ){
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable( int row, int col) {
				return false;
			}
		};
		dbTable = new JTable( model );
		JScrollPane jpane = new JScrollPane( dbTable );
		jpane.setEnabled(true);
	
		btnSave = new JButton("Save");
		btnNext = new JButton("Next");
		btnPrevious = new JButton("Previous");
		btnHelp = new JButton("Help");
		
		JLabel lblNewLabel = new JLabel("(Conjugate, Target, Species, Company, Catalog, Lot, Clone, Isotype, AUPS)");
		
		textField = new JTextField();
		textField.setEditable(true);
		textField.setText("/Users/jennychien/Documents/GoCyto/Fortessa_antibodyDB.xlsx");
		textField.setColumns(10);
		btnLoad = new JButton("Load");
		
		//Create radio buttons, and add them to button group
		rdbtnImport = new JRadioButton("Import Excel file (*.xlsx):");
		buttonGroup = new ButtonGroup();
		buttonGroup.add(rdbtnImport);
		JLabel lblEnterNewDatabase = new JLabel("Enter new database name:");
		
		textField2 = new JTextField();
		textField2.setEditable(false);
		textField2.setColumns(10);
		lblLabel2 = new JLabel("New label");
		btnDelete = new JButton("Delete");
		
		rdbtnLoadDatabase = new JRadioButton("Load antibody database:");
		buttonGroup.add(rdbtnLoadDatabase);
		comboBox = new JComboBox<String>();
		
		lblAddAntibody = new JLabel("3. Add Antibody (optional):");
		lblAddAntibody.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		
		//Step #2: create JTable and Add button
		btnAdd = new JButton("Add");
		String [] columnNames2 = {"CONJUGATE","TARGET","SPECIES","COMPANY","CATALOG#","LOT#","CLONE","ISOTYPE","AUPS"};
		String [][] data2 = {{"","","","","","","","",""}};
		model2 = new DefaultTableModel( data2, columnNames2 ){
			private static final long serialVersionUID = 2L;
			public boolean isCellEditable( int row, int col) {
				return true;
			}
		};
		addTable = new JTable( model2 );
		JScrollPane jpane2 = new JScrollPane( addTable );
		jpane2.setEnabled(true);
		
		//Layout of JComponents
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(472, Short.MAX_VALUE)
					.addComponent(btnHelp)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnPrevious)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNext)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSave))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(titleLabel)
					.addContainerGap(609, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(loadImportLabel)
					.addContainerGap(636, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(jpane2, GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnAdd)
					.addGap(15))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(jpane, GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnDelete)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblAddAntibody)
					.addContainerGap(624, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(24)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(29)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup()
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(lblEnterNewDatabase)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(textField2, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE))
										.addComponent(lblNewLabel)))
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(rdbtnLoadDatabase)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(btnLoad))
									.addGroup(groupLayout.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(rdbtnImport)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textField, 364, 364, 364)))))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(editLabel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblLabel2)))
					.addContainerGap(230, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(titleLabel)
					.addGap(26)
					.addComponent(loadImportLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(rdbtnImport))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEnterNewDatabase))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(rdbtnLoadDatabase)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnLoad))
					.addPreferredGap(ComponentPlacement.UNRELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(editLabel)
						.addComponent(lblLabel2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(jpane, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnDelete)
							.addGap(8)))
					.addGap(19)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblAddAntibody)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(jpane2, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnAdd))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSave)
						.addComponent(btnNext)
						.addComponent(btnPrevious)
						.addComponent(btnHelp))
					.addContainerGap())
		);
		
		//Add ActionListener to JButtons
		setLayout(groupLayout);
		btnSave.addActionListener(l);
		btnNext.addActionListener(l);
		btnPrevious.addActionListener(l);
		btnHelp.addActionListener(l);
		btnLoad.addActionListener(l);
		rdbtnImport.addActionListener(l);
		btnDelete.addActionListener(l);
		btnAdd.addActionListener(l);
		rdbtnLoadDatabase.addActionListener(l);
		
	}
	
	public GoCytoFrame getF() {
		return f;
	}
	
	public DefaultTableModel getModel() {
		return model;
	}
	
	public DefaultTableModel getModel2() {
		return model2;
	}
}
