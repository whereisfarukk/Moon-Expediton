package company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Asteroid {
    AffineTransform t2;
    CollisionRect rect;
    Area a2;
    public boolean remove = false;
    float x, y;
    int choose, i = 0;
    public static final int SPEED = 1;
    //private static TexturePaint texture;
    BufferedImage texture = null;
    Random random = new Random();
    GamePanel gp = new GamePanel();

    public Asteroid(float y) {
        this.y = y;
        choose = random.nextInt(5);
        if (texture == null) {
            try {
                if (choose == 0) {
                    texture = ImageIO.read(new File("src/assets/photos/meteorite1.png"));
                } else if (choose == 1) {
                    texture = ImageIO.read(new File("src/assets/photos/asteroid2.png"));
                } else if (choose == 2) {
                    texture = ImageIO.read(new File("src/assets/photos/asteroid3.png"));
                } else if (choose == 3) {
                    texture = ImageIO.read(new File("src/assets/photos/satelite.png"));
                } else if (choose == 4) {
                    texture = ImageIO.read(new File("src/assets/photos/satelite2.png"));
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        /**
         * Deducting texture height,so that asteroid does not spoon on out of the screen
         */
        if (y + texture.getHeight() > gp.screenHeight) {
            this.y = y - texture.getHeight();
        }
        /**
         * initially the asteroid spoon before the screen width
         */
        this.x = -texture.getWidth();

        this.rect = new CollisionRect(0, 0, texture.getWidth(), texture.getHeight());

        t2 = new AffineTransform();

        //int center = screenWidth / 2;
        AffineTransform at = AffineTransform.getTranslateInstance((int) x, (int) y);


        // t2.translate((int)x,(int)this.y);
        //t1.rotate(Math.toRadians(0), rect.width / 2, rect.height+96);
        GeneralPath path3 = new GeneralPath();
        path3.append(rect.getPathIterator(at), true);
        a2 = new Area(path3);

    }

    public void update() {
        if (choose == 0) {
            x += SPEED;
        } else if (choose == 1) {
            x += SPEED;
        } else if (choose == 2) {
            x += SPEED;
        } else if (choose == 3) {
            x += SPEED;
        } else if (choose == 4) {
            x += SPEED;
        }
        if (x > gp.screenWidth) {
            remove = true;
        }

        t2 = new AffineTransform();

        AffineTransform at = AffineTransform.getTranslateInstance((int) x, (int) y);

        GeneralPath path3 = new GeneralPath();
        path3.append(rect.getPathIterator(at), true);
        a2 = new Area(path3);
    }

    public void ren(Graphics2D g2d) {
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        i += 3;
        at.rotate(Math.toRadians(i), texture.getWidth() / 2, texture.getHeight() / 2);
        g2d.drawImage((Image) texture, at, null);

    }

    public Area getCollisionArea() {
        return a2;
    }


}
