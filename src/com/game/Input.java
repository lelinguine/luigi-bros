package com.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.lang.model.util.ElementScanner6;

public class Input implements KeyListener {

    private int leftPressed;
    private int rightPressed;
    private int shiftPressed;

    private int multipleJump;


    
    public Input() {
        leftPressed = 0;
        rightPressed = 0;
        shiftPressed = 0;
        multipleJump = 0;
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_D:
                rightPressed++;
                if(Main.scene.player.direction == "left" && Main.scene.player.speedAnimate == "run")
                {
                    Main.scene.player.deccelerate();
                    rightPressed = 0;
                    if(Main.scene.player.animateThread != null)
                    {
                        Main.scene.player.animateThread.stop();
                    }
                    Main.scene.player.animateDrift();
                    Main.scene.player.direction = "right";
                }
                else if (leftPressed == 1) {
                    leftPressed = 2;
                }
                break;
            case KeyEvent.VK_Q:
                leftPressed++;
                if(Main.scene.player.direction == "right" && Main.scene.player.speedAnimate == "run")
                {
                    Main.scene.player.deccelerate();
                    leftPressed = 0;
                    if(Main.scene.player.animateThread != null)
                    {
                        Main.scene.player.animateThread.stop();
                    }
                    Main.scene.player.animateDrift();
                    Main.scene.player.direction = "left";
                }
                else if (rightPressed == 1) {
                    rightPressed = 2;
                }
                break;
            case KeyEvent.VK_SPACE:
                Main.scene.player.isJumping++;
                multipleJump++;
                if(Main.scene.player.isJumping == 1 && multipleJump == 1) {
                    Main.scene.player.jumpPressed = true;
                    Main.scene.player.jump();
                    Thread jump = new Thread(() -> {
                        while(Main.scene.player.jumpPressed && Main.scene.player.isJumping < 3)
                        {
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                            Main.scene.player.isJumping+=2;
                        }
                    });
                    jump.start();
                }
                else {
                    Main.scene.player.jumpPressed = false;
                    Main.scene.player.isJumping = 0;
                }
                break;
            case KeyEvent.VK_S:
                Main.scene.player.downPressed = true;
                break;
            case KeyEvent.VK_Z:
                Main.scene.player.isUpPressed = true;
                if(leftPressed == 0 && rightPressed == 0)
                {
                    Main.scene.player.stop();
                }
                break;
            case KeyEvent.VK_SHIFT:
                shiftPressed++;
                if(shiftPressed == 1) {
                    Main.scene.player.run();
                }
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_D:
                rightPressed = 0;
                if (leftPressed > 0 && Main.scene.player.downPressed == false) {
                    leftPressed = 2;
                    Main.scene.player.stop();
                    Main.scene.player.stopDeccelerate();
                    Main.scene.player.move(-1);
                }
                else{
                    Main.scene.player.deccelerate();
                }
                break;
            case KeyEvent.VK_Q:
                leftPressed = 0;
                if (rightPressed > 0 && Main.scene.player.downPressed == false) {
                    rightPressed = 2;
                    Main.scene.player.stop();
                    Main.scene.player.stopDeccelerate();
                    Main.scene.player.move(1);
                }
                else{
                    Main.scene.player.deccelerate();
                }
                break;
            case KeyEvent.VK_SPACE:
            multipleJump = 0;
                break;
            case KeyEvent.VK_S:
                Main.scene.player.downPressed = false;
                Main.scene.player.animateDown(false);
                if(Main.scene.player.isUpPressed)
                {
                    Main.scene.player.stop();
                }
                else if(leftPressed >= 1 && rightPressed >= 1)
                {

                }
                else if(leftPressed >= 1)
                {
                    Main.scene.player.move(-1);
                }
                else if(rightPressed >= 1)
                {
                    Main.scene.player.move(1);
                }
                break;
            case KeyEvent.VK_Z:
                Main.scene.player.isUpPressed = false;
                if(leftPressed == 0 && rightPressed == 0 && Main.scene.player.downPressed == false && Main.scene.player.isJumping == 0)
                {
                    Main.scene.player.img = new javax.swing.ImageIcon(getClass().getResource(Main.scene.player.path+"initial-walk-"+Main.scene.player.direction+".png")).getImage();
                }
                break;
            case KeyEvent.VK_SHIFT:
                shiftPressed = 0;
                Main.scene.player.stopRun();
                break;
        }
    }

    public void keyTyped(KeyEvent e) {

        if (Main.scene.player.downPressed) {
            if(Main.scene.player.animateThread != null && Main.scene.player.animateThread.isAlive())
            {
                Main.scene.player.animateThread.stop();
            }
            
            Main.scene.player.animateDown(true);
            Main.scene.player.deccelerate();
        }
        else if (rightPressed == 1) {
            Main.scene.player.move(1);
        }
        else if (leftPressed == 1) {
            Main.scene.player.move(-1);
        }
    }

}
