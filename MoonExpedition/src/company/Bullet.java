package company;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;

public class Bullet {
    // public static final int DEFAULT_Y=20;
    Area a;
    CollisionRect rect;
    AffineTransform at;
    public boolean remove=false;
    float x,y;
    double angleOfBullet;
    int radius;
    // for accessing the width and height of screen//
    GamePanel gp=new GamePanel();
    public Bullet(double angleOfBullet){
       // this.x=x;
      //  this.y=y;
        this.angleOfBullet=angleOfBullet;
        this.radius=gp.RADIUS;
        x= (float) (gp.screenWidth/2+radius*Math.cos(Math.toRadians(angleOfBullet-90)))-2; // 2 delete because of perfectly match with firing spot//
        y= (float) (gp.screenHeight/2+radius*Math.sin(Math.toRadians(angleOfBullet-90)))-3;
        this.rect=new CollisionRect(0,0,6,6);
        at = new AffineTransform();
        at.translate((int)x,(int) y);
        GeneralPath path2 = new GeneralPath();
        path2.append(rect.getPathIterator(at), true);
        a = new Area(path2);


    }
    public void update(){
        // if the coordinate of x and y both cross the difference of half of screen width of and height then remove
        // that bullet//
        if(Math.abs(radius-(int)x)>(gp.screenWidth/2)-4 && Math.abs(radius-(int)y)>(gp.screenHeight/2)-4){
            remove=true;
        }
        // System.out.println(Math.abs(radius-(int)x));
        radius-=(2);
        x= (float) ((gp.screenWidth/2)+100+radius*Math.cos(Math.toRadians(angleOfBullet-90)))-2; // 2 delete because of perfectly match with firing spot//
        y= (float) (gp.screenHeight/2+radius*Math.sin(Math.toRadians(angleOfBullet-90)))-3;
       // System.out.println(gp.screenHeight);
       // rect.move((int)x,(int)y);
       // this.rect=new CollisionRect(0,0,6,6);
        at = new AffineTransform();
        at.translate((int)x, (int)y);
        GeneralPath path2 = new GeneralPath();
        path2.append(rect.getPathIterator(at), true);
        a = new Area(path2);

    }
    public void ren(Graphics g){
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.red);
            g2d.fillOval((int) x, (int) y, 6, 6);

    }
    public Area getCollisionArea(){
        return a;
    }
}
