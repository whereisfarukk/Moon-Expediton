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
    final int widthOfMoonEater=27,HeightOfMoonEater=24;
    AffineTransform t1;
    Area a1;
    CollisionRect rect;
    BufferedImage moonEater=null;
    GamePanel gp=new GamePanel();
    static int  x;
    public MoonEater(){
        try {
            moonEater
                    =ImageIO.read(new File("src/assets/photos/moonEater.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.rect=new CollisionRect(0,0,widthOfMoonEater,HeightOfMoonEater);
       // Rectangle rect01 = new Rectangle(0, 0, 20, 20);

        t1 = new AffineTransform();

        //int center = screenWidth / 2;

        t1.translate((gp.screenWidth/2)+100-(rect.width/2),(gp.screenHeight/2)-rect.height-96 );
        t1.rotate(Math.toRadians(0), rect.width / 2, rect.height+96);
        GeneralPath path1 = new GeneralPath();
        path1.append(rect.getPathIterator(t1), true);
        a1 = new Area(path1);

        //g2d.fill(path1);
//
//        g2d.setColor(Color.BLUE);
//        g2d.draw(path1.getBounds());


    }
    public void update(int i){
        this.x=i;
    }
    public void ren(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
//        g2d.drawImage((Image) texture, (int) x, (int) y,null);
//
//        AffineTransform at = AffineTransform.getTranslateInstance(x,y);
//        at.rotate(Math.toRadians(i++),texture.getWidth(),texture.getHeight());
//        g2d.drawImage((Image)texture,at,null);
     //   g2d.drawImage(moonEater,30,30,null);\
        AffineTransform t = AffineTransform.getTranslateInstance((gp.screenWidth/2)+100 - moonEater.getWidth()/2, gp.screenHeight/2- moonEater.getHeight()-92);
        t.rotate(Math.toRadians(0), moonEater.getWidth()/2, moonEater.getHeight()+92);
        g2d.drawImage(moonEater,t,null);



    }
    public Area getCollisionsArea(){return a1;}
    public CollisionRect getCollisionRect(){
        return rect;
    }
}
