package com.game;

import com.characters.Entity;
import com.characters.Player;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.JPanel;

public class Scene extends JPanel {

    public Entity sky;
    public Entity montain;
    public Player player;

    public Scene() {

        sky = new Entity("/assets/sky.png", 0, 0);
        //montain = new Entity("/assets/montain.png", 0, 0);

        player = new Player("/assets/luigi/initial-walk-right.png", 0, 283);

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new Input());

        Thread tick = new Thread(new Tick());
        tick.start();
    }

    public void paintComponent(java.awt.Graphics graphics) {

        java.awt.Graphics2D graphics2D = (java.awt.Graphics2D) graphics;

        graphics2D.drawImage(sky.img, 0, -100, null);
        
        graphics2D.drawImage(player.img, player.x, player.y, null);

        for (int i = 0; i < 500; i += 16) {
            Entity floor = new Entity("/assets/floor.png" ,i , 305);
            graphics2D.drawImage(floor.img, floor.x, floor.y, null);
        }
    }
    
}
