package company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MoonEater {
    final int widthOfMoonEater = 27, HeightOfMoonEater = 24;
    static int x;

    AffineTransform t1;
    Area a1;
    CollisionRect rect;
    BufferedImage moonEater = null;

    GamePanel gp= new GamePanel();

    public MoonEater() {
//        try {
//            moonEater = ImageIO.read(new File("src/assets/photos/moonEater.png"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        this.rect = new CollisionRect(0, 0, widthOfMoonEater, HeightOfMoonEater);

        t1 = new AffineTransform();
        t1.translate((gp.screenWidth / 2) + 100 - (rect.width / 2), (gp.screenHeight / 2) - rect.height - 96);
        t1.rotate(Math.toRadians(x), rect.width / 2, rect.height + 96);
        GeneralPath path1 = new GeneralPath();
        path1.append(rect.getPathIterator(t1), true);
        a1 = new Area(path1);
        getMoonEaterImage();


    }
    public void getMoonEaterImage(){
        try {
            moonEater = ImageIO.read(new File("src/assets/photos/moonEater.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void update(int i) {
        /**
         * Showing Moon eater in angle of x degree
         */
        this.x = i;
    }
    public void ren(Graphics2D g2d,GamePanel gp) {
        //MOON EATER COLLISION AREA CIRCULATE

        t1 = new AffineTransform();
        t1.translate((gp.screenWidth / 2) + 100 - (rect.width / 2), (gp.screenHeight / 2) - rect.height - 96);
        t1.rotate(Math.toRadians(x), rect.width / 2, rect.height + 96);
        GeneralPath path1 = new GeneralPath();
        path1.append(rect.getPathIterator(t1), true);
        a1 = new Area(path1);


       // Graphics2D g2d = (Graphics2D) g;
        AffineTransform t = AffineTransform.getTranslateInstance((gp.screenWidth / 2) + 100 - moonEater.getWidth() / 2, gp.screenHeight / 2 - moonEater.getHeight() - 92);
        t.rotate(Math.toRadians(x), moonEater.getWidth() / 2, moonEater.getHeight() + 92);
        g2d.drawImage(moonEater, t, null);


    }

    public Area getCollisionsArea() {
        return a1;
    }

}