package jone.graphicstest;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
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
        frame.setFocusable(true);
        Thread gameThread = new Thread(new GameLoop(gui));
        gameThread.setPriority(Thread.MAX_PRIORITY);
        gameThread.start(); // start Game processing.
    }

    private static class GameLoop implements Runnable {
        KeyboardInput keyboard = new KeyboardInput();
        boolean isRunning;
        World world;
        Canvas gui;
        long cycleTime;
        int[] starX = new int[STARS];
        int[] starY = new int[STARS];
        int[] starBlue = new int[STARS];
        Random rnd = new Random();
        Ship ship;


        public GameLoop(Canvas canvas) {

            gui = canvas;

            gui.addKeyListener(keyboard);
            gui.setFocusable(true);
            isRunning = true;
            world = new World(gui.getWidth(), gui.getHeight());
            ship = new Ship(world);
            world.add(ship);
            world.add(new Asteroid(world, new Vector2D(500,500), new Vector2D(0.11, 0.23),50));
            for(int i = 0; i < STARS; i++) {
                starX[i] = rnd.nextInt(world.width);
                starY[i] = rnd.nextInt(world.height);
                starBlue[i] = rnd.nextInt(255);
            }

        }

        public void run() {
            cycleTime = System.currentTimeMillis();
            gui.createBufferStrategy(2);
            BufferStrategy strategy = gui.getBufferStrategy();

            // Game Loop
            while (isRunning) {
                long difference;
                do {
                    handleInput();
                    updateGameState();
                    cycleTime = cycleTime + FRAME_DELAY;
                    difference = cycleTime - System.currentTimeMillis();
                } while(difference < 0);
                updateGUI(strategy);
                try {
                    Thread.sleep(Math.max(0, difference));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        //           \0/
        //            |
        //           /\
        private void updateGameState() {
            world.update();
        }

        private void handleInput() {
            keyboard.poll();
            if (keyboard.keyDown(KeyEvent.VK_LEFT)) {
                ship.rotateLeft();
            }
            if (keyboard.keyDown(KeyEvent.VK_RIGHT)) {
                ship.rotateRight();
            }
            if (keyboard.keyDown(KeyEvent.VK_UP)) {
                ship.forward();
            }

            if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
                ship.shoot();
            }
        }
        private void updateGUI(BufferStrategy strategy) {
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, gui.getWidth(), gui.getHeight());

            //g.translate(0,height/2);
            //g.scale(1,-1);
            //g.translate(0,-height/2);


            for(int i = 0; i < STARS; i++) {
                g.setColor(new Color(255,255,starBlue[i]));
                g.drawLine(starX[i],starY[i],starX[i],starY[i]);
            }

            world.render(g);

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