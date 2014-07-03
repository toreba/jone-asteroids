package jone.graphicstest;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Tore on 01.07.2014.
 */
public class World {
    int width;
    int height;
    Set<Sprite> sprites = new HashSet<Sprite>();
    List<Sprite> deadSprites = new ArrayList<Sprite>();
    List<Sprite> newSprites = new ArrayList<Sprite>();
    Random random = new Random();
    Runnable asteroidKilled;
    Runnable shipKilled;
    public Ship ship;


    public World(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void update(double deltaTime) {
        for (Sprite sprite : sprites) {
            sprite.update(deltaTime);
        }
        sprites.removeAll(deadSprites);
        sprites.addAll(newSprites);
        deadSprites.clear();
        newSprites.clear();
    }

    public void render(Graphics2D g) {
        for (Sprite sprite : sprites) {
            sprite.render(g);
        }
    }

    public void add(Sprite sprite) {
        newSprites.add(sprite);
    }

    public void remove(Sprite sprite) {
        deadSprites.add(sprite);
    }

    public void collisionDetect() {
        Sprite[] array = sprites.toArray(new Sprite[sprites.size()]);
        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[i].quickCollidesWith(array[j])) {
                    array[i].onCollision(array[j]);
                    array[j].onCollision(array[i]);
                }
            }
        }

    }

    public double random() {
        return random.nextDouble();
    }

    public double random(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }

    public void onAsteroidKilled(Runnable r) {
        asteroidKilled = r;
    }

    public void onShipKilled(Runnable r) {
        shipKilled = r;
    }

    public void clear() {
        deadSprites.clear();
        deadSprites.addAll(sprites);
        newSprites.clear();
    }
}


