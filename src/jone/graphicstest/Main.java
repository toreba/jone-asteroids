package jone.graphicstest;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by Tore on 27.06.2014.
 */
public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true); // start AWT painting.
        frame.setResizable(false);
        Canvas gui = new Canvas();
        frame.getContentPane().add(gui);
        gui.setFocusable(true);
        //frame.toFront();
        //frame.setFocusable(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                Thread gameThread = new Thread(new GameLoop(gui));
                gameThread.setPriority(Thread.MIN_PRIORITY);
                gameThread.start(); // start Game processing.

            }
        });
    }

}