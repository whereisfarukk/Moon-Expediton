package company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Asteroid {
    public boolean remove=false;
    float x,y;
    public static final int SPEED=1;
    public static final int WIDTH=16;
    //private static TexturePaint texture;
    BufferedImage texture=null;
    int choose=0,i=0;
    Random random=new Random();
    GamePanel gp=new GamePanel();
    public Asteroid(float y){
        this.y=y;
          choose=random.nextInt(5);
        // System.out.println(choose);
          if(texture==null){
              try {
                  if(choose==0) {
                      texture = ImageIO.read(new File("src/assets/meteorite1.png"));
                  }
                  else if(choose==1){
                      texture = ImageIO.read(new File("src/assets/asteroid2.png"));
                  }
                  else if(choose==2){
                      texture = ImageIO.read(new File("src/assets/asteroid3.png"));
                  }
                  else if(choose==3){
                      texture = ImageIO.read(new File("src/assets/satelite.png"));
                  }
                  else if(choose==4){
                      texture = ImageIO.read(new File("src/assets/satelite2.png"));
                  }

              } catch (IOException e) {
                  throw new RuntimeException(e);
              }

          }
          if(y+texture.getHeight()> gp.screenHeight) {
              this.y = y - texture.getHeight();// so that asteroid does not spoon on out of the screen//
          }
        this.x=-texture.getWidth(); //initially the asteroid spoon before the screen width//
    }
    public void update(double delta){
        if(choose==0){
            x+=SPEED+3;
        }
        else if(choose==1){
            x+=SPEED+2;
        }
        else if(choose==2){
            x+=SPEED;
        }

        else if(choose==3){
            x+=SPEED+4;
        }

        else if(choose==4){
            x+=SPEED+1;
        }
       // y+=SPEED;
        if(x> gp.screenWidth){
            remove=true;
        }
    }
    public void ren(Graphics g){
        //Graphics g=new Graphics();;
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage((Image) texture, (int) x, (int) y,null);
//
//        AffineTransform at = AffineTransform.getTranslateInstance(x,y);
//        at.rotate(Math.toRadians(i++),texture.getWidth(),texture.getHeight());
//        g2d.drawImage((Image)texture,at,null);


    }
}
