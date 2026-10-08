package proje.ui.screens;

import proje.ui.GameContext;
import proje.ui.GameFrame;
import proje.ui.components.BackgroundPanel;
import proje.ui.components.RoundedButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class VictoryPanel extends JPanel {

    private final GameFrame frame;
    private final GameContext context;

    public VictoryPanel(GameFrame frame, GameContext context) {
        this.frame = frame;
        this.context = context;

        setLayout(new BorderLayout());

        // === Victory background ===
        BackgroundPanel bg = createBackground("/menu/victory.png");
        add(bg, BorderLayout.CENTER);

        // Alt kısım için overlay panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 60, 20));

        // === Renk paleti (login ile uyumlu) ===
        Color parchment = new Color(255, 248, 230);
        Color parchmentHover = new Color(255, 242, 210);
        Color borderBrown = new Color(150, 120, 70);
        Color darkText = new Color(60, 40, 20);

        // === Yeni Oyun ===
        RoundedButton btnNewGame = new RoundedButton("Yeni Oyun");
        styleButton(btnNewGame, parchment, parchmentHover, borderBrown, darkText);
        btnNewGame.addActionListener(e -> {
            String name = context.getPlayer() != null
                    ? context.getPlayer().getName()
                    : "Finn";
            frame.startNewGame(name);
        });

        // === Çıkış ===
        RoundedButton btnExit = new RoundedButton("Çıkış");
        styleButton(btnExit, parchment, parchmentHover, borderBrown, darkText);
        btnExit.addActionListener(e -> System.exit(0));

        bottomPanel.add(btnNewGame);
        bottomPanel.add(Box.createVerticalStrut(16));
        bottomPanel.add(btnExit);

        // Alt-orta yerleşim
        bg.setLayout(new BorderLayout());
        bg.add(bottomPanel, BorderLayout.SOUTH);
    }

    // === Button stil helper ===
    private void styleButton(
            RoundedButton b,
            Color fill,
            Color hover,
            Color border,
            Color text
    ) {
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setPreferredSize(new Dimension(320, 64));
        b.setMaximumSize(new Dimension(320, 64));

        b.setFont(b.getFont().deriveFont(Font.BOLD, 26f));
        b.setForeground(text);

        b.setFillColor(fill);
        b.setHoverFill(hover);
        b.setBorderColor(border);
    }

    // === Background loader ===
    private BackgroundPanel createBackground(String path) {
        URL url = getClass().getResource(path);
        if (url == null) {
            return new BackgroundPanel(null);
        }

        try {
            BufferedImage img = ImageIO.read(url);
            return new BackgroundPanel(img);
        } catch (IOException e) {
            return new BackgroundPanel(null);
        }
    }
}