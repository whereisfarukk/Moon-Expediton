package company;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean rightKeyPressed,leftKeyPressed,spacekeyPressed;
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code=e.getKeyCode();
        if(code==KeyEvent.VK_LEFT){
            leftKeyPressed=true;
        }
        if(code==KeyEvent.VK_RIGHT){
            rightKeyPressed=true;
        }
        if(code==KeyEvent.VK_SPACE){
            spacekeyPressed=true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code=e.getKeyCode();
        if(code==KeyEvent.VK_LEFT){
            leftKeyPressed=false;
        }
        if(code==KeyEvent.VK_RIGHT){
            rightKeyPressed=false;
        }
        if(code==KeyEvent.VK_SPACE){
            spacekeyPressed=false;
        }
    }
}
