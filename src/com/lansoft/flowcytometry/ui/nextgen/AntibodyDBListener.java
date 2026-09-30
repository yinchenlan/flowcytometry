package com.lansoft.flowcytometry.ui.nextgen;
/**
 * This class is the ActionListener for the AntibodyDBPanel.
 * @author jennychien 
 */

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


public class AntibodyDBListener implements ActionListener{
	
	private AntibodyDBPanel p;
	private GoCytoFrame f;
	private TestsetPanel nextp;
	private HelpFrame helpf;
	private AntibodyDB antibodyDB;
	private ObjectOutputStream objectOut;
	
	/**
	 * The contructor takes to 2 parameters
	 * @param panel: The AntibodyDBPanel which the listener is added to.
	 * @param frame:  The GoCytoFrame that contains the panel.
	 */
	public AntibodyDBListener ( AntibodyDBPanel panel, GoCytoFrame frame ) {
		p = panel;
		f = frame;
	}
	
	//actionPerforme
	public void actionPerformed (ActionEvent e) {
		
		Object source = e.getSource();
		
		//btnPrevious will set the panel of the frame to its CytoSetupPanel.
		if ( source == p.btnPrevious ) {
			f.removeMyPanel();
			f.setMyPanel( f.getMyCytoSetupPanel() );
		}
		
		//btnNext will create a new TestsetPanel, and set the panel of
		//the frame to the new panel.
		if ( source == p.btnNext ) {
			if ( f.getMyAntibodyDB() != null ) {
				f.removeMyPanel();
				nextp = new TestsetPanel( f );
				f.setMyPanel( nextp );
				f.setMyTestsetPanel( nextp );
			}
		}
		
		//btnHelp will open an Help frame.
		if ( source == p.btnHelp ) {
			helpf = new HelpFrame();
			helpf.viewAntibodyDBHelp();
		}
		
		
		//rdbtnImport will set textFields to be editable
		if ( source == p.rdbtnImport ) {
			p.textField.setEditable(true);
			p.textField2.setEditable(true);
		}
		
		//btnLoad will load the antibody database into the table
		if ( source == p.btnLoad ) {
			
			//If rdbtnImport is selected, create new antibodyDB from file
			if ( p.rdbtnImport.isSelected() ) {
				setAntibodyDB( new AntibodyDB ( p.textField.getText() ));
				//Set name of new antibodyDB and set database of GoCyto JFrame
				antibodyDB.setName( p.textField2.getText());
				
			}
			
			//Load from file if rdbtnLoadDatabase is selected
			if ( p.rdbtnLoadDatabase.isSelected() ) {
				String filename = f.getDirectory() + (String) p.comboBox.getSelectedItem();
				try {
					ObjectInputStream inFileObj = new ObjectInputStream(
							 new BufferedInputStream(
							 new FileInputStream( filename )));
					setAntibodyDB( (AntibodyDB) inFileObj.readObject());
					inFileObj.close();
					 
				} catch ( IOException e1 ) {
					System.out.println( "Exception: " + e1.getMessage());
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
			
			//Add rows to table model from antibodyDB.
			for (Map.Entry < String, ArrayList<Antibody>> entry: getAntibodyDB().getAntibodies().entrySet() ) {
				ArrayList<Antibody> value = entry.getValue();
				for(Antibody ant : value ) {
					p.getModel().addRow( new Object [] {ant.getConjugate(), ant.getTarget(), ant.getSpecies(), ant.getCompany(), ant.getCatalog(), ant.getLot(), ant.getClone(), ant.getIsotype(), ant.getAups()});
					
				}
			}
			
			//Set GoCyto JFrame antibodyDB
			f.setMyAntibodyDB( antibodyDB );
			p.lblLabel2.setText( f.getMyAntibodyDB().getName() );
			
		}
		
		//btnSave will save AntibodyDB to file
		if ( source == p.btnSave ) {
			try {
				objectOut = new ObjectOutputStream (
									new FileOutputStream(f.getDirectory() + f.getMyAntibodyDB().getName() + ".db"));
				objectOut.writeObject( f.getMyAntibodyDB() );
				
			} catch ( Exception e1 ) {
				System.out.println("Exception: " + e1.getMessage() );
			}
			
		}
		

		//Step #2, add row to antibodyDB
		if ( source == p.btnAdd ) {
			p.getModel().addRow( new Object [] {p.getModel2().getValueAt(0, 0), p.getModel2().getValueAt(0, 1), p.getModel2().getValueAt(0, 2), 
					p.getModel2().getValueAt(0, 3), p.getModel2().getValueAt(0, 4), p.getModel2().getValueAt(0, 5), p.getModel2().getValueAt(0, 6), 
					p.getModel2().getValueAt(0, 7),  p.getModel2().getValueAt(0, 8)});
			//System.out.println((String)p.getModel2().getValueAt(0, 8));
			Antibody antibody = new Antibody ((String)p.getModel2().getValueAt(0, 0), (String)p.getModel2().getValueAt(0, 1), 
					(String)p.getModel2().getValueAt(0, 2), (String)p.getModel2().getValueAt(0, 3), (String)p.getModel2().getValueAt(0, 4),
					(String)p.getModel2().getValueAt(0, 5), (String)p.getModel2().getValueAt(0, 6), (String)p.getModel2().getValueAt(0, 7), 
					(Integer)Integer.valueOf((String)p.getModel2().getValueAt(0, 8)));
			f.getMyAntibodyDB().addAntibody( antibody );
			p.getModel2().setValueAt("", 0, 0);
			p.getModel2().setValueAt("", 0, 1);
			p.getModel2().setValueAt("", 0, 2);
			p.getModel2().setValueAt("", 0, 3);
			p.getModel2().setValueAt("", 0, 4);
			p.getModel2().setValueAt("", 0, 5);
			p.getModel2().setValueAt("", 0, 6);
			p.getModel2().setValueAt("", 0, 7);
			p.getModel2().setValueAt("", 0, 8);
		}
		
		//Load antibody database
		if ( p.rdbtnLoadDatabase.isSelected() ) {
			File folder = new File (f.getDirectory());
			File[] files = folder.listFiles();
			
			for ( int i=0; i<files.length; i++ ) {
				if (files[i].getName().endsWith(".db")) {
					p.comboBox.addItem(files[i].getName());
				}
			}
		}
		
		//btnDelete will delete row from the table
		if ( source == p.btnDelete ) {
			int numRows = p.dbTable.getSelectedRows().length;
			for (int i=0; i<numRows; i++ ) {
				String target = (String) p.dbTable.getModel().getValueAt(p.dbTable.getSelectedRow(),1);
				String conjugate = (String) p.dbTable.getModel().getValueAt(p.dbTable.getSelectedRow(), 0);
				for (int y=0; y<f.getMyAntibodyDB().getAntibodies().get(target).size(); y++ ) {
					//If conjugates are the same, and if only 1 conjugate left,remove target, else remove conjugate
					if ( f.getMyAntibodyDB().getAntibodies().get(target).get(y).getConjugate().equals( conjugate ) ) {
						if (f.getMyAntibodyDB().getAntibodies().get(target).get(y).getConjugate().length() == 1 )  {
							f.getMyAntibodyDB().getAntibodies().remove(target);
						} else {
							f.getMyAntibodyDB().getAntibodies().get(target).remove(y);
						}
						//System.out.println( "Deleted " + target + " " + conjugate );
					}
				}
				p.getModel().removeRow(p.dbTable.getSelectedRow());
			}
		}
	}

	public AntibodyDB getAntibodyDB() {
		return antibodyDB;
	}
	
	public void setAntibodyDB ( AntibodyDB a ) {
		this.antibodyDB = a;
	}

}
