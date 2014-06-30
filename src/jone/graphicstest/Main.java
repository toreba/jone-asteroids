package jone.graphicstest;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferStrategy;
import java.util.Random;

/**
 * Created by Tore on 27.06.2014.
 */
public class Main {
    private static final int FRAME_DELAY = 16; // 20ms. implies 50fps (1000/20) = 50
    private static final int STARS = 2000;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Canvas gui = new Canvas();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(gui);
        frame.setSize(1000, 1000);
        frame.setVisible(true); // start AWT painting.
        frame.toFront();
        Thread gameThread = new Thread(new GameLoop(gui));
        gameThread.setPriority(Thread.MIN_PRIORITY);
        gameThread.start(); // start Game processing.
    }

    private static class GameLoop implements Runnable {
        KeyboardInput keyboard = new KeyboardInput();
        boolean isRunning;
        Vector2D pos;
        Vector2D velocity;
        double rotate;

        int width;
        int height;
        Canvas gui;
        long cycleTime;
        int[] starX = new int[STARS];
        int[] starY = new int[STARS];
        int[] starBlue = new int[STARS];
        Random rnd = new Random();

        public GameLoop(Canvas canvas) {
            gui = canvas;

            gui.addKeyListener(keyboard);
            gui.setFocusable(true);
            isRunning = true;
            width = gui.getWidth();
            height = gui.getHeight();
            pos = new Vector2D(100,100);
            velocity = new Vector2D(0,0);

            for(int i = 0; i < STARS; i++) {
                starX[i] = rnd.nextInt(width);
                starY[i] = rnd.nextInt(height);
                starBlue[i] = rnd.nextInt(255);

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
            pos = pos.plus(velocity);
            if(pos.x <= 0) {
                pos.x = width + pos.x;
            }
            if(pos.x >= width) {
                pos.x = pos.x - width;
            }
            if(pos.y <= 0) {
                pos.y = pos.y + height;

            }
            if(pos.y >= height) {
                pos.y = pos.y - height;
            }
            keyboard.poll();
            if(keyboard.keyDown(KeyEvent.VK_LEFT)) {
                rotate = rotate - 0.02f;
            }
            if(keyboard.keyDown(KeyEvent.VK_RIGHT)) {
                rotate = rotate + 0.02f;
            }
            if(keyboard.keyDown(KeyEvent.VK_UP)) {
                velocity = velocity.plus(Vector2D.FromPolar(0.2, rotate));
            }

        }

        private void updateGUI(BufferStrategy strategy) {
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, gui.getWidth(), gui.getHeight());

            g.translate(0,height/2);
            g.scale(1,-1);
            g.translate(0,-height/2);


            for(int i = 0; i < STARS; i++) {
                g.setColor(new Color(255,255,starBlue[i]));
                g.drawLine(starX[i],starY[i],starX[i],starY[i]);
            }



            //g.drawRect(x,y,3,3);

            Path2D.Double ship = new Path2D.Double();
            ship.moveTo(0, 8);
            ship.lineTo(5, -8);
            ship.lineTo(-5, -8);
            ship.closePath();
            AffineTransform transform = g.getTransform();
            g.translate(pos.x,pos.y);
            g.rotate(-rotate);
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