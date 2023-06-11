package com.game;

public class Tick implements Runnable {
    private final int TICKS = 10;

    @Override
    public void run() {
        while (true) {
            Main.scene.repaint();
            try {
                Thread.sleep(TICKS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
