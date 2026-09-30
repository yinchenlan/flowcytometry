package com.lansoft.flowcytometry.ui.nextgen;
/**
 * This class is a JFrame that can display multiple panels. Each help
 * panel explains what is to be done for each main panel.
 * @author jennychien
 */

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.SystemColor;

public class HelpFrame extends JFrame {

	
	private JPanel cytoSetupHelpPanel,
				antibodyDBHelpPanel,
				testsetHelpPanel,
				runCytoHelpPanel,
				resultsHelpPanel;
	
	private JTextArea cytoSetupText,
					antibodyDBText,
					testsetText,
					runCytoText,
					resultsText;

	/**
	 * Create the frame.
	 */
	public HelpFrame() {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 200, 351, 227);
		Border textBorder = BorderFactory.createEtchedBorder();
		textBorder = BorderFactory.createTitledBorder(textBorder, "Help information");
		
		//A help panel for each corresponding main panel.
		cytoSetupHelpPanel = new JPanel();
		antibodyDBHelpPanel = new JPanel();
		testsetHelpPanel = new JPanel();
		runCytoHelpPanel = new JPanel();
		resultsHelpPanel = new JPanel();
		
		//Each panel has a JTextArea
		cytoSetupText = new JTextArea("The Flow Cytometer Setup page allows the user to \nenter detectors found in their flow cytometer.\nEach detector is assigned an average wavelength that \nis allowed to pass through its filter. Then the \nconjugates are assigned to the detectors \nwhich are able to pick up their emission.");
		cytoSetupText.setBackground(SystemColor.window);
		cytoSetupText.setEditable(false);
		cytoSetupHelpPanel.setBorder( textBorder );
		cytoSetupHelpPanel.add( cytoSetupText );
		
		antibodyDBText = new JTextArea("The Antibody Database Setup page allows the \nuser to upload and edit a database of \nconjugated antibodies from which to create the Flow \nCytometer panel.");
		antibodyDBText.setBackground(SystemColor.window);
		antibodyDBText.setEditable(false);
		antibodyDBHelpPanel.setBorder( textBorder );
		antibodyDBHelpPanel.add( antibodyDBText );
		
		testsetText = new JTextArea("The Target Testset Setup page allows the user to \ncreate or load a testset of targets found in the \nAntibody Database. Each target is to have a cell \nsurface density estimate (1 for least and 10 for most \ndense). If desired, targets can be preassigned a \nspecific conjugate. Also, targets can subsetted into \ngroups, where the required group is to be added to \nevery panel inthe solution.");
		testsetText.setBackground(SystemColor.window);
		testsetText.setEditable(false);
		testsetHelpPanel.setBorder( textBorder );
		testsetHelpPanel.add( testsetText );
		
		runCytoText = new JTextArea("The Run GoCyto page allows the user to run the \nalgorithm to calculate the possible panels \nfor the given flow cytometer, antibody database, \nand testset. User is able to select and change \noptions using the pull down menu.");
		runCytoText.setBackground(SystemColor.window);
		runCytoText.setEditable(false);
		runCytoHelpPanel.setBorder( textBorder );
		runCytoHelpPanel.add( runCytoText );
		
		resultsText = new JTextArea("The Panel Solutions page allows the user to see \nthe panels that the algorithm calculated as \npossible solutions for the given flow cytometer, \nantibody database, and testset. The panels are \nordered with the most optimal panel(s) as listed \nfirst.");
		resultsText.setBackground(SystemColor.window);
		resultsText.setEditable(false);
		resultsHelpPanel.setBorder( textBorder );
		resultsHelpPanel.add( resultsText );
	}
	
	//The view methods allow for the JFrame to set specified panel to visible.
	public void viewCytoHelp() {
		this.setVisible(true);
		getContentPane().add(cytoSetupHelpPanel );
	}
	
	public void viewAntibodyDBHelp() {
		this.setVisible(true);
		getContentPane().add( antibodyDBHelpPanel );
	}
	
	public void viewTestsetHelp() {
		this.setVisible(true);
		getContentPane().add( testsetHelpPanel );
	}
	
	public void viewRunCytoHelp() {
		this.setVisible(true);
		getContentPane().add( runCytoHelpPanel );
	}
	
	public void viewResultsHelp() {
		this.setVisible(true);
		getContentPane().add( resultsHelpPanel );
	}

}
