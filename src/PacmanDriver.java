import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PacmanDriver {

	public static void main(String[] args) {
		pacman pm = new pacman();
		File file = new File("C:\\Users\\Kawaiii\\eclipse-workspace\\Pacman\\pacman_beginning.wav");
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			Clip clip = AudioSystem.getClip();
			clip.open(stream);
			clip.start();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
