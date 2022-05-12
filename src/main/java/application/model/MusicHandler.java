package application.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class MusicHandler implements Runnable {
    private MediaPlayer mediaPlayer;
    private Thread thread;
    private File[] files;
    private boolean playing = false;
    private int i = 0;

    public MusicHandler() {
        System.out.println("music handler");
        files = new File("music").listFiles();
        thread = new Thread(this);
        mediaPlayer = new MediaPlayer(new Media(files[0].toURI().toString()));
    }

    public void nextButton() {
        System.out.println("next");
        playing = false;
        mediaPlayer = new MediaPlayer(new Media(files[nextSong()].toURI().toString()));
        playing = true;
        thread = new Thread(this);
        thread.start();
    }

    public void prevButton() {
        System.out.println("prev");
        playing = false;
        mediaPlayer = new MediaPlayer(new Media(files[prevSong()].toURI().toString()));
        playing = true;
        thread = new Thread(this);
        thread.start();
    }

    private int nextSong() {
        i = (i + 1) % files.length;
        return i;
    }

    private int prevSong() {
        i = (i + files.length - 1) % files.length;
        return i;
    }

    public void playButton() {
        if (!playing) {
            System.out.println("yabadaba");
            playing=true;
            thread = new Thread(this);
            thread.start();
        } else {
            System.out.println("wabadab");
            playing=false;
        }
    }

    @Override
    public void run() {
        do {
            mediaPlayer.play();
        } while (playing);
        mediaPlayer.pause();
    }
}
