package company;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
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
    public static final float WAIT_MOON_EATER_SPAWN_TIME = 3200;
    public static final float MIN_ASTEROID_SPAWN_TIME = 2.0f;//2.0f
    public static final float MAX_ASTEROID_SPAWN_TIME = 3.0f;//3.0f
    public int gameState;
    public int gameOverCommand = 0;
    public int gameTitleCommand = 0;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int gameOverState = 3;
    public final int titleHighScoreState=1;
    public final int titleAboutState=2;
    public final int titleQuitState=3;
    public  int selectedTitleCommand=-1;



    float shootTimer, asteroidSpawnTimer, moonEaterSpawnTimer;
    static int spoonAngleOfME;
    Random random = new Random();
    Player player = new Player(this);

    KeyHandler keyH = new KeyHandler(this, player);
    Thread gameThread;
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
    BufferedImage FullHeart = null;
    BufferedImage BlankHeart = null;
    BufferedImage img = null;

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

    public void setupGame() throws IOException {

        ME = new MoonEater();

        img = ImageIO.read(new File("src/assets/photos/Moon.png"));
        FullHeart = ImageIO.read(new File("src/assets/photos/life/heart_full.png"));
        BlankHeart = ImageIO.read(new File("src/assets/photos/life/heart_blank.png"));
        gameState = titleState;

        playMusic(4);
        //System.out.println(1);
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
        if (gameState == playState) {
            player.update();

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
                i += 0.3;
                if (i >= 360) {
                    rotate = true;
                }
            } else if (rotate) {
                i -= 0.5;
                if (i <= -60) {
                    rotate = false;
                }
            }
            if (WAIT_MOON_EATER_SPAWN_TIME <= moonEaterSpawnTimer) {
                /**
                 *  When the time is over ,moon eater appear randomly in any place
                 */
                spoonAngleOfME = random.nextInt(360);
                ME.update(spoonAngleOfME);
                moonEaterSpawnTimer = 0;
            } else {
                /**
                 * Moon eater rotate along with the moon.
                 * ME will rotate a head start with spooning angle of ME
                 */
                ME.update((int) i + spoonAngleOfME);
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
                    spoonAngleOfME = random.nextInt(360);
                    ME.update(spoonAngleOfME);
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
                    if (playerLife < 0) {
                        gameState = gameOverState;
                    }
                }
            }
            asteroids.removeAll(asteroidsToRemove);
        } else if (gameState == pauseState) {

        }


    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (gameState== titleState) {
            if(selectedTitleCommand==-1) {
                drawTitleScreen(g2d);
            }
            else if(selectedTitleCommand==titleHighScoreState){
                drawHighScoreScreen(g2d);
            }
            else if(selectedTitleCommand==titleAboutState){
                drawAboutScreen(g2d);
            }
        } else if(gameState==playState){

            drawMainGameScreen(g2d);
        }
        else if (gameState == pauseState) {
            drawMainGameScreen(g2d);
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, this.screenWidth, this.screenHeight);

        }

    }

    public void drawMainGameScreen(Graphics2D g2d) {
        BufferedImage BackGround = null;
        try {
            BackGround = ImageIO.read(new File("src/assets/photos/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            for (Bullet bullet : bullets) {
                bullet.ren(g2d);
            }
        } catch (Exception e) {
            System.out.println("this is in the gamePanel");
        }
        try {
            for (Asteroid asteroid : asteroids) {
                asteroid.ren(g2d);
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
        // ME = new MoonEater();
        g2d.setColor(Color.red);
        /**
         * Visual representation of Moon Eater collision area
         */
        //g2d.fill(ME.a1);
        ME.ren(g2d, this);


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
        player.draw(g2d);


        /**
         * Displaying 8-bit image for score
         */
        drawScoreScreen(g2d);
        /**
         * Displaying player life
         */
        drawPlayerLife(g2d, playerLife);

        /**
         * Displaying Game over screen on top of main game screen
         */
        if (gameState == gameOverState) {
            drawGameOverScreen(g2d);
        }
    }

    public void drawScoreScreen(Graphics2D g2d) {
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
    }

    public void drawGameOverScreen(Graphics2D g2d) {
        // Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, this.screenWidth, this.screenHeight);

        try {
            pixelMplus = Font.createFont(Font.TRUETYPE_FONT, new File("src/assets/fonts/PixelMplus10-Regular.ttf")).deriveFont(90f);
            g2d.setFont(pixelMplus);
            String Text = "GAME OVER";
            int w = g2d.getFontMetrics().stringWidth(Text);
            // int h = g.getFontMetrics().stringHeight(Text);
            g2d.setColor(new Color(27, 30, 35));
            g2d.drawString(Text, this.screenWidth / 2 - w / 2, this.screenHeight / 2 - 90);
            g2d.setColor(Color.white);
            g2d.drawString(Text, this.screenWidth / 2 - w / 2 - 4, this.screenHeight / 2 - 90 - 4);


            pixelMplus = Font.createFont(Font.TRUETYPE_FONT, new File("src/assets/fonts/PixelMplus10-Regular.ttf")).deriveFont(45f);
            g2d.setFont(pixelMplus);
            Text = "Retry";
            w = g2d.getFontMetrics().stringWidth(Text);
            g2d.setColor(new Color(27, 30, 35));
            g2d.drawString(Text, this.screenWidth / 2 - w / 2, this.screenHeight / 2 - 30);
            g2d.setColor(Color.white);
            g2d.drawString(Text, this.screenWidth / 2 - w / 2 - 4, this.screenHeight / 2 - 30 - 4);
            if (gameOverCommand == 0) {
                g2d.drawString(">", this.screenWidth / 2 - w / 2 - 40, this.screenHeight / 2 - 30);
            }


            pixelMplus = Font.createFont(Font.TRUETYPE_FONT, new File("src/assets/fonts/PixelMplus10-Regular.ttf")).deriveFont(45f);
            g2d.setFont(pixelMplus);
            Text = "Quit";
            //    w = g.getFontMetrics().stringWidth(Text);
            g2d.setColor(new Color(27, 30, 35));
            g2d.drawString(Text, this.screenWidth / 2 - w / 2, this.screenHeight / 2 + 20);
            g2d.setColor(Color.white);
            g2d.drawString(Text, this.screenWidth / 2 - w / 2 - 4, this.screenHeight / 2 + 20 - 4);
            if (gameOverCommand == 1) {
                g2d.drawString(">", this.screenWidth / 2 - w / 2 - 40, this.screenHeight / 2 + 20);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void drawTitleScreen(Graphics2D g2d) {
        try {
            pixelMplus = Font.createFont(Font.TRUETYPE_FONT, new File("src/assets/fonts/PixelMplus10-Regular.ttf")).deriveFont(95f);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        g2d.setFont(pixelMplus);
        String Text = "UUFO";
        int w = g2d.getFontMetrics().stringWidth(Text);

        int width = this.screenWidth / 2 - w / 2;
        int height = this.screenHeight / 2 - 100;
        g2d.setColor(new Color(27, 30, 35));
        g2d.setColor(Color.orange);
        g2d.drawString(Text, width, height);
        g2d.setColor(Color.red);
        g2d.drawString(Text, width - 4, height - 4);

        //NEW GAME
        g2d.setFont(g2d.getFont().deriveFont(Font.TRUETYPE_FONT, 30f));
        if (gameTitleCommand == 0) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 35f));
        }
        Text = "NEW GAME";
        width = screenWidth / 2 - g2d.getFontMetrics().stringWidth(Text) / 2;
        height = height + 80;
        g2d.setColor(new Color(27, 30, 35));
        g2d.drawString(Text, width, height);
        g2d.setColor(Color.white);
        g2d.drawString(Text, width - 4, height - 4);


        //HIGH SCORE

        g2d.setFont(g2d.getFont().deriveFont(Font.TRUETYPE_FONT, 30f));
        if (gameTitleCommand == 1) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 35f));
        }
        Text = "HIGH SCORE";
        width = screenWidth / 2 - g2d.getFontMetrics().stringWidth(Text) / 2;
        height = height + 35;
        g2d.setColor(new Color(27, 30, 35));
        g2d.drawString(Text, width, height);
        g2d.setColor(Color.white);
        g2d.drawString(Text, width - 4, height - 4);

        //ABOUT
        g2d.setFont(g2d.getFont().deriveFont(Font.TRUETYPE_FONT, 30f));
        if (gameTitleCommand == 2) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 35f));
        }
        Text = "ABOUT";
        width = screenWidth / 2 - g2d.getFontMetrics().stringWidth(Text) / 2;
        height = height + 35;
        g2d.setColor(new Color(27, 30, 35));
        g2d.drawString(Text, width, height);
        g2d.setColor(Color.white);
        g2d.drawString(Text, width - 4, height - 4);

        //QUIT

        g2d.setFont(g2d.getFont().deriveFont(Font.TRUETYPE_FONT, 30f));
        if (gameTitleCommand == 3) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 35f));
        }
        Text = "QUIT";
        width = screenWidth / 2 - g2d.getFontMetrics().stringWidth(Text) / 2;
        height = height + 35;
        g2d.setColor(new Color(27, 30, 35));
        g2d.drawString(Text, width, height);
        g2d.setColor(Color.white);
        g2d.drawString(Text, width - 4, height - 4);


    }
    public void drawHighScoreScreen(Graphics2D g2d){
        drawBackCursor(g2d);

    }
    public void drawAboutScreen(Graphics2D g2d){
        drawBackCursor(g2d);


    }
    public void drawBackCursor(Graphics2D g2d){
        g2d.setColor(new Color(0, 0, 0,150));
        g2d.fillRect(0, 0, this.screenWidth, this.screenHeight);
        String Text = "⬅";
        int width =30 ;
        int height = 530;
        g2d.setColor(Color.orange);
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 60f));
        g2d.drawString(Text, width, height);

    }
    public void drawPlayerLife(Graphics2D g2d, int playerCurrentLife) {
        int w = 26, h = 30;
        int i = 0;
        while (i < playerMaxLife) {
            g2d.drawImage(BlankHeart, w, h, 40, 40, null);
            w += 40;
            i++;
        }
        w = 26;
        h = 30;
        i = 0;
        while (i < playerCurrentLife) {
            g2d.drawImage(FullHeart, w, h, 40, 40, null);
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
