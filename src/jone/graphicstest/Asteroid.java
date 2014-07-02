package jone.graphicstest;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by Tore on 01.07.2014.
 */
public class Asteroid extends Sprite {
    private final double rotateSpeed;
    BufferedImage bufferedImage;
    int size;

    public Asteroid(World world, Vector2D pos, Vector2D velocity, double rotateSpeed, int size) {
        super(world, pos, velocity,size==1 ? 20 : size == 2 ? 40 : 80);
        this.size = size;
        this.rotateSpeed = rotateSpeed;
        bufferedImage = ResourceLoader.getImage("asteroid1_"+size+".png");
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        rotate +=rotateSpeed * 2 * Math.PI * deltaTime;
        wrap(80,80);
    }

    @Override
    public void render(Graphics2D g) {
        AffineTransform transform = g.getTransform();
        g.translate(pos.x,pos.y);
        g.rotate(-rotate);
        g.translate(-radius,-radius);
        g.drawImage(bufferedImage, 0, 0, null);
        g.setTransform(transform);

    }


    private BufferedImage toCompatibleImage(BufferedImage image)
    {
        // obtain the current system graphical settings
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();

	/*
	 * if image is already compatible and optimized for current system
	 * settings, simply return it
	 */
        if (image.getColorModel().equals(gfx_config.getColorModel()))
            return image;

        // image is not optimized, so create a new image that is
        BufferedImage new_image = gfx_config.createCompatibleImage(
                image.getWidth(), image.getHeight(), image.getTransparency());

        // get the graphics context of the new image to draw the old image on
        Graphics2D g2d = (Graphics2D) new_image.getGraphics();

        // actually draw the image and dispose of context no longer needed
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        // return the new optimized image
        return new_image;
    }

    @Override
    public void onCollision(Sprite other) {
        if(other instanceof Bullet) {
            if(size > 1) {
                double angle = velocity.angle() + Math.PI/4;
                for(int i = 0; i<4; i++) {
                    world.add(new Asteroid(world,
                            pos.plus(Vector2D.fromPolar(radius/2, angle)),
                            velocity.plus(Vector2D.fromPolar(radius,angle)),
                            world.random(-1, 1),
                            size - 1));
                    angle += Math.PI / 2;
                }
            }
            world.remove(this);
        }
    }
}

