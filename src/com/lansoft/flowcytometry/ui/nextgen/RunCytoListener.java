package com.lansoft.flowcytometry.ui.nextgen;
/**
 * This class creates an action listener for the RunCytoPanel class.
 * @author jennychien
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RunCytoListener implements ActionListener{
	
	private RunCytoPanel p;
	private GoCytoFrame f;
	private ResultsPanel nextp;
	private HelpFrame h;
	
	/**
	 * This constructor takes a panel and a frame
	 * @param panel: RunCytoPanel that this listener is listening to.
	 * @param frame: GoCytoFrame that contains the RunCytoPanel.
	 */
	public RunCytoListener( RunCytoPanel panel, GoCytoFrame frame ) {
		p = panel;
		f = frame;
	}

	//Override actionPerformed to determine the logic of buttons and text components.
	public void actionPerformed(ActionEvent e) {
		
		Object source = e.getSource();
		
		//If btnPrevious is pushed, the frame will set its panel to its TestsetPanel.
		if ( source == p.btnPrevious ) {
			f.removeMyPanel();
			f.setMyPanel( f.getMyTestsetPanel());
		}
		
		//If btnNext is pushed, the frame will set its panel to its ResultsPanel.
		if ( source == p.btnNext ) {
			f.removeMyPanel();
			nextp = new ResultsPanel ( f );
			f.setMyPanel( nextp );
			f.setMyResultsPanel( nextp );
		}
		
		//btnHelp will create a new HelpFrame, and will add the testsetHelp panel to frame.
		if ( source == p.btnHelp ) {
			h = new HelpFrame();
			h.viewRunCytoHelp();
		}
		
		
	}
	
}
