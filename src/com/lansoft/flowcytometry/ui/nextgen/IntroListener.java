package com.lansoft.flowcytometry.ui.nextgen;
/**
 * This class is the actionlistener for IntroPanel class.
 * @author jennychien
 */

import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.TreeMap;


public class IntroListener implements ActionListener  {
	
	private IntroPanel p;
	private GoCytoFrame f;
	private CytoSetupPanel nextp;
	private String directory;
	private File [] files;

	/**
	 * IntroListener constructor takes a panel and a frame
	 * @param panel: IntroPanel that listener is listening to
	 * @param frame:  GoCytoFrame that contains the IntroPanel
	 */
	public IntroListener (IntroPanel panel, GoCytoFrame frame ) {
		f = frame;
		p = panel;
	}

	//Override actionPerformed to determine the logic of buttons and text components.
	public void actionPerformed (ActionEvent e)  {
		
		Object source = e.getSource();
		
		//If radio button to setup is selected, enable textField
		if ( source == p.radioSetup ) {
			p.nameField.setEnabled(true);
			p.nameField.setEditable(true);
		}
		
		//If the next button is pushed
		if ( source == p.introNextButton ) {
			
			//If user wants to setup new flow cytometer detectors, listener
			//will remove the current panel, create a new CytoSetupPanel, and
			//have the frame set it as its current panel.
			if ( p.radioSetup.isSelected() ) {
				String name = p.nameField.getText();
				FlowCytometer fc = new FlowCytometer(name);
				f.setFlowcytometer( fc );
				
				nextp = new CytoSetupPanel( name, f);
				f.setMyCytoSetupPanel( nextp );
				f.removeMyPanel();
				f.setMyPanel( nextp );
			}
			
			//Load saved Cytometer paramter file according to selection from comboBox
			//Open FileInputStream and read object. Set frame flow cytometer to read object.
			if ( p.radioLoad.isSelected() ) {
				String filename = f.getDirectory() + (String) p.comboBox.getSelectedItem();
				try {
					ObjectInputStream inFileObj = new ObjectInputStream(
							 new BufferedInputStream(
							 new FileInputStream( filename )));
					f.setFlowcytometer( ( FlowCytometer) inFileObj.readObject());
					inFileObj.close();
					 
				} catch ( IOException e1 ) {
					System.out.println( "Exception: " + e1.getMessage());
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//Remove the current panel, create a new CytoSetupPanel, and
				//have the frame set it as its current panel.
				String flowName = ((String) p.comboBox.getSelectedItem()).replaceAll(".cyto","");
				nextp = new CytoSetupPanel( flowName, f);
				f.setMyCytoSetupPanel( nextp );
				f.removeMyPanel();
				f.setMyPanel( nextp );
	
			}
		}
	
		
		//set Directory for JFrame, for any files to be saved/read
		//Find files with *.cytometer, and load detector objects
		if ( source == p.btnEnter ) {
			directory = p.textField.getText();
			File folder = new File (directory);
			files = folder.listFiles();
			f.setDirectory( directory );
			
			for ( int i=0; i<files.length; i++ ) {
				if (files[i].getName().endsWith(".cyto")) {
					p.comboBox.addItem(files[i].getName());
				}
			}
		}

	}
}
