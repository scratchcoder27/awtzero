package awtzero;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * A simple music player class that can load and play audio files.
 */
public class MusicPlayer {
    private Clip clip;

    /**
     * Loads an audio file from the specified path.
     * @param path The path to the audio file.
     * @throws IOException If an I/O error occurs.
     * @throws UnsupportedAudioFileException If the audio file format is not supported.
     * @throws LineUnavailableException If a line cannot be opened because it is unavailable.
     */
    public void load(String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File file = new File(path);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
    }

    /**
     * Plays the loaded audio file from the beginning.
     * If it is already playing, it restarts the playback.
    */
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // rewind
            clip.start();
        }
    }

    /**
     * Loops the loaded audio file continuously.
     */
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Stops the playback of the audio file, whether it's playing or looping.
     */
    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}