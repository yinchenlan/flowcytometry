package com.lansoft.flowcytometry.ui.nextgen;
/**
 * This class creates a Run GoCyto panel.  This allows
 * the user to run the algorithm for calculating the optimal
 * panel(s) with the given input.
 * @author jennychien
 */

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;

public class RunCytoPanel extends JPanel {

	//Variable needed for the ActionListener are made public
	private GoCytoFrame f;
	public JButton btnSave,
					btnNext,
					btnPrevious,
					btnHelp;
	
	/**
	 * Create the panel. 
	 * @param frame: the GoCytoFrame that contains this panel
	 */
	public RunCytoPanel( GoCytoFrame frame ) {
		
		//Create ActionListener for panel
		f = frame;
		ActionListener l = (ActionListener) new RunCytoListener( this, f );
		
		JLabel lblRun = new JLabel("Run GoCyto");
		lblRun.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		
		JLabel lblChosenDatabase = new JLabel("1. Flow cytometer:");
		lblChosenDatabase.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		JComboBox comboBox = new JComboBox();
		
		JLabel lblAntibodyDatabase = new JLabel("2. Conjugated antibody database:");
		lblAntibodyDatabase.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		JComboBox comboBox_1 = new JComboBox();
		
		JLabel lblNewLabel = new JLabel("3. Target testset:");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		JComboBox comboBox_2 = new JComboBox();
		
		btnSave = new JButton("Save");
		btnNext = new JButton("Run");
		btnPrevious = new JButton("Previous");
		btnHelp = new JButton("Help");
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblRun))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addContainerGap()
									.addComponent(lblChosenDatabase))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(17)
									.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(20)
									.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addContainerGap()
									.addComponent(lblAntibodyDatabase)))
							.addGap(150)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(6)
									.addComponent(comboBox_2, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblNewLabel))))
					.addContainerGap(298, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(457, Short.MAX_VALUE)
					.addComponent(btnHelp)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnPrevious)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNext)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSave)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblRun)
					.addGap(94)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblChosenDatabase)
						.addComponent(lblNewLabel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBox_2, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
					.addGap(38)
					.addComponent(lblAntibodyDatabase)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 159, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSave)
						.addComponent(btnNext)
						.addComponent(btnPrevious)
						.addComponent(btnHelp))
					.addContainerGap())
		);
		setLayout(groupLayout);
		
		//Add ActionListener to the JButtons
		btnSave.addActionListener(l);
		btnNext.addActionListener(l);
		btnPrevious.addActionListener(l);
		btnHelp.addActionListener(l);

	}
}
