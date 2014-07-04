package jone.graphicstest;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by Tore on 01.07.2014.
 */
public class Ship extends Sprite {
    BufferedImage bufferedImage;
    private double rotateSpeed;

    public Ship(World world) {
        super(world, new Vector2D(world.width/2,world.height/2), new Vector2D(0,0),10 );
        this.world = world;
        bufferedImage = ResourceLoader.getImage("ship.png");

    }


    public void update(double deltaTime) {
        super.update(deltaTime);
        if(velocity.length() > 801) {
            velocity = Vector2D.fromPolar(800, velocity.angle());
        }
        wrap(0, 0);
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

    public void rotateLeft(boolean first) {
        setRotateSpeed(first);
        rotate = rotate + rotateSpeed;
    }
    public void rotateRight(boolean first) {
        setRotateSpeed(first);
        rotate = rotate - rotateSpeed;
    }

    private void setRotateSpeed(boolean first) {
        if(first) {
            rotateSpeed = 0.02;
        }
        rotateSpeed = Math.min(0.2,rotateSpeed + 0.002);


    }


    public void forward() {
        velocity = velocity.plus(Vector2D.fromPolar(7, rotate));
    }

    public void shoot() {
        Bullet bullet = new Bullet(world, pos, velocity.plus(Vector2D.fromPolar(900, rotate)));
        world.add(bullet);

    }

    @Override
    public void onCollision(Sprite other) {
        if(other instanceof Asteroid || other instanceof  EnemyBullet)  {
            world.shipKilled.run();
        }
    }
}
