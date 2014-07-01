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


    public World(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void update() {
        for(Sprite sprite: sprites) {
            sprite.update();
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
}
