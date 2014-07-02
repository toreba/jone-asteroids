package jone.graphicstest;

import java.awt.*;

/**
 * Created by Tore on 01.07.2014.
 */
public class Bullet extends Sprite {

    public Bullet(World world, Vector2D pos, Vector2D velocity) {
        super(world, pos, velocity,1);
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        if(pos.x < 0 || pos.x >= world.width || pos.y < 0 || pos.y >= world.height) {
            world.remove(this);
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillRect((int) pos.x - 4, (int) pos.y - 4, 8, 8);
    }

    @Override
    public void onCollision(Sprite other) {
        //world.remove(this);
    }
}
