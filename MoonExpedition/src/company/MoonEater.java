package company;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class MoonEater {
    CollisionRect rect;
    BufferedImage moonEater=null;
    GamePanel gp=new GamePanel();
    public MoonEater(){
        try {
            moonEater=ImageIO.read(new File("src/assets/moonEater.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.rect=new CollisionRect(333,253,40,40);

    }
    public void update(double delta){

    }
    public void ren(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
//        g2d.drawImage((Image) texture, (int) x, (int) y,null);
//
//        AffineTransform at = AffineTransform.getTranslateInstance(x,y);
//        at.rotate(Math.toRadians(i++),texture.getWidth(),texture.getHeight());
//        g2d.drawImage((Image)texture,at,null);
     //   g2d.drawImage(moonEater,30,30,null);\
        AffineTransform t = AffineTransform.getTranslateInstance(gp.screenWidth/2 - moonEater.getWidth()/2, gp.screenHeight/2- moonEater.getHeight()-97);
        t.rotate(Math.toRadians(0), moonEater.getWidth()/2, moonEater.getHeight()+97);
        g2d.drawImage(moonEater,t,null);

    }
    public CollisionRect getCollisionRect(){
        return rect;
    }
}
