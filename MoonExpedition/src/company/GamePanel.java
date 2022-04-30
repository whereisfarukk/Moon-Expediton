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
    boolean rotate=false;
    final int screenWidth = 700;
    final int screenHeight = 700;
    double i=0,UFO_position_x,UFO_position_y,UFO_AngleChange=0;

    Thread gameThread;
    KeyHandler keyH=new KeyHandler();
    public  GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
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
                System.out.println("FPS : "+drawCount);
                drawCount=0;
                timer=0;
            }
        }
    }
    public void update(){
        if(keyH.rightKeyPressed){
            UFO_AngleChange+=2;
            //X=Xo+r*cos(theta)
            //Y=Yo+r*sin(theta)
            UFO_position_x= (screenWidth/2)+200*Math.cos(Math.toRadians(UFO_AngleChange));
            UFO_position_y= (screenHeight/2)+200*Math.sin(Math.toRadians(UFO_AngleChange));
        }
        if(keyH.leftKeyPressed){
            UFO_AngleChange-=2;
            UFO_position_x= 100+20*Math.cos(Math.toRadians(UFO_AngleChange));
            UFO_position_y= 100+20*Math.sin(Math.toRadians(UFO_AngleChange));
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


    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d=(Graphics2D)g;
        BufferedImage img=null;
        BufferedImage UFO_img=null;
        try {
            img= ImageIO.read(new File("src/assets/moon.png"));
            UFO_img=ImageIO.read(new File("src/assets/UFO.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g2d.setColor(Color.white);
        // g2d.translate(screenWidth/2,screenHeight/2);
        //    g2d.fillOval(screenWidth/2-10,screenHeight/2-10,20,20);

        AffineTransform at = AffineTransform.getTranslateInstance((screenWidth/2)-(img.getWidth()/2),(screenHeight/2)-(img.getHeight()/2));
        at.rotate(Math.toRadians(i),img.getWidth()/2,img.getHeight()/2);

        g2d.drawImage(img,at,null);
        g2d.setColor(Color.white);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //System.out.println(UFO_img.getWidth()+" "+UFO_img.getHeight());

        //g2d.drawImage(UFO_img, 0, 0, this);  // Display with top-left corner at (0, 0)

        // drawImage() does not use the current transform of the Graphics2D context
        // Need to create a AffineTransform and pass into drawImage()
        AffineTransform transform = new AffineTransform();  // identity transform
        // Display the image with its center at the initial (x, y)
        g2d.translate(screenWidth/2 - UFO_img.getWidth()/2, screenHeight/2- UFO_img.getHeight()-150);
        //  g2d.drawImage(UFO_img, transform, this);
        g2d.setColor(Color.white);
        // g2d.fillOval(0+UFO_img.getWidth()/2,0+UFO_img.getHeight()+104 ,20,20);

        transform.rotate(Math.toRadians(UFO_AngleChange), UFO_img.getWidth()/2, UFO_img.getHeight()+150); // about its center

        //transform.scale(0.9, 0.9);
        g2d.drawImage(UFO_img, transform, this);
        // g2d.setTransform(transform);


    }
}