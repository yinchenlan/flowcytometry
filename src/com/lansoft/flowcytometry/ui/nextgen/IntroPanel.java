package com.lansoft.flowcytometry.ui.nextgen;
/**
 * This class is the JPanel for the first introduction panel.  
 * It allows the user to chose an already exhisting flow cytometer
 * detector setting or to setup a new set of detectors.
 * @author jennychien
 */

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.SystemColor;


public class IntroPanel extends JPanel {
	
	//Buttongs and text fields need to be public for the IntroListener
	//class to access.
	private static final long serialVersionUID = 1L;
	public JRadioButton radioSetup,
						radioLoad;
	public JButton introSaveButton, 
					introNextButton,
					introPrevButton,
					btnEnter;
	public JTextField nameField;
	private String name;
	private JTextArea textArea;
	public JComboBox<String> comboBox;
	public JTextField textField;

	/**
	 * Panel constructor takes the JFrame as an argument. This 
	 * constructure creates approprate text, radio buttons, and buttons
	 * for the intro page.
	 * @param f: JFrame that will be associated with the JPanel
	 */
	public IntroPanel( GoCytoFrame f) {
		
		//Create buttons, button group, text areas, and ActionListener.
		ButtonGroup radioGroup = new ButtonGroup();
		ActionListener l = (ActionListener) new IntroListener ( this, f );
		
		radioLoad = new JRadioButton("Load/Edit Flow Cytometer");
		radioSetup = new JRadioButton("Setup New Flow Cytometer:");
		
		JButton introPrevButton = new JButton("Previous");
		introPrevButton.setEnabled(false);
		introNextButton = new JButton("Next");
		introSaveButton = new JButton("Save");
		introSaveButton.setEnabled(false);
		btnEnter = new JButton("Enter");
		
		nameField = new JTextField();
		nameField.setEnabled(false);
		nameField.setEditable(false);
		nameField.setForeground(Color.GRAY);
		nameField.setText("(enter name)");
		nameField.setColumns(10);
		
		JLabel titleText = new JLabel("Welcome to GoCyto");
		titleText.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		
		textArea = new JTextArea();
		comboBox = new JComboBox<String>();
		
		JLabel lblPleaseEnter = new JLabel("1. Please enter directory for saving GoCyto files:");
		lblPleaseEnter.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setText("/Users/jennychien/Documents/GoCyto/QC/workspace/");
		
		JLabel lblNewLabel = new JLabel("2. Setup or Load Flow Cytometer:");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		//Layout of all the JComponents
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(titleText, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(702, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblPleaseEnter)
									.addGap(86)
									.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(6)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(btnEnter)
										.addComponent(textField, GroupLayout.PREFERRED_SIZE, 328, GroupLayout.PREFERRED_SIZE))))
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(radioLoad)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE))
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(radioSetup)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
									.addGap(284))
								.addGroup(groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblNewLabel)
									.addContainerGap())))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(introPrevButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(introNextButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(introSaveButton)
							.addGap(213))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(titleText)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(82)
							.addComponent(textArea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(90)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblPleaseEnter)
								.addComponent(lblNewLabel))))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnEnter))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(radioSetup)
								.addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(15)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(radioLoad))))
					.addGap(259)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(introSaveButton)
						.addComponent(introNextButton)
						.addComponent(introPrevButton))
					.addContainerGap())
		);
		setLayout(groupLayout);
		radioGroup.add(radioSetup);
		radioGroup.add(radioLoad);
		
		//Add actionListener to buttons and field.
		radioSetup.addActionListener(l);
		radioLoad.addActionListener(l);
		introNextButton.addActionListener(l);
		introSaveButton.addActionListener(l);
		btnEnter.addActionListener(l);
		
		nameField.addActionListener(l);
		textField.addActionListener(l);

	}
	
	public void setName (String n) {
		name = n;
	}
	
	public String getName ( ) {
		return name;
	}
}
