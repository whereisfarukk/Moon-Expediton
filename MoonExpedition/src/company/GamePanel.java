package company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GamePanel extends JPanel implements Runnable{
    long timer;
    int FPS=60,speed=0,x=0,y=0,setDirX=3,setDirY=3;
    boolean rotate=false;
    final int screenWidth = 700;
     final int screenHeight = 700;
    double i=0,UFO_position_x,UFO_position_y,UFO_AngleChange=0,delta=0;
    //public static final float  SHOOT_WAIT_TIME=0.3f;
    public static final int RADIUS=250;
    public static final float WAIT_SHOOT_TIME=300;
    public static final float MIN_ASTEROID_SPAWN_TIME=0.5f;
    public static final float MAX_ASTEROID_SPAWN_TIME=2.0f;
    float shootTimer,asteroidSpawnTimer;
    Random random=new Random();
    Thread gameThread;
    KeyHandler keyH=new KeyHandler();
    ArrayList<Bullet> bullets=new ArrayList<Bullet>();
    ArrayList<Asteroid> asteroids=new ArrayList<Asteroid>();
    public  GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        asteroidSpawnTimer=random.nextFloat()*(MAX_ASTEROID_SPAWN_TIME-MIN_ASTEROID_SPAWN_TIME)+MIN_ASTEROID_SPAWN_TIME;

        shootTimer=0;
    }
    public void startGameTread(){
        gameThread =new Thread(this);
        gameThread.start();

    }
    @Override
    public void run() {
        double drawnInterval=1000000000/FPS;
        delta=0;
        long lastTime=System.nanoTime();
        long currentTime;
        timer = 0;
        int drawCount = 0;
        while(gameThread!= null){
            currentTime = System.nanoTime();
            delta+=(currentTime-lastTime)/drawnInterval;
            timer+=(currentTime-lastTime);
           // shootTimer+= TimeUnit.NANOSECONDS.toMillis((currentTime-lastTime));
            //converted nanosecond to millisecond
            shootTimer+=((currentTime-lastTime)*1.0E-6);
          //  System.out.println(shootTimer);

          //  System.out.println(timer);
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

        speed+=3;
//        BackgroundOffset++;
//        if(BackgroundOffset%sc                                                                                                                   reenWidth==0){
//            BackgroundOffset=0;
//        }
        if(keyH.rightKeyPressed){
            UFO_AngleChange+=(1.5*delta);
            //X=Xo+r*cos(theta)
            UFO_position_x= (screenWidth/2)+200*Math.cos(Math.toRadians(UFO_AngleChange-90));
            //Y=Yo+r*sin(theta)
            UFO_position_y= (screenHeight/2)+200*Math.sin(Math.toRadians(UFO_AngleChange-90));
        }
        if(keyH.leftKeyPressed){
            UFO_AngleChange-=(1.5*delta);
            UFO_position_x= (screenWidth/2)+200*Math.cos(Math.toRadians(UFO_AngleChange-90));
            UFO_position_y= (screenHeight/2)+200*Math.sin(Math.toRadians(UFO_AngleChange-90));
        }


        if(asteroidSpawnTimer<=0){
            asteroidSpawnTimer=random.nextFloat()*(MAX_ASTEROID_SPAWN_TIME-MIN_ASTEROID_SPAWN_TIME)+MIN_ASTEROID_SPAWN_TIME;
            asteroids.add(new Asteroid(random.nextInt(screenWidth)));
        }
        asteroidSpawnTimer-=(timer*4E-11);
      //  System.out.println(timer*1E-11);
        //System.out.println(asteroidSpawnTimer);
        ArrayList<Asteroid>asteroidsToRemove=new ArrayList<Asteroid>();
        for(Asteroid asteroid : asteroids){
            asteroid.update(delta);
            if(asteroid.remove){
                asteroidsToRemove.add(asteroid);
            }
        }
        asteroids.removeAll(asteroidsToRemove);

        if(keyH.spaceKeyPressed && WAIT_SHOOT_TIME<=shootTimer){
            bullets.add(new Bullet(UFO_AngleChange));
            shootTimer=0;
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
                bullet.update(delta);
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
            BackGround=ImageIO.read(new File("src/assets/Starscape01.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            for (Bullet bullet : bullets) {
                bullet.ren(g);
            }
        }
        catch (Exception e){
            System.out.println("this is in the gamePanel");
        }
        try {
            for (Asteroid asteroid : asteroids) {
                asteroid.ren(g);
            }
        }
        catch (Exception e){
            System.out.println("this is in the gamePanel");
        }
        // code for moon circulation//
        g2d.drawImage(BackGround,0,0,null);

        g2d.setColor(Color.white);
        //AffineTransform t2 = new AffineTransform();
        AffineTransform at = AffineTransform.getTranslateInstance((screenWidth/2)-(img.getWidth()/2),(screenHeight/2)-(img.getHeight()/2));
        at.rotate(Math.toRadians(i),img.getWidth()/2,img.getHeight()/2);
        g2d.drawImage(img,at,null);

    //    g2d.drawImage(BackGround,x,y,null);


//        g2d.drawImage(Meteorite1,x,y,null);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //code for player UFO circulation//

        AffineTransform t = AffineTransform.getTranslateInstance(screenWidth/2 - UFO_img.getWidth()/2, screenHeight/2- UFO_img.getHeight()-RADIUS);
        t.rotate(Math.toRadians(UFO_AngleChange), UFO_img.getWidth()/2, UFO_img.getHeight()+RADIUS);
        g2d.drawImage(UFO_img,t,null);
        g2d.fillOval((int)UFO_position_x,(int)UFO_position_y,8,8);

    }
}
