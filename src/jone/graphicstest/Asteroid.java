package jone.graphicstest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Tore on 01.07.2014.
 */
public class Asteroid extends Sprite {
    double radius;
    BufferedImage bufferedImage;

    public Asteroid(World world, Vector2D pos, Vector2D velocity, double radius) {
        super(world, pos, velocity);
        this.radius = radius;
        try {
            BufferedImage image = ImageIO.read(this.getClass().getResource("asteroid1_1.png"));
            bufferedImage = toCompatibleImage(image);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        super.update();
        rotate += 0.01;
        wrap(80,80);
    }

    @Override
    public void render(Graphics2D g) {
        AffineTransform transform = g.getTransform();
        g.translate(pos.x,pos.y);
        g.rotate(-rotate);
        g.translate(-80,-80);
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

}

