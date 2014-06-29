package jone.graphicstest;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Created by Tore on 27.06.2014.
 */
public class Main {
    private static final int FRAME_DELAY = 20; // 20ms. implies 50fps (1000/20) = 50


    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Canvas gui = new Canvas();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(gui);
        frame.setSize(500, 500);
        frame.setVisible(true); // start AWT painting.
        Thread gameThread = new Thread(new GameLoop(gui));
        gameThread.setPriority(Thread.MIN_PRIORITY);
        gameThread.start(); // start Game processing.
    }

    private static class GameLoop implements Runnable {

        boolean isRunning;
        int lineX;
        Canvas gui;
        long cycleTime;

        public GameLoop(Canvas canvas) {
            gui = canvas;
            isRunning = true;
            lineX = 0;
        }

        public void run() {
            cycleTime = System.currentTimeMillis();
            gui.createBufferStrategy(2);
            BufferStrategy strategy = gui.getBufferStrategy();

            // Game Loop
            while (isRunning) {

                updateGameState();

                updateGUI(strategy);

                synchFramerate();
            }
        }

        private void updateGameState() {
            lineX++;
        }

        private void updateGUI(BufferStrategy strategy) {
            Graphics g = strategy.getDrawGraphics();

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, gui.getWidth(), gui.getHeight());
            g.setColor(Color.BLACK);

            for(int i = 0; i<500;i++) {
                for(int j = 0; j<500;j++) {
                    g.setColor(new Color(i%256,j%256,(i*j)%256));
                    g.drawLine(i, j-10, lineX +i, j); // arbitrary rendering logic
                }

            }
            g.drawLine(lineX, 0, lineX + 10, 0); // arbitrary rendering logic
            g.dispose();
            strategy.show();
        }

        private void synchFramerate() {
            cycleTime = cycleTime + FRAME_DELAY;
            long difference = cycleTime - System.currentTimeMillis();
            try {
                Thread.sleep(Math.max(0, difference));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
