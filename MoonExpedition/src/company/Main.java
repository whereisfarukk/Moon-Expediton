package company;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Main {
    public static void main(String s[]) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Moon Expedition");
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

//        window.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                System.out.println(e.getX() + "," + e.getY());
//            }
//        });

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
//        GamePanel gm=new GamePanel();
//        gm.playMusic(4);
        gamePanel.setupGame();
        gamePanel.startGameTread();


    }
}