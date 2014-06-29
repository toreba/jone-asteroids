package jone.graphicstest;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferStrategy;
import java.util.Random;

/**
 * Created by Tore on 27.06.2014.
 */
public class Main {
    private static final int FRAME_DELAY = 20; // 20ms. implies 50fps (1000/20) = 50
    private static final int STARS = 1000;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Canvas gui = new Canvas();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(gui);
        frame.setSize(1000, 1000);
        frame.setVisible(true); // start AWT painting.
        Thread gameThread = new Thread(new GameLoop(gui));
        gameThread.setPriority(Thread.MIN_PRIORITY);
        gameThread.start(); // start Game processing.
    }

    private static class GameLoop implements Runnable {

        boolean isRunning;
        float x,y;
        float vx, vy;
        float rotate;

        int width;
        int height;
        Canvas gui;
        long cycleTime;
        int[] starX = new int[STARS];
        int[] starY = new int[STARS];
        Random rnd = new Random();

        public GameLoop(Canvas canvas) {
            gui = canvas;
            isRunning = true;
            width = gui.getWidth();
            height = gui.getHeight();
            x=100;
            y=100;
            vx = 1.5f;
            vy = 1.7f;


            for(int i = 0; i < STARS; i++) {
                starX[i] = rnd.nextInt(width);
                starY[i] = rnd.nextInt(height);
            }
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
            x = x + vx;
            y = y + vy;
            if(x <= 0 || x >= width) {
                vx = -vx;
            }
            if(y <= 0 || y >= height) {
                vy = -vy;
            }
            rotate = rotate +0.05f;
        }

        private void updateGUI(BufferStrategy strategy) {
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, gui.getWidth(), gui.getHeight());


            g.setColor(Color.WHITE);
            for(int i = 0; i < STARS; i++) {
                g.drawLine(starX[i],starY[i],starX[i],starY[i]);
            }



            //g.drawRect(x,y,3,3);

            Path2D.Float ship = new Path2D.Float();
            ship.moveTo(0, -8);
            ship.lineTo(5, 8);
            ship.lineTo(-5,8);
            ship.closePath();
            AffineTransform transform = g.getTransform();
            g.translate(x,y);
            g.rotate(rotate);
            g.setColor(Color.GREEN);
            g.fill(ship);
            g.setTransform(transform);

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