package jone.graphicstest;

import java.awt.*;

/**
 * Created by Tore on 01.07.2014.
 */
public abstract class Sprite {
    Vector2D pos;
    Vector2D velocity;
    double rotate;
    World world;

    public Sprite(World world, Vector2D pos, Vector2D velocity) {
        this.world = world;
        this.pos = pos;
        this.velocity = velocity;
    }

    public void update() {
        pos = pos.plus(velocity);
    }

    protected void wrap(double dx, double dy) {
        if(pos.x < -dx) {
            pos = new Vector2D(pos.x + world.width + 2 * dx, pos.y);
        }
        if(pos.x >= world.width + dx) {
            pos = new Vector2D(pos.x - world.width - 2 * dx, pos.y);
        }
        if(pos.y < -dy) {
            pos = new Vector2D(pos.x, pos.y + world.height + 2 * dy);
        }
        if(pos.y >= world.height + dy) {
            pos = new Vector2D(pos.x, pos.y - world.height - 2 * dy);
        }
    }

    public abstract void render(Graphics2D g);
}
