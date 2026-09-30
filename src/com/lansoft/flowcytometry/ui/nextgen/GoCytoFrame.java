package com.lansoft.flowcytometry.ui.nextgen;
/**
 * This class is the JFrame that contains multiple panels 
 * for the GoCyto wizard.
 * @author jennychien
 */

import java.awt.EventQueue;
//import org.pushingpixels.substance.api.SubstanceLookAndFeel;
//import org.pushingpixels.substance.api.skin.BusinessSkin;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.*;

public class GoCytoFrame extends JFrame {

	//myPanel is the current panel that is added and visible
	//on the JFrame.  The other JPanel variables keeps track of
	//possible panels that JFrame may need to refer back to.

	private JPanel myPanel;
	private IntroPanel myIntroPanel;
	private CytoSetupPanel myCytoSetupPanel;
	private AntibodyDBPanel myAntibodyDBPanel;
	private TestsetPanel myTestsetPanel;
	private RunCytoPanel myRunCytoPanel;
	private ResultsPanel myResultsPanel;
	
	private String directory;
	private FlowCytometer myFlowCytometer;
	private static final long serialVersionUID = 1L;
	private AntibodyDB myAntibodyDB;
	private Testset myTestset;
	

	/**
	 * Main method launches the application. Creates a GoCytoFrame, and set to visible.
	 */
	public static void main(String[] args) {
		//try { 
		//   UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		//} catch (Exception e) {
		//    e.printStackTrace();
		//}

		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GoCytoFrame frame = new GoCytoFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * GoCytoFrame contructor creates a new IntroPanel, and uses setter
	 * methods to set private variables.
	 */
	public GoCytoFrame() {
		
		setResizable(false);
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setBounds(100, 100, 900, 566);
		
		myPanel = new IntroPanel( this );
		this.setMyIntroPanel( myPanel );
		this.setMyPanel( myPanel );

	}

	/**
	 * setMyPanel method takes the given JPanel and adds it to the JFrame
	 * @param p: JPanel that is to be added to the JFrame
	 */
	public void setMyPanel( JPanel p ) {
		this.myPanel = p;
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(28)
					.addComponent(myPanel, GroupLayout.PREFERRED_SIZE, 842, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(30, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(32, Short.MAX_VALUE)
					.addComponent(myPanel, GroupLayout.PREFERRED_SIZE, 506, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		getContentPane().setLayout(groupLayout);
	}
	
	public JPanel getMyPanel() {
		return myPanel;
	}

	public void removeMyPanel() {
		this.remove( myPanel);
	}
	
	public IntroPanel getMyIntroPanel() {
		return myIntroPanel;
	}

	public void setMyIntroPanel(JPanel p) {
		this.myIntroPanel = (IntroPanel) p;
	}

	public CytoSetupPanel getMyCytoSetupPanel() {
		return myCytoSetupPanel;
	}

	public void setMyCytoSetupPanel(CytoSetupPanel p) {
		this.myCytoSetupPanel = p;
	}

	public AntibodyDBPanel getMyAntibodyDBPanel() {
		return myAntibodyDBPanel;
	}

	public void setMyAntibodyDBPanel(AntibodyDBPanel p) {
		this.myAntibodyDBPanel = p;
	}
	
	public TestsetPanel getMyTestsetPanel () {
		return myTestsetPanel;
	}
	
	public void setMyTestsetPanel( TestsetPanel p ) {
		this.myTestsetPanel= p;
	}

	public RunCytoPanel getMyRunCytoPanel() {
		return myRunCytoPanel;
	}

	public void setMyRunCytoPanel(RunCytoPanel myRunCytoPanel) {
		this.myRunCytoPanel = myRunCytoPanel;
	}
	
	public ResultsPanel getMyResultsPanel() {
		return myResultsPanel;
	}
	
	public void setMyResultsPanel(ResultsPanel p) {
		this.myResultsPanel = p;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public FlowCytometer getFlowcytometer() {
		return myFlowCytometer;
	}

	public void setFlowcytometer(FlowCytometer flowcytometer) {
		this.myFlowCytometer = flowcytometer;
	}

	public Testset getMyTestset() {
		return myTestset;
	}

	public void setMyTestset(Testset myTestset) {
		this.myTestset = myTestset;
	}

	public AntibodyDB getMyAntibodyDB() {
		return myAntibodyDB;
	}

	public void setMyAntibodyDB(AntibodyDB myAntibodyDB) {
		this.myAntibodyDB = myAntibodyDB;
	}

}
