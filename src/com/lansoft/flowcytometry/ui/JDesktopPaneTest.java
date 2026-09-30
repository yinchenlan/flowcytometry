package com.lansoft.flowcytometry.ui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JDesktopPaneTest extends JFrame {

     public static void main(String[] a){
          JDesktopPaneTest s = new JDesktopPaneTest();
     }
     
     JDesktopPaneTest() {
          setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          setTitle("JDesktopPaneTest");
          JDesktopPane dtp = new JDesktopPane();
          setLayeredPane(dtp);

     // menu
          JMenuBar mb_test = new JMenuBar();
          JMenu m_system = new JMenu("System");
          JMenuItem mi_quit = new JMenuItem("Quit");
          mi_quit.addActionListener(new ActionListener(){
               public void actionPerformed(ActionEvent evnt){
                    dispose();
               }
          });
          m_system.add(mi_quit);
          mb_test.add(m_system);
          setJMenuBar(mb_test);

     // first internal frame          
          JInternalFrame jif = new JInternalFrame("Thing 1", true, true, true, true);
          Container jifcp = jif.getContentPane();
          jifcp.setLayout(new FlowLayout());
          jifcp.add(new JButton("Yes"));
          jif.pack();
          jif.setVisible(true);
          dtp.add(jif,JLayeredPane.DEFAULT_LAYER);

     // second internal frame
          JTextArea ta = new JTextArea(4,20);
          JScrollPane sp = new JScrollPane(ta);
          JInternalFrame jif1 = new JInternalFrame("Thing 2", true, true, true, true);
          jif1.getContentPane().add(new JScrollPane(ta));
          jif1.pack();
          jif1.setVisible(true);
          dtp.add(jif1,JLayeredPane.DEFAULT_LAYER);

     // show frame
          setSize(new Dimension(640,480));
          setVisible(true);
     }
}
