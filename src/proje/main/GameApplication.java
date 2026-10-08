package proje.main;

import proje.model.character.Player;
import proje.model.world.GameMap;
import proje.service.BattleManager;
import proje.ui.GameContext;
import proje.ui.GameFrame;
import proje.service.MusicPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;

public class GameApplication {

    private static void applyGlobalFont(Font f) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof Font) {
                UIManager.put(key, f);
            }
        }
    }

    public static void main(String[] args) {

        // 1) Font (crash-safe)
        try {
            Font uiFont = proje.ui.GameFont.get(30f);
            System.out.println("Using font: " + uiFont.getFontName());
            applyGlobalFont(uiFont);
        } catch (Throwable t) {
            System.out.println("Font load failed, using default UI font. " + t.getMessage());
        }

        // 2) Player (login panel sonra değiştirir)
        Player player = new Player("Finn");

        GameMap gameMap = new GameMap();
        BattleManager battleManager = new BattleManager();
        GameContext context = new GameContext(player, gameMap, battleManager);

        // 3) ARKA PLAN MÜZİĞİ (LOOP)
        MusicPlayer music = new MusicPlayer();
        music.playLoopFromResource("/music/Frozen_Valor_better.mp3");

        // 4) UI
        SwingUtilities.invokeLater(() -> new GameFrame(context));
    }
}
