package company;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean rightKeyPressed, leftKeyPressed, spaceKeyPressed;
    GamePanel gp;
    Player player;

    public KeyHandler(GamePanel gp, Player player) {
        this.gp = gp;
        this.player = player;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        //TITLE SELECT
        if (gp.gameState == gp.titleState) {
            if(gp.selectedTitleCommand==-1) {
                if (code == KeyEvent.VK_UP) {
                    gp.gameTitleCommand--;
                    if (gp.gameTitleCommand < 0) {
                        gp.gameTitleCommand = 3;
                    }
                }

                if (code == KeyEvent.VK_DOWN) {
                    gp.gameTitleCommand++;
                    if (gp.gameTitleCommand > 3) {
                        gp.gameTitleCommand = 0;
                    }
                }
            }
            if(gp.gameState==gp.titleState ){
                if(gp.gameTitleCommand==gp.titleAboutState){
                    if(code==KeyEvent.VK_LEFT) {
                        gp.selectedTitleCommand = -1;
                    }
                }
                if(gp.gameTitleCommand==gp.titleHighScoreState){
                    if(code==KeyEvent.VK_LEFT) {
                        gp.selectedTitleCommand = -1;
                    }
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.gameTitleCommand == 0) {
                    /**
                     * Selecting play state
                     */
                    gp.gameState = gp.playState;
                }
                if (gp.gameTitleCommand == 1) {
                    //CODE FOR HIGH SCORE
                    gp.selectedTitleCommand=1;
                }
                if (gp.gameTitleCommand == 2) {
                    //ABOUT THE  GAME
                    gp.selectedTitleCommand=2;

                }
                if (gp.gameTitleCommand == 3) {
                    /**
                     *  Exit the whole game
                     */
                    System.exit(0);
                }
            }
        }
        if (code == KeyEvent.VK_LEFT) {
            leftKeyPressed = true;
        }
        if (code == KeyEvent.VK_RIGHT) {
            rightKeyPressed = true;
        }
        if (code == KeyEvent.VK_SPACE) {
            spaceKeyPressed = true;
        }
        if (code == KeyEvent.VK_P) {
            if (gp.gameState == gp.playState) {
                gp.gameState = gp.pauseState;
            }
        }
        if (code == KeyEvent.VK_R) {
            if (gp.gameState == gp.pauseState) {
                gp.gameState = gp.playState;
            }
        }
        /**
         * Game over screen
         */
        if (gp.gameState == gp.gameOverState) {
            if (code == KeyEvent.VK_UP && gp.gameOverCommand == 1) {
                gp.gameOverCommand = 0;

            }
            if (code == KeyEvent.VK_DOWN && gp.gameOverCommand == 0) {
                gp.gameOverCommand = 1;
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.gameOverCommand == 0) {
                    player.setDefault();
                    gp.gameState = gp.playState;
                }
                if (gp.gameOverCommand == 1) {
                    player.setDefault();
                    gp.gameState = gp.titleState;
                }
            }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) {
            leftKeyPressed = false;
        }
        if (code == KeyEvent.VK_RIGHT) {
            rightKeyPressed = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            spaceKeyPressed = false;
        }
    }
}
