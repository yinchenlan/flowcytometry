package com.lansoft.flowcytometry.ui.nextgen;
/**
 * This class is the action listener for the ResultsPanel class.
 * @author jennychien
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class ResultsListener implements ActionListener {
	
	private ResultsPanel p;
	private GoCytoFrame f;
	private HelpFrame h;
	
	public ResultsListener ( ResultsPanel panel, GoCytoFrame frame ) {
		p = panel;
		f = frame;
	}
	
	public void actionPerformed(ActionEvent e) {
		
		Object source = e.getSource();
		
		if ( source == p.btnPrevious ) {
			f.removeMyPanel();
			f.setMyPanel( f.getMyRunCytoPanel() );
		}
		
		if ( source == p.btnHelp ) {
			h = new HelpFrame();
			h.viewResultsHelp();
		}
	
	}
	

}
