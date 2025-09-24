package awtzero;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private Clip clip;

    public void load(String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File file = new File(path);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // rewind
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}