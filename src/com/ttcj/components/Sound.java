package com.ttcj.components;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JApplet;


public class Sound extends JApplet{
	private AudioClip sound;
	private URL soundPath;
	
	public Sound (String filename){
		try{
			soundPath = new File(filename).toURI().toURL();
			sound = Applet.newAudioClip(soundPath);
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	public void playSound(){
		sound.play();
	}
}


/*
public class Sound{
	private Clip soundClip;
	public Sound(String filename)
	{
		File file = new File(filename);
		AudioInputStream sound = null;
		try {
			sound = AudioSystem.getAudioInputStream(file);
		} catch (UnsupportedAudioFileException | IOException e) {
			System.out.println("IO error");
			e.printStackTrace();
		}
			try {
				soundClip = AudioSystem.getClip();
			} catch (LineUnavailableException e) {
				System.out.println("IO error");
				e.printStackTrace();
			}

			try {
				soundClip.open(sound);
			} catch (LineUnavailableException e) {
				System.out.println("Sound cant be opened");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("IO error");
				e.printStackTrace();
			}

			soundClip.setFramePosition(0);
			soundClip.start();
	}
}
*/
