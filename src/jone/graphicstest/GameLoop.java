package jone.graphicstest;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.Random;

/**
* Created by Tore on 02.07.2014.
*/
public class GameLoop implements Runnable {

    private static final int FRAME_DELAY = 16; // 20ms. implies 50fps (1000/20) = 50
    private static final int STARS = 2000;

    KeyboardInput keyboard = new KeyboardInput();
    boolean isRunning;
    World world;
    Canvas gui;
    int[] starX = new int[STARS];
    int[] starY = new int[STARS];
    int[] starBlue = new int[STARS];
    Random rnd = new Random();
    Ship ship;
    double secondsSinceLastAsteroid;
    private int score;
    private int bulletsShot;
    private double timwBetweenAsteroids = 20;
    private int lives = 5;
    private int astroidsCreated = 0;


    public GameLoop(Canvas canvas) {

        gui = canvas;
        gui.addKeyListener(keyboard);
        isRunning = true;
        world = new World(gui.getWidth(), gui.getHeight());
        world.onAsteroidKilled(() -> score +=1000);
        world.onShipKilled(this::shipKilled);
        start();
        for(int i = 0; i < STARS; i++) {
            starX[i] = rnd.nextInt(world.width);
            starY[i] = rnd.nextInt(world.height);
            starBlue[i] = rnd.nextInt(255);
        }

    }

    private void shipKilled() {
        lives = lives -1;
        start();
    }

    private void start() {
        world.clear();
        ship = world.ship = new Ship(world);
        world.add(ship);
        secondsSinceLastAsteroid = 1000;
        world.add(new Enemy(world));
    }

    public void run() {
        long lastTime = System.currentTimeMillis();
        gui.createBufferStrategy(2);
        BufferStrategy strategy = gui.getBufferStrategy();

        // Game Loop
        while (isRunning) {
            handleInput();

            long now = System.currentTimeMillis();
            long milliseconds = now - lastTime;
            lastTime = now;
            double deltaTime = milliseconds /1000.0;
            updateGameState(deltaTime);
            updateGUI(strategy);

            try {
                Thread.sleep(Math.max(0, FRAME_DELAY - milliseconds));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //           \0
    //            |)
    //           /\
    private void updateGameState(double deltaTime) {
        world.collisionDetect();
        world.update(deltaTime);

        secondsSinceLastAsteroid += deltaTime;

        if(secondsSinceLastAsteroid > timwBetweenAsteroids) {
            Vector2D dist = Vector2D.fromPolar(200, world.random(0, 2 * Math.PI));
            Vector2D newPos = ship.pos.plus(dist);
            int toughness = 1;
            if(astroidsCreated > 4 && world.random() > 0.6) {
                toughness = 3;
            }
            else if(astroidsCreated > 1 &&  world.random() > 0.4) {
                toughness = 2;
            }
            world.add(new Asteroid(world, newPos, dist.mul(world.random(-0.2, -0.02)), world.random(-0.5, 0.5), 3, toughness));
            secondsSinceLastAsteroid = 0;
            timwBetweenAsteroids = timwBetweenAsteroids -0.6;
            astroidsCreated = astroidsCreated + 1;
        }
    }

    private void handleInput() {
        keyboard.poll();
        if (keyboard.keyDown(KeyEvent.VK_LEFT)) {
            ship.rotateLeft(keyboard.keyDownOnce(KeyEvent.VK_LEFT));
        }
        if (keyboard.keyDown(KeyEvent.VK_RIGHT)) {
            ship.rotateRight(keyboard.keyDownOnce(KeyEvent.VK_RIGHT));
        }
        if (keyboard.keyDown(KeyEvent.VK_UP)) {
            ship.forward();
        }

        if (keyboard.keyDownOnce(KeyEvent.VK_SPACE) || keyboard.keyDownOnce(KeyEvent.VK_DOWN)) {
            ship.shoot();
            bulletsShot = bulletsShot + 1;
            score = Math.max(0, score -50);
        }
    }
    private void updateGUI(BufferStrategy strategy) {
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,  RenderingHints.VALUE_INTERPOLATION_BILINEAR); //VALUE_INTERPOLATION_BICUBIC

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, world.width, world.height);

        //g.translate(0,height/2);
        //g.scale(1,-1);
        //g.translate(0,-height/2);


        for(int i = 0; i < STARS; i++) {
            g.setColor(new Color(255,255,starBlue[i]));
            g.drawLine(starX[i],starY[i],starX[i],starY[i]);
        }

        world.render(g);

        drawHUD(g);

        g.dispose();
        strategy.show();
    }

    private void drawHUD(Graphics2D g) {
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, world.width, 20);
        g.setColor(Color.BLACK);
        g.drawString("SCORE: " + score, 50, 16);
        g.drawString("BULLETS SHOT: " + bulletsShot, 299, 16);
        g.drawString("LIVES: " + lives, 500, 16);
    }

}
