package com.lansoft.flowcytometry.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.Font;
import java.beans.PropertyVetoException;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.JSlider;

import com.lansoft.flowcytometry.ui.brightness.BrightnessModel;
import com.lansoft.flowcytometry.ui.brightness.BrightnessValue;
import com.lansoft.flowcytometry.ui.slider.BrightnessEditor;
import com.lansoft.flowcytometry.ui.slider.BrightnessRenderer;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;


public class AppFrame extends JFrame {

	private JDesktopPane contentPane;
	private JTable table;
	private JTable table_1;
	private JTable table_2;
	private JTable table_3;
	private JTextField textField_1;
	private JTable table_4;
	private JTable table_5;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					AppFrame frame = new AppFrame();
					//frame.internalFrame.setIcon(true);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AppFrame() {
		setTitle("GoCyto");
		setIconImage(Toolkit.getDefaultToolkit().getImage(AppFrame.class.getResource("/fcIcon.gif")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 972, 627);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnNewMenu = new JMenu("Tutorial");
		menuBar.add(mnNewMenu);
		
		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);
		
		JMenu mnContact = new JMenu("Contact");
		menuBar.add(mnContact);
		
		JMenu mnFaqs = new JMenu("FAQs");
		menuBar.add(mnFaqs);
		contentPane = new JDesktopPane();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane);
		
		JDesktopPane desktopPane = new JDesktopPane();
		tabbedPane.addTab("Detector", null, desktopPane, null);
		
		JInternalFrame internalFrame_4 = new JInternalFrame("Fortessa Configuration");
		internalFrame_4.setMaximizable(true);
		internalFrame_4.setIconifiable(true);
		internalFrame_4.setBounds(64, 39, 738, 456);
		desktopPane.add(internalFrame_4);
		
		JPanel panel_8 = new JPanel();
		internalFrame_4.getContentPane().add(panel_8, BorderLayout.NORTH);
		panel_8.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_11 = new JPanel();
		panel_8.add(panel_11, BorderLayout.CENTER);
		panel_11.setLayout(new BorderLayout(0, 0));
		
		JList list_4 = new JList();
		list_4.setModel(new AbstractListModel() {
			String[] values = new String[] {"Detector 1", "Detector 2", "Detector 3"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list_4.setSelectedIndex(1);
		panel_11.add(list_4, BorderLayout.CENTER);
		
		JPanel panel_12 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_12.getLayout();
		flowLayout_2.setAlignment(FlowLayout.RIGHT);
		panel_11.add(panel_12, BorderLayout.SOUTH);
		
		JButton btnAdd = new JButton("Add");
		panel_12.add(btnAdd);
		
		JButton btnNewButton_4 = new JButton("Delete");
		panel_12.add(btnNewButton_4);
		
		JButton btnNewButton_5 = new JButton("Modify");
		panel_12.add(btnNewButton_5);
		
		JPanel panel_9 = new JPanel();
		internalFrame_4.getContentPane().add(panel_9, BorderLayout.CENTER);
		panel_9.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_13 = new JPanel();
		panel_9.add(panel_13, BorderLayout.CENTER);
		
		
		
		table_4 = new JTable();
		table_4.setColumnSelectionAllowed(true);
		table_4.setCellSelectionEnabled(true);
		table_4.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table_4.setModel(new DefaultTableModel(
			new Object[][] {
				{null},
				{null},
				{null},
			},
			new String[] {
				"Available"
			}
		));
		//panel_13.add(table_4);
		JScrollPane scrollPane_8 = new JScrollPane();
		panel_13.add(scrollPane_8);
		scrollPane_8.setViewportView(table_4);
		
		JPanel panel_14 = new JPanel();
		panel_13.add(panel_14);
		panel_14.setLayout(new GridLayout(2, 1, 0, 0));
		
		JButton button = new JButton("<-");
		panel_14.add(button);
		
		JButton btnNewButton_6 = new JButton("->");
		panel_14.add(btnNewButton_6);
		
		table_5 = new JTable();
		table_5.setModel(new DefaultTableModel(
			new Object[][] {
				{null},
				{null},
				{null},
			},
			new String[] {
				"Selected"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		panel_13.add(table_5);
		
		JPanel panel_15 = new JPanel();
		panel_9.add(panel_15, BorderLayout.SOUTH);
		
		JPanel panel_10 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_10.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		internalFrame_4.getContentPane().add(panel_10, BorderLayout.SOUTH);
		
		JButton btnNewButton_2 = new JButton("Save");
		panel_10.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Close");
		panel_10.add(btnNewButton_3);
		internalFrame_4.setVisible(true);
				
		//JPanel panel = new JPanel();
		JDesktopPane panel = new JDesktopPane();
		tabbedPane.addTab("MyFlowCytometer", null, panel, null);
		panel.setLayout(null);
		
		JInternalFrame internalFrame_1 = new JInternalFrame("New Flow Cytometer");
		
		internalFrame_1.setResizable(true);
		internalFrame_1.setMaximizable(true);
		internalFrame_1.setIconifiable(true);
		internalFrame_1.setBounds(6, 44, 716, 369);
		panel.add(internalFrame_1);
		internalFrame_1.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(87, 109, 271, 200);
		internalFrame_1.getContentPane().add(scrollPane);
		
		String column_names[]= {"Detector","Conjugate(s)"};
		TableModel table_model=new DefaultTableModel(column_names, 6);
		table = new JTable(table_model);
		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(406, 109, 271, 200);
		internalFrame_1.getContentPane().add(scrollPane_1);
		
		String column_names2[]= {"Conjugate","Brightness"};
		TableModel table_model2=new DefaultTableModel(column_names2, 6);
		table_1 = new JTable(table_model2);
		table_1.setFillsViewportHeight(true);
		scrollPane_1.setViewportView(table_1);
		
		JLabel lblFlowCytometerConfiguration = new JLabel("Flow Cytometer Configuration");
		lblFlowCytometerConfiguration.setBounds(296, 46, 183, 18);
		internalFrame_1.getContentPane().add(lblFlowCytometerConfiguration);
		
		internalFrame_1.setVisible(true);
		
		//JPanel panel_5 = new JPanel();
		JDesktopPane panel_5 = new JDesktopPane();
		tabbedPane.addTab("Conjugate Brightness", null, panel_5, null);
		panel_5.setLayout(null);
		
		Object[][] brightnessData = 
				new Object[][] {
				{"Alexa Fluor 350", new BrightnessValue(2)},
				{"BV450", new BrightnessValue(6)},
				{"DAPI", new BrightnessValue(8)}
		};
		// FIXME: model should be made a member variable for access
		AbstractTableModel brightnessModel = new BrightnessModel(brightnessData);
		
		JInternalFrame internalFrame_3 = new JInternalFrame("Ruth Configuration");
		internalFrame_3.setNormalBounds(new Rectangle(100, 50, 100, 50));
		internalFrame_3.setResizable(true);
		internalFrame_3.setMaximizable(true);
		internalFrame_3.setIconifiable(true);
		internalFrame_3.setBounds(27, 48, 691, 346);
		panel_5.add(internalFrame_3);
		internalFrame_3.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel_6 = new JPanel();
		internalFrame_3.getContentPane().add(panel_6, BorderLayout.NORTH);
		panel_6.setLayout(new GridLayout(0, 2, 0, 0));
		
		String[] databases = { "Chuck", "Ruth", "Jenny" };
		JComboBox comboBox = new JComboBox(databases);
		panel_6.add(comboBox);
		
		JButton btnNewButton = new JButton("Open Database");
		panel_6.add(btnNewButton);
		
		textField_1 = new JTextField();
		panel_6.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnFindConjugate = new JButton("Find Conjugate");
		panel_6.add(btnFindConjugate);
		
		JScrollPane scrollPane_7 = new JScrollPane();
		internalFrame_3.getContentPane().add(scrollPane_7, BorderLayout.CENTER);
		table_3 = new JTable(brightnessModel);
		table_3.setShowVerticalLines(true);
		table_3.setShowHorizontalLines(true);
		table_3.getColumn("Brightness").setCellRenderer(new BrightnessRenderer());
		table_3.getColumn("Brightness").setCellEditor(new BrightnessEditor());
		//table_3.setDefaultRenderer(BrightnessValue.class, new BrightnessRenderer());
		scrollPane_7.setViewportView(table_3);
		
		JPanel panel_7 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_7.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		internalFrame_3.getContentPane().add(panel_7, BorderLayout.SOUTH);
		
		JButton btnNewButton_1 = new JButton("Add");
		panel_7.add(btnNewButton_1);
		
		JButton btnDelete = new JButton("Delete");
		panel_7.add(btnDelete);
		
		JButton btnSave_1 = new JButton("Save");
		panel_7.add(btnSave_1);
		internalFrame_3.setVisible(true);
		
		//JPanel panel_1 = new JPanel();
		JDesktopPane panel_1 = new JDesktopPane();
		tabbedPane.addTab("Antibodies & Conjugates", null, panel_1, null);
		panel_1.setLayout(null);
		
		JInternalFrame internalFrame = new JInternalFrame("Target Conjugates");
		internalFrame.setIconifiable(true);
		internalFrame.setBounds(6, 6, 782, 428);
		panel_1.add(internalFrame);
		internalFrame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(6, 6, 748, 338);
		internalFrame.getContentPane().add(scrollPane_2);
		
		String column_names3[]= {"Conjugate","Target", "Target Species", "Company", "Catalog #", "Lot #", "Clone", "Isotype", "AUPS(ul)"};
		TableModel table_model3=new DefaultTableModel(column_names3, 6);
		table_2 = new JTable(table_model3);
		table_2.setShowVerticalLines(true);
		table_2.setShowHorizontalLines(true);
		table_2.setFillsViewportHeight(true);
		scrollPane_2.setViewportView(table_2);
		
		JButton btnImportExcel = new JButton("Import Excel");
		btnImportExcel.setBounds(435, 354, 101, 30);
		internalFrame.getContentPane().add(btnImportExcel);
		
		JButton btnImportCsv = new JButton("Import CSV");
		btnImportCsv.setBounds(548, 354, 101, 30);
		internalFrame.getContentPane().add(btnImportCsv);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(661, 354, 93, 30);
		internalFrame.getContentPane().add(btnSave);
		
		internalFrame.setVisible(true);
		
		JDesktopPane panel_2 = new JDesktopPane();
		tabbedPane.addTab("Target Test Sets", null, panel_2, null);
		panel_2.setLayout(null);
		
		JDesktopPane panel_3 = new JDesktopPane();
		tabbedPane.addTab("Run MyCytoGen", null, panel_3, null);
		panel_3.setLayout(null);
		
		JInternalFrame internalFrame_2 = new JInternalFrame("Run MyCytoGen");
		internalFrame_2.setBounds(0, 0, 758, 419);
		panel_3.add(internalFrame_2);
		internalFrame_2.getContentPane().setLayout(null);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(101, 43, 220, 139);
		internalFrame_2.getContentPane().add(scrollPane_3);
		
		JList list = new JList();
		scrollPane_3.setViewportView(list);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(431, 43, 220, 139);
		internalFrame_2.getContentPane().add(scrollPane_4);
		
		JList list_1 = new JList();
		scrollPane_4.setViewportView(list_1);
		
		JScrollPane scrollPane_5 = new JScrollPane();
		scrollPane_5.setBounds(101, 229, 220, 139);
		internalFrame_2.getContentPane().add(scrollPane_5);
		
		JList list_2 = new JList();
		scrollPane_5.setViewportView(list_2);
		
		JScrollPane scrollPane_6 = new JScrollPane();
		scrollPane_6.setBounds(431, 229, 220, 139);
		internalFrame_2.getContentPane().add(scrollPane_6);
		
		JList list_3 = new JList();
		scrollPane_6.setViewportView(list_3);
		
		JLabel lblStepChoose = new JLabel("Step 1: Choose Your Flow Cytometer");
		lblStepChoose.setBounds(101, 6, 220, 18);
		internalFrame_2.getContentPane().add(lblStepChoose);
		
		JLabel lblStepChoose_1 = new JLabel("Step 2: Choose Target & Conjugates Database");
		lblStepChoose_1.setBounds(101, 199, 280, 18);
		internalFrame_2.getContentPane().add(lblStepChoose_1);
		
		JLabel lblStepChoose_2 = new JLabel("Step 3: Choose Your Required Target Group");
		lblStepChoose_2.setBounds(431, 6, 273, 18);
		internalFrame_2.getContentPane().add(lblStepChoose_2);
		
		JLabel lblStepChoose_3 = new JLabel("Step 4: Choose Your Target Groups");
		lblStepChoose_3.setBounds(431, 199, 220, 18);
		internalFrame_2.getContentPane().add(lblStepChoose_3);
		internalFrame_2.setVisible(true);
		
		JDesktopPane panel_4 = new JDesktopPane();
		tabbedPane.addTab("Results", null, panel_4, null);
	}
}
