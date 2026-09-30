package com.lansoft.flowcytometry.ui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import javax.swing.*;
import javax.swing.plaf.basic.*;

public class InternalFrameUnMovable extends JFrame {

    private static final long serialVersionUID = 1L;
    public JDesktopPane desktop;

    public InternalFrameUnMovable() {
        desktop = new JDesktopPane();
        getContentPane().add(desktop);
        desktop.add(createInternalFrame(1, Color.RED));
        desktop.add(createInternalFrame(2, Color.GREEN));
        desktop.add(createInternalFrame(3, Color.BLUE));
    }

    private JInternalFrame createInternalFrame(int number, Color background) {
        JInternalFrame internal = new JInternalFrame("Frame" + number, true, true, true, true);
        internal.setBackground(background);
        internal.setVisible(true);
        int location = 50 * number;
        internal.setBounds(location, location, 300, 300);
        return internal;
    }

    public static void main(String args[]) throws PropertyVetoException {
        InternalFrameUnMovable frame = new InternalFrameUnMovable();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        try {// Activate first internal frame
            JInternalFrame[] frames = frame.desktop.getAllFrames();
            frames[0].setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
        JInternalFrame[] frames = frame.desktop.getAllFrames();// Make first internal frame unmovable
        for (int i = 0; i < frames.length; i++) {
            JInternalFrame f = frames[i];
            if (f.isIconifiable()) {
                f.setIcon(true);
            }
        }
        /*JInternalFrame f = frames[0];
        BasicInternalFrameUI ui = (BasicInternalFrameUI) f.getUI();
        Component north = ui.getNorthPane();
        //MouseMotionListener[] actions = (MouseMotionListener[]) north.getListeners(MouseMotionListener.class);
        MouseMotionListener[] actions = north.getListeners(MouseMotionListener.class);
        for (int i = 0; i < actions.length; i++) {
        north.removeMouseMotionListener(actions[i]);
        }*/
    }
}
