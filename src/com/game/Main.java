package com.game;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.JFrame;
import javax.sound.sampled.FloatControl;
public class Main {
 
    public static Scene scene;
    public static void main(String[] args) {
        JFrame frame = new JFrame("Super Luigi Bros. (test)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520, 360);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        scene = new Scene();
        frame.setContentPane(scene);
        frame.setVisible(true);

        try {
            File audioFile = new File("src/sounds/ost.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat audioFormat = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioInputStream);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = -8.0f;
            gainControl.setValue(volume);

            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
        }

    }
}
