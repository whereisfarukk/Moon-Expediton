package company;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class CollisionRect extends Rectangle {

    public CollisionRect(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
