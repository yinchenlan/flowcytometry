package com.lansoft.flowcytometry.ui.nextgen;
/**
 * This class is the action listener for  TestsetPanel class.
 * @author jennychien
 */

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
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;

public class TestsetListener implements ActionListener {

	private TestsetPanel p;
	private GoCytoFrame f;
	private RunCytoPanel nextp;
	private HelpFrame h;
	private Testset testset;
	
	/**
	 * TestsetListener constructor takes a panel and a frame.
	 * @param panel: the TestsetPanel that listener is listening to.
	 * @param frame: the GoCytoFrame that contains the TestsetPanel
	 */
	public TestsetListener ( TestsetPanel panel, GoCytoFrame frame ) {
		f = frame;
		p = panel;
	}

	//Override actionPerformed to determine the logic of buttons and text components.
	public void actionPerformed(ActionEvent e) {
		
		Object source = e.getSource();
		
		//If the btnPrevious is pushed, the frame will set its panel to its AntibodyDBPanel
		if ( source == p.btnPrevious ) {
			f.removeMyPanel();
			f.setMyPanel( f.getMyAntibodyDBPanel() );
		}
		
		//If the btnNext is pushed, the frame will set its panel to its RunCytoPanel
		if ( source == p.btnNext ) {
			f.removeMyPanel();
			nextp = new RunCytoPanel( f );
			f.setMyPanel( nextp );
			f.setMyRunCytoPanel( nextp );
		}
		
		//btnHelp will create a new HelpFrame, and will add the testsetHelp panel to frame.
		if ( source == p.btnHelp ) {
			h = new HelpFrame();
			h.viewTestsetHelp();
		}
		
		//For Step #1, when user pushes the "Enter" button, will create new testset or 
		//load any testset from directory
		if ( source == p.btnEnter1 ) {
			
			//If new testset radio button is selected, create new testset
			if ( p.rdbtnNewTestset.isSelected() ) {
				testset = new Testset ( p.textField1.getText());
				p.lblTestsetName.setText( p.textField1.getText() );
				f.setMyTestset( testset );
			}
			
			//If want to load a testset, look into directory
			if ( p.rdbtnLoadTestset.isSelected() ) {
				try {
					ObjectInputStream inFileObj = new ObjectInputStream(
							 new BufferedInputStream(
							 new FileInputStream(f.getDirectory() + p.comboBox1.getSelectedItem())));
					testset = ( Testset ) inFileObj.readObject();
					f.setMyTestset( testset );
					inFileObj.close();
					 
				} catch ( IOException e1 ) {
					System.out.println( "Exception: " + e1.getMessage());
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//Display targets in table. Go through f.getMyTestset, add row to model
				//Also add to #3 preassignment comboBox and #4 comboBox
				p.repopulateTable();
				for (Map.Entry<String, Target> entry : ((Testset)f.getMyTestset()).getTargets().entrySet() ) {
					Target target1 = entry.getValue();
					p.comboBox3Target.addItem( target1.getName() );
					p.comboBox4Target.addItem( target1.getName() );
				}
			}
		}
		
		//If want to load a testset, look into directory, add *.testset files to comboBox1
		if ( p.rdbtnLoadTestset.isSelected() ) {
			File folder = new File (f.getDirectory());
			File[] files = folder.listFiles();
			p.comboBox1.removeAllItems();
			for ( int i=0; i<files.length; i++ ) {
				if (files[i].getName().endsWith(".testset")) {
					p.comboBox1.addItem(files[i].getName());
				}
			}
		}
		
		//For Step #2, add target to testset with selected density
		if ( source == p.btnAdd2 ) {
			Target target = new Target ((String)p.comboBoxTarget.getSelectedItem(), (Integer)p.comboBoxDensity.getSelectedItem());
			testset.addTarget( target );
			p.getModel().addRow( new Object [] {target.getName(), target.getDensity(), "","",""});
			p.comboBox3Target.addItem( target.getName() );
			
			//Add target to target group Step #4 from table
			p.comboBox4Target.addItem( target.getName() );
		}
		
		//For Step #2, delete target from targets and from table
		if ( source == p.btnDelete2) {
			if ( testset.contains( (String)p.comboBoxTarget.getSelectedItem()) ) {
				testset.remove( (String)p.comboBoxTarget.getSelectedItem());
				for (int i=0; i<p.getModel().getRowCount(); i++) {
					if ( p.getModel().getValueAt(i, 0) == (String)p.comboBoxTarget.getSelectedItem() ) {
						p.getModel().removeRow(i);
					}
				}
			}
		}
		
		//For Step #3, create a preset pair
		if ( source == p.btnAssign ) {
			testset.assign((String)p.comboBox3Target.getSelectedItem(), (String)p.comboBox3Conjugate.getSelectedItem());
			//iterate through #5 table, display group information about assignment for target
			for (int y=0; y<p.model.getRowCount(); y++ ) {
				if ( (boolean)p.model.getValueAt(y, 0).equals((String)p.comboBox3Target.getSelectedItem())) {
					p.model.setValueAt((String)p.comboBox3Conjugate.getSelectedItem(), y, 2);
				}
			}
		}
		
		//For Step #4, allow user to move targets from and to tables
		if ( source == p.btnAdd ) {
			p.model4To.addRow( new Object [] {p.comboBox4Target.getSelectedItem()});
		}
		
		//For Step#4, allow user to remove targets from group
		if ( source == p.btnRemove ) {
			for (int i=0; i<=p.model4To.getRowCount(); i++) {
				if (p.comboBox4Target.getSelectedItem().equals( p.model4To.getValueAt(i, 0))) {
					p.model4To.removeRow(i);
				}
			}
		}
		
		//For Step #4, assign group to set up testset group
		if ( source == p.btnEnter4 ) {
			
			//For #4 table, add targets to group, and assign in testset object
			if ( p.rdbtnNew.isSelected() ) {
				ArrayList <String> group = new ArrayList <String> ();
				for (int i=0; i<p.model4To.getRowCount(); i++ ) {
					group.add( (String)p.model4To.getValueAt(i, 0));
					//iterate through #5 table, display group information about group and required for target
					for (int y=0; y<p.model.getRowCount(); y++ ) {
						if ( (boolean)p.model.getValueAt(y, 0).equals((String)p.model4To.getValueAt(i, 0))) {
							p.model.setValueAt(p.textField4.getText(), y, 3);
							if ( p.rdbtnRequiredGroup.isSelected() ) {
								p.model.setValueAt("Yes", y, 4);
							}
						}
					}
				}
				//Add assignment to testset object
				testset.assignGroups( p.textField4.getText(), group);
				//Clear table when assigned
				p.textField4.setText("");
				p.model4To.setRowCount(0);
			}
	
			//For #4, if group is to be deleted, remove from testset groups and table
			if ( p.rdbtnDelete.isSelected() ) {
				//Iterate through target objects, and remove group
				ArrayList <String> deleteGroupTargets = f.getMyTestset().getTargetGroups().get( p.comboBox4.getSelectedItem() );
				for (int a=0; a<deleteGroupTargets.size(); a++ ){
					f.getMyTestset().getTargets().get( deleteGroupTargets.get(a) ).removeGroup( (String)p.comboBox4.getSelectedItem() );
				}
				f.getMyTestset().removeGroup( (String)p.comboBox4.getSelectedItem() );
				//Iterate through table to reset target groups displayed
				p.repopulateTable();
			}
			
		}
		
		//Save Testset object to file
		if ( source == p.btnSave ) {
			try {
				ObjectOutputStream objectOut = new ObjectOutputStream (
									new FileOutputStream(f.getDirectory() + f.getMyTestset().getName() + ".testset"));
				objectOut.writeObject( f.getMyTestset() );
				
			} catch ( Exception e1 ) {
				System.out.println("Exception: " + e1.getMessage() );
			}
		}
		
		//For Step #4, add targets to comboBox for deleting
		if ( source == p.rdbtnDelete) {
			p.comboBox4.removeAllItems();
			for (Map.Entry<String, ArrayList<String>> entry : f.getMyTestset().getTargetGroups().entrySet() ) {
				String k = entry.getKey();
				p.comboBox4.addItem( k );
			}
		}
	}
	
}
