package jone.graphicstest;

/**
 * Created by Tore on 30.06.2014.
 */
public class Vector2D {
    public static final Vector2D ZERO = new Vector2D(0,0);
    public final double x;
    public final double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2D fromPolar(double length, double angle) {
        return new Vector2D(
            length*Math.sin(angle),
            length*Math.cos(angle));
    }


    public Vector2D plus(Vector2D vec) {
        return new Vector2D(x + vec.x, y + vec.y);
    }
    public Vector2D minus(Vector2D vec) {
        return new Vector2D(x - vec.x, y - vec.y);
    }

    public double length() {
        return Math.sqrt(x*x+y*y);
    }

    public double angle() {
        if(x == 0 && y == 0) {
            return 0;
        }
        return Math.atan2(x, y);

    }

    public Vector2D mul(double scalar) {
        return new Vector2D(x * scalar, y * scalar);
    }
}
