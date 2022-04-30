package company;
import java.awt.*;

public class Bullet {
    public static final int DEFAULT_Y=20;
    public boolean remove=false;
    float x,y;
    public Bullet(float x){
        this.x=x;
        this.y=DEFAULT_Y;
    }
    public void update(){
        if(y-5>=700){
            remove=true;
        }
        y+=5;
    }
    public void ren(Graphics g){
//      Grpahics g=new Graphics();;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.green);
            g2d.fillOval((int) x, (int) y, 2, 2);


    }
}
