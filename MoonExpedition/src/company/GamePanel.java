package company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.util.Map;

public class GamePanel extends JPanel implements Runnable{
    int FPS=60;
    double i=0;
    boolean rotate=false;
    final int screenWidth = 600;
    final int screenHeight = 600;
    Thread gameThread;
    public  GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
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
            lastTime=currentTime;
            if(delta>=1){
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if(timer>=1000000000){
                System.out.println("FPS : "+drawCount);
                drawCount=0;
                timer=0;
            }
        }
    }
    public void update(){
        if(rotate==false) {
            i += 0.7;
            if(i>=360){
                rotate=true;
            }
        }
        else if(rotate){
            i-=0.7;
            if(i<=-60){
                rotate=false;
            }
        }

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d=(Graphics2D)g;
        BufferedImage img=null;
        try {
            img= ImageIO.read(new File("src/assets/moon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        AffineTransform at = AffineTransform.getTranslateInstance((screenWidth/2)-(img.getWidth()/2),(screenHeight/2)-(img.getHeight()/2));
        at.rotate(Math.toRadians(i),img.getWidth()/2,img.getHeight()/2);
        g2d.drawImage(img,at,null);
        //g2d.fillOval(4,4, 40,40);


    }
}