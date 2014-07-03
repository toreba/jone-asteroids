package jone.graphicstest;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by Tore on 01.07.2014.
 */
public class Enemy extends Sprite {
    BufferedImage bufferedImage;
    private double timeSinceLastShot = 0;

    public Enemy(World world) {
        super(world, new Vector2D(0,100), new Vector2D(30,0),30);
        this.world = world;
        bufferedImage = ResourceLoader.getImage("enemy.png");

    }


    public void update(double deltaTime) {
        super.update(deltaTime);
        wrap(0, 0);
        timeSinceLastShot =  timeSinceLastShot + deltaTime;
        if(timeSinceLastShot > 2) {
            shoot();
            timeSinceLastShot =0;
        }
    }



    public void render(Graphics2D g) {
        AffineTransform transform = g.getTransform();
        g.translate(pos.x,pos.y);
        g.rotate(-rotate);
        g.scale(1, -1);
        g.translate(-bufferedImage.getWidth()/2,-bufferedImage.getHeight()/2);
        g.drawImage(bufferedImage, 0, 0, null);
        g.setTransform(transform);

    }

    public void shoot() {
        double angle = world.ship.pos.minus(pos).angle();
        EnemyBullet bullet = new EnemyBullet(world, pos, velocity.plus(Vector2D.fromPolar(200, angle)));
        world.add(bullet);

    }

    @Override
    public void onCollision(Sprite other) {
        if(other instanceof Bullet)  {
            world.remove(this);
        }
    }
}
