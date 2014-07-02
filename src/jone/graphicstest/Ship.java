package jone.graphicstest;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by Tore on 01.07.2014.
 */
public class Ship extends Sprite {
    BufferedImage bufferedImage;

    public Ship(World world) {
        super(world, new Vector2D(100,100), new Vector2D(0,0),10 );
        this.world = world;
        bufferedImage = ResourceLoader.getImage("16.png");

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
        g.translate(-17,-18);
        g.drawImage(bufferedImage, 0, 0, null);
        g.setTransform(transform);

    }

    public void rotateLeft() {
        rotate = rotate + 0.03f;
    }
    public void rotateRight() {
        rotate = rotate - 0.03f;
    }
    public void forward() {
        velocity = velocity.plus(Vector2D.fromPolar(3.6, rotate));
    }

    public void shoot() {
        Bullet bullet = new Bullet(world, pos, velocity.plus(Vector2D.fromPolar(900, rotate)));
        world.add(bullet);

    }

    @Override
    public void onCollision(Sprite other) {

    }
}
