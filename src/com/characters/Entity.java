package com.characters;

import java.awt.Image;

public class Entity {

    public Image img;
    public int x;
    public int y;

    public Entity(String img, int x, int y) {
        this.img = new javax.swing.ImageIcon(getClass().getResource(img)).getImage();

        this.x = x;
        this.y = y; 
    }
}






