package company;
import java.awt.*;

public class Bullet {
    // public static final int DEFAULT_Y=20;
    public boolean remove=false;
    float x,y;
    double angleOfBullet;
    int radius;
    // for accessing the width and height of screen//
    GamePanel gp=new GamePanel();
    public Bullet(float x,float y,double angleOfBullet){
        this.x=x;
        this.y=y;
        this.angleOfBullet=angleOfBullet;
        this.radius=gp.RADIUS;
    }
    public void update(){
        // if the coordinate of x and y both cross the difference of half of screen width of and height then remove
        // that bullet//
        if(Math.abs(radius-(int)x)>(gp.screenWidth/2)-4 && Math.abs(radius-(int)y)>(gp.screenHeight/2)-4){
            remove=true;
        }
        // System.out.println(Math.abs(radius-(int)x));
        radius-=5;
        x= (float) (gp.screenWidth/2+radius*Math.cos(Math.toRadians(angleOfBullet-90)))-2; // 2 delete because of perfectly match with firing spot//
        y= (float) (gp.screenHeight/2+radius*Math.sin(Math.toRadians(angleOfBullet-90)))-3;
    }
    public void ren(Graphics g){
            //Graphics g=new Graphics();;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.red);
            g2d.fillOval((int) x, (int) y, 4, 4);


    }
}
