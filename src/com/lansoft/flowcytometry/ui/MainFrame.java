package com.lansoft.flowcytometry.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JButton;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JDialog;

import com.alee.laf.WebLookAndFeel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Toolkit;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private AntibodiesConfiguration antibodiesConfiguration;
	private AntibodyDatabaseAndConfigurationDialog dbAndConfDialog;
	private AboutDialog aboutDialog;
	private JButton aboutButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					//WebLookAndFeel.install ();
					MainFrame frame = new MainFrame();
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
	public MainFrame() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/fcIcon.gif")));
		
		this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                MainFrame.this.setVisible(true);
            }
        });
		setResizable(false);
		setTitle("Antibody Panel Designer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 458, 293);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		antibodiesConfiguration = new AntibodiesConfiguration(this, "Antibodies Configurations");
		
		JButton btnNewButton_1 = new JButton("Antibody Database Setup");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				antibodiesConfiguration.setVisible(true);
			}
		});
		btnNewButton_1.setBounds(131, 87, 182, 23);
		contentPane.add(btnNewButton_1);
		
		dbAndConfDialog = new AntibodyDatabaseAndConfigurationDialog();
		
		JButton btnNewButton_2 = new JButton("Antibody Panel Design");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dbAndConfDialog.setVisible(true);
			}
		});
		btnNewButton_2.setBounds(131, 122, 182, 23);
		contentPane.add(btnNewButton_2);
		
		JLabel lblNewLabel = new JLabel("Main Menu");
		lblNewLabel.setBackground(UIManager.getColor("nimbusRed"));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(131, 46, 182, 16);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton_3 = new JButton("Exit");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainFrame.this.dispose();
				System.exit(0);
			}
		});
		btnNewButton_3.setBounds(356, 216, 90, 28);
		contentPane.add(btnNewButton_3);
		
		aboutDialog = new AboutDialog();
		
		aboutButton = new JButton("About");
		aboutButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				aboutDialog.setVisible(true);
			}
		});
		aboutButton.setBounds(254, 216, 90, 28);
		contentPane.add(aboutButton);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}
	
	public JButton getAboutButton() {
		return aboutButton;
	}
}
