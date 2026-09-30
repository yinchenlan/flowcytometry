package com.lansoft.flowcytometry.ui.nextgen;
/**
 * This class creates a Flow Cytometer Setup panel. The
 * panel allows user to assign conjugates and average 
 * filter wavelength to detectors, which are assigned to
 * a speific cytometer.
 * @author jennychien
 */


import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.JTable;
import java.awt.SystemColor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.JRadioButton;
import java.util.TreeMap;
import java.util.Map;
import java.util.Set;

public class CytoSetupPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	//JButtons made public for CytoSetupListener to access.
	private String cytoName;
	public JButton btnPrevious,
					btnNext,
					btnHelp, 
					btnSave,
					btnSaveDetector,
					btnAddRow,
					btnDelete;
	public ButtonGroup radioGroup;
	public JRadioButton rdbtnNew;
	public JTextField textField, 
					textField2;
	public JTable tableCB,
					tableFC;

	private TableColumnModel cm;
	private DefaultTableModel model,
							modelFC;
	private JScrollPane jpaneFC;
	private JLabel lblForDetector;
	
	/**
	 * Create the frame.
	 * @param name: Name given to the flow cytometer given by user
	 * @param f: GoCytoFrame that the panel is going to be added to. Needs to
	 * be passed onto the listener.
	 */
	public CytoSetupPanel( String name, GoCytoFrame f) {
		setBorder(null);
		setToolTipText("");
		
		//JComponents instantiated for the CytSetupPanel class.
		ActionListener l = (ActionListener) new CytoSetupListener (this, f);
		cytoName = name;
		
		JLabel titleLabel = new JLabel("Detector Setup");
		titleLabel.setBackground(SystemColor.window);
		titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		
		JLabel nameLabel = new JLabel("for " + cytoName);
	
		btnNext = new JButton("Next");
		btnPrevious = new JButton("Previous");
		btnHelp = new JButton("Help");
		btnSave = new JButton("Save");

		JLabel lblAddeditDetector = new JLabel("1. Add/Edit detector");
		lblAddeditDetector.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		textField = new JTextField();
		textField.setEnabled(false);
		textField.setEditable(false);
		textField.setColumns(10);
		JLabel lblEnterAverage = new JLabel("2. Enter emission wavelength");
		lblEnterAverage.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		JLabel lblEnterAssociated = new JLabel("3. Enter detector's associated conjugates");
		lblEnterAssociated.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		JLabel lblBrightnessIndex = new JLabel("and its brightness index (1-10):");
		lblBrightnessIndex.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		//Create tableCB for entering Conjugate and Brightness index
		String [] columnNames = {"CONJUGATE", "BRIGHTNESS"};
		String [] brightness = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
		String [] [] data = {{"", brightness[0],}, {"", brightness[0]}, {"", brightness[0]}};
		model = new DefaultTableModel( data, columnNames );
		tableCB = new JTable(model);
		tableCB.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		tableCB.setCellSelectionEnabled(true);
		tableCB.setColumnSelectionAllowed(true);
		
		//Create DefaultComboBoxModel with values of brightness.
		//Add tableCB to JScrollPane
		DefaultComboBoxModel<String> cm = new DefaultComboBoxModel<String>( brightness );
		JComboBox<String> brightnessCombo = new JComboBox<String>();
		brightnessCombo.setModel( cm );
		TableColumn col = tableCB.getColumnModel().getColumn(1);
		col.setCellEditor( new DefaultCellEditor( brightnessCombo ));
		JScrollPane jpane = new JScrollPane(tableCB);
		jpane.setToolTipText("");
		btnSaveDetector = new JButton("Save detector");
		
		textField2 = new JTextField();
		textField2.setColumns(10);
		btnAddRow = new JButton("Add row");
		
		JLabel lblFlowCytometer = new JLabel("4. Flow Cytometer detectors:");
		lblFlowCytometer.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		//Create table to display all Flow Cytometer settings.
		//Add tableFC to JScrollPane
		String [] columnNamesFC = {"DETECTOR", "WAVELENGTH", "CONJUGATE", "BRIGHTNESS"};
		String [] [] dataFC = {};
		modelFC = new DefaultTableModel( dataFC, columnNamesFC ){
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable( int row, int col) {
				return false;
			}
		};
		tableFC = new JTable( modelFC );
		jpaneFC = new JScrollPane( tableFC );
		
		//Add JRadioButtons to a group
		radioGroup = new ButtonGroup();
		rdbtnNew = new JRadioButton("Enter new detector:");
		radioGroup.add( rdbtnNew );
		
		lblForDetector = new JLabel("for the detector: ");
		lblForDetector.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		btnDelete = new JButton("Delete");
		
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(8)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addGroup(groupLayout.createSequentialGroup()
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
											.addGroup(groupLayout.createSequentialGroup()
												.addGap(4)
												.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
													.addComponent(lblEnterAverage)
													.addComponent(textField, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)))
											.addGroup(groupLayout.createSequentialGroup()
												.addGap(36)
												.addComponent(textField2, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE))
											.addGroup(groupLayout.createSequentialGroup()
												.addGap(26)
												.addComponent(rdbtnNew)))
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
											.addGroup(groupLayout.createSequentialGroup()
												.addGap(132)
												.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
													.addGroup(groupLayout.createSequentialGroup()
														.addComponent(jpane, GroupLayout.PREFERRED_SIZE, 337, GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(btnAddRow))
													.addComponent(btnSaveDetector)))
											.addGroup(groupLayout.createSequentialGroup()
												.addGap(120)
												.addComponent(jpaneFC, GroupLayout.PREFERRED_SIZE, 435, GroupLayout.PREFERRED_SIZE))
											.addGroup(groupLayout.createSequentialGroup()
												.addGap(112)
												.addComponent(lblBrightnessIndex, GroupLayout.PREFERRED_SIZE, 227, GroupLayout.PREFERRED_SIZE))))
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblAddeditDetector)
										.addGap(164)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
											.addComponent(lblFlowCytometer)
											.addGroup(groupLayout.createSequentialGroup()
												.addComponent(lblEnterAssociated, GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
												.addGap(28)))))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(btnHelp)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnPrevious)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnNext)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnSave)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDelete))
						.addComponent(titleLabel)
						.addComponent(nameLabel)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(25)
							.addComponent(lblForDetector)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(titleLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(nameLabel)
					.addGap(59)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAddeditDetector)
						.addComponent(lblEnterAssociated))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnNew)
						.addComponent(lblBrightnessIndex))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnAddRow))
						.addComponent(jpane, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnSaveDetector)
						.addComponent(lblEnterAverage))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addComponent(lblFlowCytometer)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(jpaneFC, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(6)
							.addComponent(lblForDetector)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(71)
							.addComponent(btnDelete)))
					.addGap(42)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNext)
						.addComponent(btnPrevious)
						.addComponent(btnHelp)))
		);
		setLayout(groupLayout);
		
		//Add ActionListener to JComponents
		btnPrevious.addActionListener(l);
		btnNext.addActionListener(l);
		btnHelp.addActionListener(l);
		btnSave.addActionListener(l);
		rdbtnNew.addActionListener(l);
		btnAddRow.addActionListener(l);
		btnSaveDetector.addActionListener(l);
		btnDelete.addActionListener(l);
		
		//If frame's flow cytometer's detectors are not null, set tableFC to show data from flow cytometer.
		if (f.getFlowcytometer().getArrayLength() > 0 && f.getFlowcytometer() != null) {
			for (int i=0; i < f.getFlowcytometer().getDetectors().size(); i++ ) {
				Detector d = f.getFlowcytometer().getDetectors().get(i);
				for (Map.Entry <String, String> entry : d.getConjugate().entrySet()) {	
					this.getModelFC().addRow(new Object [] {d.getName(), d.getWavelength(), entry.getKey(), entry.getValue()});
				}
				
			}
		}
		
	}
	
	public String getCytoName() {
		return cytoName;
	}

	public TableColumnModel getCm() {
		return cm;
	}

	public void setCm(TableColumnModel cm) {
		this.cm = cm;
	}

	public DefaultTableModel getModel() {
		return model;
	}

	public void setModel(DefaultTableModel model) {
		this.model = model;
	}
	
	public DefaultTableModel getModelFC() {
		return modelFC;
	}
	
	public void setModelFC(DefaultTableModel model) {
		this.model = model;
	}
}
