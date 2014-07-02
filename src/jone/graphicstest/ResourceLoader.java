package jone.graphicstest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tore on 02.07.2014.
 */
public class ResourceLoader {

    private static Map<String, BufferedImage> images = new HashMap<>();

    public static BufferedImage getImage(String name) {
        BufferedImage img = images.get(name);
        if(img == null) {
            try {
                BufferedImage image = ImageIO.read(ResourceLoader.class.getResource(name));
                img = toCompatibleImage(image);
                images.put(name,img);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return img;
    }

    private static BufferedImage toCompatibleImage(BufferedImage image)
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
