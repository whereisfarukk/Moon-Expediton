package company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable{
    int FPS=60,speed=0,x=0,y=0,setDirX=3,setDirY=3;
    boolean rotate=false;
    final int screenWidth = 1000;
     final int screenHeight = 700;
    double i=0,UFO_position_x,UFO_position_y,UFO_AngleChange=0;
    public static final float  SHOOT_WAIT_TIME=0.3f;
    public static final int RADIUS=250;
    float shootTimer;
    Thread gameThread;
    KeyHandler keyH=new KeyHandler();
    ArrayList<Bullet> bullets=new ArrayList<Bullet>();
    public  GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        shootTimer=0;
    }
    public void startGameTread(){
        gameThread =new Thread(this);
        gameThread.start();

    }
    @Override
    public void run() {
        double drawnInterval=1000000000/FPS;
        double delta=0;
        long lastTime=System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        while(gameThread!= null){
            currentTime = System.nanoTime();
            delta+=(currentTime-lastTime)/drawnInterval;
            timer+=(currentTime-lastTime);
            lastTime = currentTime;
            if(delta>=1){
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if(timer>=1000000000){
               // System.out.println("FPS : "+drawCount);
                drawCount=0;
                timer=0;
            }
        }
    }
    public void update(){
        x+=setDirX;
        y+=setDirY;
        if(x>screenWidth){
            setDirX*=(-1);
        }
        if(x<0){
            setDirX*=(-1);
        }
        if(y<0){
            setDirY*=(-1);
        }
        if(y>screenHeight){
            setDirY*=(-1);
        }
        speed+=3;
//        BackgroundOffset++;
//        if(BackgroundOffset%sc                                                                                                                   reenWidth==0){
//            BackgroundOffset=0;
//        }
        if(keyH.rightKeyPressed){
            UFO_AngleChange+=2;
            //X=Xo+r*cos(theta)
            //Y=Yo+r*sin(theta)
            UFO_position_x= (screenWidth/2)+200*Math.cos(Math.toRadians(UFO_AngleChange-90));
            UFO_position_y= (screenHeight/2)+200*Math.sin(Math.toRadians(UFO_AngleChange-90));
        }
        if(keyH.leftKeyPressed){
            UFO_AngleChange-=2;
            UFO_position_x= (screenWidth/2)+200*Math.cos(Math.toRadians(UFO_AngleChange-90));
            UFO_position_y= (screenHeight/2)+200*Math.sin(Math.toRadians(UFO_AngleChange-90));
        }
        if(keyH.spacekeyPressed){

            bullets.add(new Bullet((float) 15,(float)15,UFO_AngleChange));
           // System.out.println(bullets.size());
        }
        if(rotate==false) {
            i += 1.0;
            if(i>=360){
                rotate=true;
            }
        }
        else if(rotate){
            i-=1.1;
            if(i<=-60){
                rotate=false;
            }
        }
        ArrayList<Bullet>bulletsToRemove=new ArrayList<Bullet>();
            for (Bullet bullet : bullets) {
                bullet.update();
                if (bullet.remove) {
                    bulletsToRemove.add(bullet);
                }
            }
            bullets.removeAll(bulletsToRemove);

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d=(Graphics2D)g;
        BufferedImage img=null;
        BufferedImage UFO_img=null;
        BufferedImage BackGround=null;
        BufferedImage Meteorite1=null;
        try {
            img= ImageIO.read(new File("src/assets/moon.png"));
            UFO_img=ImageIO.read(new File("src/assets/UFO.png"));
            BackGround=ImageIO.read(new File("src/assets/BackGround.png"));
            Meteorite1=ImageIO.read(new File("src/assets/meteorite1.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }


        g2d.setColor(Color.white);
        //AffineTransform t2 = new AffineTransform();
        AffineTransform at = AffineTransform.getTranslateInstance((screenWidth/2)-(img.getWidth()/2),(screenHeight/2)-(img.getHeight()/2));
        at.rotate(Math.toRadians(i),img.getWidth()/2,img.getHeight()/2);
        g2d.drawImage(img,at,null);

        g2d.drawImage(BackGround,x,y,null);

        g2d.drawImage(Meteorite1,x,y,null);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

        AffineTransform t = AffineTransform.getTranslateInstance(screenWidth/2 - UFO_img.getWidth()/2, screenHeight/2- UFO_img.getHeight()-RADIUS);
        t.rotate(Math.toRadians(UFO_AngleChange), UFO_img.getWidth()/2, UFO_img.getHeight()+RADIUS);
        g2d.drawImage(UFO_img,t,null);
        g2d.fillOval((int)UFO_position_x,(int)UFO_position_y,8,8);
        try {
            for (Bullet bullet : bullets) {
                bullet.ren(g);
            }
        }
        catch (Exception e){
            System.out.println("this is in the gamePanel");
        }

    }
}
