package jone.graphicstest;

/**
 * Created by Tore on 30.06.2014.
 */
public class Vector2D {
    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2D FromPolar(double length, double angle) {
        return new Vector2D(
            length*Math.sin(angle),
            length*Math.cos(angle));
    }


    public Vector2D plus(Vector2D vec) {
        return new Vector2D(x + vec.x, y + vec.y);
    }
}
