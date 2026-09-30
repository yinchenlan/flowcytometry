package com.lansoft.flowcytometry.ui.nextgen;
/**
 * This class is the ActionListener for the CytoSetupPanel.
 * @author jennychien
 */
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import javax.swing.table.DefaultTableModel;


public class CytoSetupListener implements ActionListener {

	
	private CytoSetupPanel p;
	private GoCytoFrame f;
	private AntibodyDBPanel nextp;
	private HelpFrame h;
	private String detectorName;
	private ObjectOutputStream objectOut;
	
	/**
	 * The constructor takes 2 parameters:
	 * @param panel: The panel from which the listener is added to.
	 * @param frame:  The frame that contains the panel. 
	 */
	public CytoSetupListener ( CytoSetupPanel panel, GoCytoFrame frame ) {
		p = panel;
		f = frame;
	}
	
	public void actionPerformed (ActionEvent e) {
		
		Object source = e.getSource();
		
		//btnPrevious will set the panel in frame as its IntroPanel
		if ( source == p.btnPrevious ) {
			f.removeMyPanel();
			f.setMyPanel( f.getMyIntroPanel() );
		}
		
		//btnNext create a new AntibodyDBPanel, and will set the panel in 
		//frame as this new panel.
		if ( source == p.btnNext ) {
			f.removeMyPanel();
			nextp = new AntibodyDBPanel ( f );
			f.setMyPanel( nextp );
			f.setMyAntibodyDBPanel( nextp );
		}
		
		//btnHelp will create a new HelpFrame, and will add the CytoHelp
		//panel to the frame.
		if ( source == p.btnHelp ) {
			h = new HelpFrame();
			h.viewCytoHelp();
		}
		
		//If rdbtn for "Enter new detector", make textField editable
		if ( source == p.rdbtnNew ) {
			p.textField.setEditable(true);
			p.textField.setEnabled(true);
		}
		
		//If user wants to save a detector
		if ( source == p.btnSaveDetector ) {
			
			//And the detector is a new detector with a new name
			if ( p.rdbtnNew.isSelected() ) {
				detectorName = p.textField.getText();
				Detector detector = new Detector (detectorName);
				String wavelength = p.textField2.getText();
				detector.setWavelength( wavelength );
	
				//Iterate through table and add conjugate/brightness to detector.
				//Add appropriate rows to tableFC
				int i = p.tableCB.getModel().getRowCount();
				for (int rows=0; rows < i; rows++ ) {
					p.getModelFC().addRow(new Object [] {detectorName, wavelength, p.tableCB.getValueAt(rows, 0), p.tableCB.getValueAt(rows, 1)} );
					detector.addConjugate(p.tableCB.getValueAt(rows, 0), p.tableCB.getValueAt(rows, 1) );
				}
		
				//Clear fields after adding the detector
				p.radioGroup.clearSelection();
				p.textField.setText("");
				p.textField.setEnabled(false);
				p.textField2.setText("");
				for (int rows=0; rows < i; rows++ ) {
					p.tableCB.setValueAt("", rows, 0);
					p.tableCB.setValueAt("", rows, 1);
				}
				
				//Add detector to flow cytometer
				f.getFlowcytometer().addDetector( detector );

			}
		}
		
		//If save button is pressed, open file and write flowcytometer object to file
		if ( source == p.btnSave ) {
			try {
				objectOut = new ObjectOutputStream (
									new FileOutputStream(f.getDirectory() + p.getCytoName() + ".cyto"));
				objectOut.writeObject( f.getFlowcytometer() );
				
			} catch ( Exception e1 ) {
				System.out.println("Exception: " + e1.getMessage() );
			}
		}
		
		//Need to add a row to the JTable
		if ( source == p.btnAddRow ) {
			p.getModel().addRow(new Object[] {"", "0"});
		}
	
		//If delete detector/conjugates from detector setup
		if ( source == p.btnDelete ) {
			int numRows = p.tableFC.getSelectedRows().length;
			for (int i=0; i<numRows; i++ ) {
				String detector = (String) p.getModelFC().getValueAt(p.tableFC.getSelectedRow(),0);
				String conjugate = (String) p.getModelFC().getValueAt(p.tableFC.getSelectedRow(), 2);
				System.out.print( conjugate.toString() );
				for (int y=0; y<f.getFlowcytometer().getDetectors().size(); y++ ) {
					if ( f.getFlowcytometer().getDetectors().get(y).getConjugate().containsKey( conjugate ) ) {
						if (f.getFlowcytometer().getDetectors().get(y).getConjugate().size() == 1 ) {
							f.getFlowcytometer().getDetectors().remove(y);
						} else {
							f.getFlowcytometer().getDetectors().get(y).getConjugate().remove( conjugate );
						}
						System.out.println( "Deleted " + detector + " " + conjugate );
					}
				}
				p.getModelFC().removeRow(p.tableFC.getSelectedRow());
			}
		}
	}
}
