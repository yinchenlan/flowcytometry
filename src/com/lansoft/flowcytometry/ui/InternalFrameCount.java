package com.lansoft.flowcytometry.ui;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * @see http://stackoverflow.com/q/9414728/230513
 * @see http://stackoverflow.com/a/9415197/230513
 */
public class InternalFrameCount extends JFrame {

    private static final int SIZE = 300;
    private static final String ICONIFY = "Iconify";
    private static final String RESTORE = "Restore";
    private static final String COUNT = "Count:";
    private JDesktopPane desktop = new JDesktopPane();
    private JLabel count = new JLabel(COUNT);
    private List<JInternalFrame> list = new ArrayList<JInternalFrame>();

    public InternalFrameCount() {
        desktop.add(createInternalFrame(1, Color.RED));
        desktop.add(createInternalFrame(2, Color.GREEN));
        desktop.add(createInternalFrame(3, Color.BLUE));
        this.add(desktop);
        JPanel panel = new JPanel();
        panel.add(new JButton(new ButtonAction(ICONIFY)));
        panel.add(new JButton(new ButtonAction(RESTORE)));
        panel.add(count);
        this.add(panel, BorderLayout.SOUTH);
        count.setText(COUNT + desktop.getAllFrames().length);
    }

    private class ButtonAction extends AbstractAction {

        private boolean iconify;

        public ButtonAction(String name) {
            super(name);
            iconify = ICONIFY.equals(name);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            for (JInternalFrame jif : list) {
                try {
                    jif.setIcon(iconify);
                } catch (PropertyVetoException e) {
                    e.printStackTrace(System.err);
                }
            }
            count.setText(COUNT + desktop.getAllFrames().length);
        }
    }

    private JInternalFrame createInternalFrame(int number, Color background) {
        JInternalFrame jif = new JInternalFrame(
            "F" + number, true, true, true, true);
        list.add(jif);
        jif.setBackground(background);
        int topLeft = SIZE / 10 * number;
        jif.pack();
        jif.setBounds(topLeft, topLeft, SIZE / 2, SIZE / 2);
        jif.setVisible(true);
        return jif;
    }

    public static void main(String args[]) throws PropertyVetoException {
        System.out.println(System.getProperty("os.name"));
        System.out.println(System.getProperty("java.runtime.version"));
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                InternalFrameCount frame = new InternalFrameCount();
                frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                frame.pack();
                frame.setSize(SIZE, SIZE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
