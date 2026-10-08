package proje.service;

import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.InputStream;

public class MusicPlayer {

    private Thread thread;
    private volatile boolean running = false;

    public void playLoopFromResource(String resourcePath) {
        stop();

        running = true;
        thread = new Thread(() -> {
            while (running) {
                try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
                    if (in == null) {
                        System.err.println("[MusicPlayer] resource not found: " + resourcePath);
                        return;
                    }
                    BufferedInputStream bis = new BufferedInputStream(in);
                    Player player = new Player(bis);
                    player.play(); // mp3 bitince while döngüsü tekrar başlatır (loop)
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }, "MusicPlayer-Thread");

        thread.setDaemon(true); // oyun kapanınca thread de kapansın
        thread.start();
    }

    public void stop() {
        running = false;
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }
}
