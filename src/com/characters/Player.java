package com.characters;

import javax.sound.sampled.*;
import java.io.File;

public class Player extends Entity{

    private boolean isAnimate;
    public volatile  String speedAnimate;

    public boolean isStopped;

    public boolean isUpPressed;

    private volatile  int speed;
    public volatile boolean downPressed;

    public volatile int isJumping;
    public volatile boolean jumpPressed;

    public String path;
    public String direction;

    public Thread moveThread;
    public Thread animateThread;
    public Thread deccelerateThread;

    public Player(String img, int x, int y) {
        super(img, x, y);
        path = "/assets/luigi/";
        speedAnimate = "walk";
        speed = 3;
        direction = "right";
        downPressed = false;
        isStopped = true;
        deccelerateThread = null;
        jumpPressed = false;
    }

    //************************************************************************

    public void animate() {

            isAnimate = true;
            stop();
            stopDeccelerate();

            animateThread = new Thread(() -> {while(true)
            {

                if(isJumping == 0)
                {
                    int speedHere = speed;
                    String initial = path+"initial-"+speedAnimate+"-"+direction+".png";
                    String img = path+speedAnimate+"-"+direction+".png";
                    this.img = new javax.swing.ImageIcon(getClass().getResource(img)).getImage();
                    try {
                        Thread.sleep(300/speedHere);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.img = new javax.swing.ImageIcon(getClass().getResource(initial)).getImage();
                    try {
                        Thread.sleep(300/speedHere);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
            }});
            animateThread.start();
    }

    public void animateDown(boolean isPressed) {

        if(isJumping == 0)
        {
            if(isPressed)
            {
                img = new javax.swing.ImageIcon(getClass().getResource(path+"back-"+direction+".png")).getImage();
                y = 291;
                
            }
            else
            {
                img = new javax.swing.ImageIcon(getClass().getResource(path+"initial-walk-"+direction+".png")).getImage();
                y = 283;
            }
        }
    }

    public void animateDrift()
    {
        if(isJumping == 0)
        {
            img = new javax.swing.ImageIcon(getClass().getResource(path+"drift-"+direction+".png")).getImage();
        }
    }

    //************************************************************************

    public void jumpAnimate()
    {
        img = new javax.swing.ImageIcon(getClass().getResource(path+"jump-"+direction+".png")).getImage();
    }


    public void animateFall()
    {
        img = new javax.swing.ImageIcon(getClass().getResource(path+"fall-"+direction+".png")).getImage();
    }


    public void jump() {
    y = 283;
    if (y == 283) {

        try {

            File audioFile = new File("src/sounds/jump.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat audioFormat = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioInputStream);
            clip.start();


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
        }

        Thread jumpThread = new Thread(() -> {
            int i = 0;
            int jumpScale = 1;
            while (i < 35) {
                jumpScale = isJumping;
                jumpAnimate();
                y -= 1;
                try {
                    Thread.sleep(2 * jumpScale);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
            animateFall();
            while (i > 0) {
                y += 1;
                try {
                    Thread.sleep(4 * jumpScale);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (jumpScale > 1) {
                    jumpScale--;
                }

                i--;
            }
            isJumping = 0;
            if (isUpPressed) {
                img = new javax.swing.ImageIcon(getClass().getResource(path + "up-" + direction + ".png")).getImage();
            } else if (downPressed) {
                y = 291;
                img = new javax.swing.ImageIcon(getClass().getResource(path + "back-" + direction + ".png")).getImage();
            } else {
                img = new javax.swing.ImageIcon(getClass().getResource(path + "initial-walk-" + direction + ".png")).getImage();
            }
            isJumping = 0;
            jumpPressed = false;
        });
        jumpThread.start();
    }
}


    
    //************************************************************************

    public void move(int pos) {
        isAnimate = false;
        isStopped = false;
        if(!isAnimate) {
            if(pos > 0)
            {
                direction = "right";
            }
            else if(pos < 0)
            {
                direction = "left";
            }
            animate();
        }
        if(speedAnimate == "run")
        {
            speed = 5;
        }
        else if(speedAnimate == "walk")
        {
            speed = 3;
        }
        moveThread = new Thread(() -> walk(pos));
        moveThread.start();
    }

    private void walk(int pos) {
        while (true) {
            int speedHere = speed;
            if(x > 0 && x < 490)
            {
                x += pos;
            }
            else if(x == 0)
            {
                x += 1;
            }
            else if(x == 490)
            {
                x -= 1;
            }
            try {
                Thread.sleep(30/speedHere);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        speedAnimate = "run";
        speed = 5;
    }

    public void stopRun() {
        Thread stopRunThread = new Thread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
            }
            speedAnimate = "walk";
            speed = 3;
        });
        stopRunThread.start();
    }

    //************************************************************************

    public void deccelerate() {

        if(!(deccelerateThread != null && deccelerateThread.isAlive()))
        {
            deccelerateThread = new Thread(() -> {
                while (speed > 1) {
                    speed--;
                    try {
                        Thread.sleep(30*speed);
                    } catch (InterruptedException e) {
                    }
                }
                stop();
            });
            deccelerateThread.start();
        }
    }

    public void stopDeccelerate() {
        if(deccelerateThread != null && deccelerateThread.isAlive())
        {
            deccelerateThread.stop();
        }
    }

    //************************************************************************

    public void stop() {
        if(moveThread != null && moveThread.isAlive())
        {
            moveThread.stop();
            isStopped = true;
        }
        if(animateThread != null && animateThread.isAlive())
        {
            animateThread.stop();
        }

        if(isUpPressed && !downPressed)
        {
            this.img = new javax.swing.ImageIcon(getClass().getResource(path+"up-"+direction+".png")).getImage();
        }
        else if(!downPressed)
        {
            this.img = new javax.swing.ImageIcon(getClass().getResource(path+"initial-walk-"+direction+".png")).getImage();
        }
        
    }
}
