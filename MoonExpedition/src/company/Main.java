package company;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
class Main {
    public static void main(String s[]) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Java Game");
        GamePanel gamePanel=new GamePanel();
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        gamePanel.startGameTread();


    }
}