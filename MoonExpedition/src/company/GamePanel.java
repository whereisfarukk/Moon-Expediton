package company;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.awt.geom.Area;


public class GamePanel extends JPanel implements Runnable {
    Ellipse2D.Double ellipse;
    Area area;
    Font pixelMplus;
    MoonEater ME;
    long timer;
    int FPS = 60, speed = 0, x = 0, y = 0, setDirX = 3, setDirY = 3;
    int score = 0;
    int playerLife = 3;
    final int playerMaxLife = 3;
    boolean rotate = false;
    final int screenWidth = 1000;
    final int screenHeight = 550;
    double i = 0, UFO_position_x, UFO_position_y, UFO_AngleChange = 0, delta = 0;
    public static final int RADIUS = 230;
    public static final float WAIT_SHOOT_TIME = 700;//700
    public static final float WAIT_MOON_EATER_SPAWN_TIME = 2500;
    public static final float MIN_ASTEROID_SPAWN_TIME = 2.0f;//2.0f
    public static final float MAX_ASTEROID_SPAWN_TIME = 3.0f;//3.0f
    float shootTimer, asteroidSpawnTimer, moonEaterSpawnTimer;
    Random random = new Random();
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
    BufferedImage FullHeart = null;
    BufferedImage BlankHeart = null;

    Sound sound = new Sound();

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        /**
         *  a asteroid spoon between the interval of min steroid spoon time
         * and maximum steroid spoon time
         */
        asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;

        shootTimer = 0;
        moonEaterSpawnTimer = 0;
        //score = 0;

    }

    public void startGameTread() {
        gameThread = new Thread(this);
        gameThread.start();

    }

    @Override
    public void run() {
        double drawnInterval = 1000000000 / FPS;
        delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        timer = 0;
        int drawCount = 0;
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawnInterval;
            timer += (currentTime - lastTime);

            /**
             *  converted nanosecond to millisecond
             */
            shootTimer += ((currentTime - lastTime) * 1.0E-6);
            moonEaterSpawnTimer += ((currentTime - lastTime) * 1.0E-6);

            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if (timer >= 1000000000) {
                // System.out.println("FPS : "+drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        x += setDirX;
        y += setDirY;

        speed += 3;
        if (keyH.rightKeyPressed) {
            UFO_AngleChange += (1.5);
            /**
             *  X=Xo+r*cos(theta)
             */
            UFO_position_x = (screenWidth / 2) + 100 * Math.cos(Math.toRadians(UFO_AngleChange - 90));
            /**
             *  Y=Yo+r*sin(theta)
             */
            UFO_position_y = (screenHeight / 2) + 100 * Math.sin(Math.toRadians(UFO_AngleChange - 90));
        }
        if (keyH.leftKeyPressed) {
            UFO_AngleChange -= (1.5);
            UFO_position_x = (screenWidth / 2) + 280 * Math.cos(Math.toRadians(UFO_AngleChange - 90));
            UFO_position_y = (screenHeight / 2) + 280 * Math.sin(Math.toRadians(UFO_AngleChange - 90));
        }


        if (asteroidSpawnTimer <= 0) {
            asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
            asteroids.add(new Asteroid(random.nextInt(screenHeight)));
        }
        asteroidSpawnTimer -= (timer * 4E-11);
        //System.out.println(timer*1E-11);
        //System.out.println(asteroidSpawnTimer);
        ArrayList<Asteroid> asteroidsToRemove = new ArrayList<Asteroid>();
        for (Asteroid asteroid : asteroids) {
            asteroid.update();
            if (asteroid.remove) {
                asteroidsToRemove.add(asteroid);
            }
        }

        if (keyH.spaceKeyPressed && WAIT_SHOOT_TIME <= shootTimer) {
            playSE(1);
            bullets.add(new Bullet(UFO_AngleChange));
            shootTimer = 0;
        }
        if (rotate == false) {
            i += 1.0;
            if (i >= 360) {
                rotate = true;
            }
        } else if (rotate) {
            i -= 1.1;
            if (i <= -60) {
                rotate = false;
            }
        }
        if (WAIT_MOON_EATER_SPAWN_TIME <= moonEaterSpawnTimer) {
            /**
             *  When the time is over ,moon eater appear randomly in any place
             */
            ME.update(random.nextInt(360));
            moonEaterSpawnTimer = 0;
        }
        ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
        for (Bullet bullet : bullets) {
            bullet.update();
            if (bullet.remove) {
                bulletsToRemove.add(bullet);
            }
        }

        for (Bullet bullet : bullets) {
            bullet.getCollisionArea().intersect(ME.getCollisionsArea());
            if (!bullet.getCollisionArea().isEmpty()) {
                playSE(2);
                bulletsToRemove.add(bullet);
                /**
                 * When the player able to shot moon eater ,it will disappear
                 * and spoon another place.
                 * And moonEaterSpawnTimer will set to 0 ,so that next spoon gets the same waiting
                 * time
                 */
                ME.update(random.nextInt(360));
                score += 10;
                moonEaterSpawnTimer = 0;
            }
        }
        bullets.removeAll(bulletsToRemove);

        ellipse = new Ellipse2D.Double(0, 0, 91, 22);
        AffineTransform t1 = new AffineTransform();
        t1.translate((screenWidth / 2) + 100 - (ellipse.width / 2), (screenHeight / 2) - ellipse.height - RADIUS - 8);
        t1.rotate(Math.toRadians(UFO_AngleChange), ellipse.width / 2, ellipse.height + RADIUS + 8);
        GeneralPath path1 = new GeneralPath();
        path1.append(ellipse.getPathIterator(t1), true);
        area = new Area(path1);
        for (Asteroid asteroid : asteroids) {
            asteroid.getCollisionArea().intersect(area);
            if (!asteroid.getCollisionArea().isEmpty()) {
                // stopMusic();
                playSE(3);
                asteroidsToRemove.add(asteroid);
                System.out.println("collied");
                playerLife--;
            }
        }
        asteroids.removeAll(asteroidsToRemove);


    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        BufferedImage moonEater = null;
        BufferedImage img = null;
        BufferedImage UFO_img = null;
        BufferedImage BackGround = null;
        BufferedImage Meteorite1 = null;
        try {
            img = ImageIO.read(new File("src/assets/photos/Moon.png"));
            UFO_img = ImageIO.read(new File("src/assets/photos/UFO.png"));
            //BackGround=ImageIO.read(new File("src/assets/satelite3.png"));
            BackGround = ImageIO.read(new File("src/assets/photos/Background.png"));
            //moonEater=ImageIO.read(new File("src/assets/moonEater.png"));
            FullHeart = ImageIO.read(new File("src/assets/photos/life/heart_full.png"));
            BlankHeart = ImageIO.read(new File("src/assets/photos/life/heart_blank.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            for (Bullet bullet : bullets) {
                bullet.ren(g);
            }
        } catch (Exception e) {
            System.out.println("this is in the gamePanel");
        }
        try {
            for (Asteroid asteroid : asteroids) {
                asteroid.ren(g);
                /**
                 *  VISUAL REPRESENTATION OF THE COLLISION AREA OF ASTEROID
                 */
//                g2d.setColor(Color.red);
//                g2d.fill(asteroid.a2);

            }
        } catch (Exception e) {
            System.out.println("this is in the gamePanel");
        }

        /**
         *  Code for Starry background
         */
        g2d.drawImage(BackGround, 0, 0, null);


        /**
         *  Moon Eater Rendering
         */
        ME = new MoonEater();
        g2d.setColor(Color.red);
        /**
         * Visual representation of Moon Eater collision area
         */
        //g2d.fill(ME.a1);
        ME.ren(g);


        /**
         * Moon Circulation Rendering
         */
        g2d.setColor(Color.white);
        //AffineTransform t2 = new AffineTransform();
        AffineTransform at = AffineTransform.getTranslateInstance((screenWidth / 2) + 100 - (img.getWidth() / 2), (screenHeight / 2) - (img.getHeight() / 2));
        at.rotate(Math.toRadians(i), img.getWidth() / 2, img.getHeight() / 2);
        g2d.drawImage(img, at, null);


        /**
         * Visual representation of UFO collision area
         */
        g2d.setColor(Color.red);
        //g2d.fill(area);


        /**
         * UFO circulation Rendering
         */
        AffineTransform t = AffineTransform.getTranslateInstance((screenWidth / 2) + 100 - UFO_img.getWidth() / 2, screenHeight / 2 - UFO_img.getHeight() - RADIUS);
        t.rotate(Math.toRadians(UFO_AngleChange), UFO_img.getWidth() / 2, UFO_img.getHeight() + RADIUS);
        g2d.drawImage(UFO_img, t, null);


        /**
         * Displaying 8-bit image for score
         */
        try {
            pixelMplus = Font.createFont(Font.TRUETYPE_FONT, new File("src/assets/fonts/PixelMplus10-Regular.ttf")).deriveFont(32f);
            g2d.setFont(pixelMplus);
            String SCORE = Integer.toString(score);
            g2d.setColor(Color.white);
            g2d.drawString("SCORE : " + SCORE, 26, 30);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //  g2d.drawImage(FullHeart,26,30,40,40,null);
        // g2d.drawImage(BlankHeart,0,0,null);
        drawPlayerLife(g, playerLife);

    }

    public void drawPlayerLife(Graphics g, int playerCurrentLife) {
        int w = 26, h = 30;
        int i = 0;
        while (i < playerMaxLife) {
            g.drawImage(BlankHeart, w, h, 40, 40, null);
            w += 40;
            i++;
        }
        w = 26;
        h = 30;
        i = 0;
        while (i < playerCurrentLife) {
            g.drawImage(FullHeart, w, h, 40, 40, null);
            w += 40;
            i++;
        }
    }

    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void stopMusic() {
        sound.stop();
    }

    public void playSE(int i) {
        sound.setFile(i);
        sound.play();
    }
}
