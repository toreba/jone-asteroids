package jone.graphicstest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by Tore on 01.07.2014.
 */
public class Ship extends Sprite {
    BufferedImage bufferedImage;

    public Ship(World world) {
        super(world, new Vector2D(100,100), new Vector2D(0,0) );
        this.world = world;
        try {
            BufferedImage image = ImageIO.read(this.getClass().getResource("16.png"));
            bufferedImage = toCompatibleImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }


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

    public void update() {
        super.update();
        if(velocity.length() > 14.0001) {
            velocity = Vector2D.fromPolar(14, velocity.angle());
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
        velocity = velocity.plus(Vector2D.fromPolar(0.06, rotate));
    }

    public void shoot() {
        Bullet bullet = new Bullet(world, pos, velocity.plus(Vector2D.fromPolar(15, rotate)));
        world.add(bullet);

    }
}
