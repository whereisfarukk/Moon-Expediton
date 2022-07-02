package company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    GamePanel gp;
    KeyHandler KeyH;
    BufferedImage UFO_img = null;

    int x;
    int y;
    public Player(GamePanel gp) {
        this.gp=gp;
        this.x=gp.setDirX;
        this.y=gp.setDirY;
        getPlayerImage();

    }
    public void setDefault(){
        gp.i=0;
        gp.score=0;
        gp.playerLife=gp.playerMaxLife;
        gp.UFO_AngleChange=0;
        gp.bullets.clear();
        gp.asteroids.clear();
    }
    public void getPlayerImage(){
        try {
            UFO_img = ImageIO.read(new File("src/assets/photos/UFO.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void update(){
        x += 4;
        y += 4;

        gp.speed += 3;
        if (gp.keyH.rightKeyPressed) {
            gp.UFO_AngleChange += (1.5);
            /**
             *  X=Xo+r*cos(theta)
             */
            gp.UFO_position_x = (gp.screenWidth / 2) + 100 * Math.cos(Math.toRadians(gp.UFO_AngleChange - 90));
            /**
             *  Y=Yo+r*sin(theta)
             */
            gp.UFO_position_y = (gp.screenHeight / 2) + 100 * Math.sin(Math.toRadians(gp.UFO_AngleChange - 90));
        }
        if (gp.keyH.leftKeyPressed) {
            gp.UFO_AngleChange -= (1.5);
            gp.UFO_position_x = (gp.screenWidth / 2) + 280 * Math.cos(Math.toRadians(gp.UFO_AngleChange - 90));
            gp.UFO_position_y = (gp.screenHeight / 2) + 280 * Math.sin(Math.toRadians(gp.UFO_AngleChange - 90));
        }

    }
    public void draw(Graphics2D g2d){
        AffineTransform t = AffineTransform.getTranslateInstance((gp.screenWidth / 2) + 100 - UFO_img.getWidth() / 2, gp.screenHeight / 2 - UFO_img.getHeight() - gp.RADIUS);
        t.rotate(Math.toRadians(gp.UFO_AngleChange), UFO_img.getWidth() / 2, UFO_img.getHeight() + gp.RADIUS);
        g2d.drawImage(UFO_img, t, null);

    }
}
