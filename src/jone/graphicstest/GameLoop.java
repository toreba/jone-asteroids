package jone.graphicstest;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.Random;
import java.util.function.Consumer;

/**
* Created by Tore on 02.07.2014.
*/
public class GameLoop implements Runnable {

    private final Font fontBoldLarge = new Font("Arial", Font.BOLD, 50);

    enum GameState {START,PLAYING,PAUSE,KILLED,GAME_OVER, QUIT}

    private static final int FRAME_DELAY = 16; // 20ms. implies 50fps (1000/20) = 50
    private static final int STARS = 2000;

    KeyboardInput keyboard = new KeyboardInput();
    GameState state;
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
    private double secondsBetweenAsteroids;
    private int lives;
    private int astroidsCreated;


    public GameLoop(Canvas canvas) {

        gui = canvas;
        gui.addKeyListener(keyboard);
        init();
    }

    private void init() {
        state = GameState.START;
        world = new World(gui.getWidth(), gui.getHeight());
        world.onAsteroidKilled(() -> score +=1000);
        world.onShipKilled(this::shipKilled);
        for(int i = 0; i < STARS; i++) {
            starX[i] = rnd.nextInt(world.width);
            starY[i] = rnd.nextInt(world.height);
            starBlue[i] = rnd.nextInt(255);
        }
        secondsBetweenAsteroids = 20;
        lives = 5;
        astroidsCreated = 0;
        score = 0;
        bulletsShot = 0;
    }


    private void start() {
        world.clear();
        world.update(0);
        ship = world.ship = new Ship(world);
        world.add(ship);
        secondsSinceLastAsteroid = 1000;
        world.add(new Enemy(world));
        state = GameState.PLAYING;
    }

    private void shipKilled() {
        lives = lives -1;
        if(lives == 0) {
            state = GameState.GAME_OVER;
        }
        else {
            state = GameState.KILLED;
        }
    }


    public void run() {
        gui.createBufferStrategy(2);
        BufferStrategy strategy = gui.getBufferStrategy();
        long lastTime = System.currentTimeMillis();

        // Game Loop
        while (state != GameState.QUIT) {
            long now = System.currentTimeMillis();
            long milliseconds = now - lastTime;
            lastTime = now;
            double deltaTime = milliseconds / 1000.0;
            switch(state) {
                case START:
                    showStartScreen();
                    break;
                case PLAYING:
                    play(deltaTime);
                    break;
                case PAUSE:
                    showPauseScreen();
                    break;
                case KILLED:
                    showOuchScreen();
                    break;
                case GAME_OVER:
                    showGameOverScreen();
                    break;
            }


            try {
                Thread.sleep(Math.max(0, FRAME_DELAY - (System.currentTimeMillis() - lastTime)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void showScreen(Consumer<Graphics2D> r) {
        BufferStrategy strategy = gui.getBufferStrategy();
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,  RenderingHints.VALUE_INTERPOLATION_BILINEAR); //VALUE_INTERPOLATION_BICUBIC

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, world.width, world.height);

        r.accept(g);

        g.dispose();
        strategy.show();
    }

    private void showPauseScreen() {
        showScreen(g -> {
            g.setColor(Color.GREEN);
            g.setFont(fontBoldLarge);
            drawCenteredText(g,  "PAUSE", 400);
            drawCenteredText(g,  "press space to continue", 500);
        });


        keyboard.poll();
        if(keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
            state = GameState.PLAYING;
        }
    }
    private void showOuchScreen() {
        showScreen(g -> {
            g.setColor(Color.GREEN);
            g.setFont(fontBoldLarge);
            drawCenteredText(g,  "OUCH!! :-()", 400);
            drawCenteredText(g,  "press space to continue", 500);
        });


        keyboard.poll();
        if(keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
            start();
        }
    }

    private void showGameOverScreen() {
        showScreen(g -> {
            g.setColor(Color.GREEN);
            g.setFont(fontBoldLarge);

            drawCenteredText(g,  "GAME OVER :-(", 400);
            drawCenteredText(g,  "press space to restart", 500);
        });

        keyboard.poll();
        if(keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
            init();
        }
    }

    private void showStartScreen() {
        showScreen(g -> {
            g.setColor(Color.GREEN);
            g.setFont(fontBoldLarge);

            drawCenteredText(g,  "ASTEROIDS :-)", 400);
            drawCenteredText(g,  "press space to start", 500);
        });


        keyboard.poll();
        if(keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
            start();

        }

    }

    private void drawCenteredText(Graphics2D g, String str, float y) {
        double width = g.getFontMetrics().getStringBounds(str,g).getWidth();
        g.drawString(str, (float)(world.width/2 - width/2), y);

    }

    private void play(double deltaTime) {

        handleInput();

        updateGameState(deltaTime);
        updateGUI();

    }
    //           \0
    //            |)
    //           /\
    private void updateGameState(double deltaTime) {
        world.collisionDetect();
        world.update(deltaTime);

        secondsSinceLastAsteroid += deltaTime;

        if(secondsSinceLastAsteroid > secondsBetweenAsteroids) {
            Vector2D dist = Vector2D.fromPolar(400, world.random(0, 2 * Math.PI));
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
            if(secondsBetweenAsteroids > 8) {
                secondsBetweenAsteroids = secondsBetweenAsteroids - 0.6;
            }
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
        if(keyboard.keyDownOnce(KeyEvent.VK_P)) {
            state = GameState.PAUSE;
        }
    }
    private void updateGUI() {
        showScreen(g ->{
            for(int i = 0; i < STARS; i++) {
                g.setColor(new Color(255,255,starBlue[i]));
                g.drawLine(starX[i],starY[i],starX[i],starY[i]);
            }

            world.render(g);

            drawHUD(g);
        });

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
