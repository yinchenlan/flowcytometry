package com.lansoft.flowcytometry.ui.nextgen;
/**
 * This class creates a Results Panel which shows the optimum panel(s)
 * that the algorithm has calculated for the testset.
 */

import javax.swing.JPanel;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTable;
import javax.swing.JButton;

public class ResultsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GoCytoFrame f;
	private JTable table;
	public JButton btnSave,
				btnPrevious,
				btnHelp;
	
	/**
	 * Create the panel.
	 * @param frame: the GoCytoFrame that contains the Results Panel
	 */

	public ResultsPanel( GoCytoFrame frame ) {
		
		f = frame;
		ActionListener l = (ActionListener) new ResultsListener( this, f );
		
		JLabel lblResults = new JLabel("Panel Solutions");
		lblResults.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		
		JLabel lblSelectPanel = new JLabel("Select Panel Solution:");
		lblSelectPanel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		JComboBox comboBox = new JComboBox();
		
		table = new JTable();
		
		JButton btnNextPanel = new JButton("Next Panel");
		
		JButton btnPreviousPanel = new JButton("Previous Panel");
		
		btnSave = new JButton("Save");
		btnPrevious = new JButton("Previous");
		btnHelp = new JButton("Help");
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblResults)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblSelectPanel)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(27)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(btnPreviousPanel)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnNextPanel))
								.addComponent(table, GroupLayout.PREFERRED_SIZE, 778, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(38, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(536, Short.MAX_VALUE)
					.addComponent(btnHelp)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnPrevious)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSave)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblResults)
					.addGap(66)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSelectPanel)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(table, GroupLayout.PREFERRED_SIZE, 264, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNextPanel)
						.addComponent(btnPreviousPanel))
					.addPreferredGap(ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSave)
						.addComponent(btnPrevious)
						.addComponent(btnHelp))
					.addContainerGap())
		);
		setLayout(groupLayout);
			
		f = frame;
		
		//Add ActionListener to JComponents
		btnHelp.addActionListener(l);
		btnPrevious.addActionListener(l);
		btnSave.addActionListener(l);
	}
}
