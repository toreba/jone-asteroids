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
    Random random = new Random();
    Runnable asteroidKilled;


    public World(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void update(double deltaTime) {
        for(Sprite sprite: sprites) {
            sprite.update(deltaTime);
        }
        for(Sprite sprite : deadSprites) {
            sprites.remove(sprite);
        }
        deadSprites.clear();
    }

    public void render(Graphics2D g) {
        for(Sprite sprite: sprites) {
            sprite.render(g);
        }
    }
    public void add(Sprite sprite) {
        sprites.add(sprite);
    }

    public void remove(Sprite sprite) {
        deadSprites.add(sprite);
    }

    public void collisionDetect() {
        Sprite[] array = sprites.toArray(new Sprite[sprites.size()]);
        for(int i = 0; i<array.length; i++) {
            for(int j = i+1; j<array.length; j++) {
                if(array[i].quickCollidesWith(array[j])) {
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
}
