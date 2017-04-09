package com.ttcj.components;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

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
