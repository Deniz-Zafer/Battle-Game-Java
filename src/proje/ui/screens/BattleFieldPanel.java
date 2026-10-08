package proje.ui.screens;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class BattleFieldPanel extends JPanel {

    private int enemyOffsetX = 0;

    private boolean playerDefending = false;
    private float shieldAlpha = 0f;  // 0..1
    private Timer shieldTimer;

    private Image background;
    private Image playerSprite;
    private Image enemySprite;
    private int playerOffsetX = 0;
    private Timer playerHitTimer;


    // Player çizim referansları (kalkan efekti için de kullanacağız)
    private static final int PLAYER_X = 120;
    private static final int PLAYER_Y_FROM_BOTTOM = 260;
    private static final int PLAYER_W = 180;
    private static final int PLAYER_H = 180;

    private static final int ENEMY_W = 220;
    private static final int ENEMY_H = 220;

    public BattleFieldPanel() {
        setOpaque(true);
        loadImages();
    }

    private void loadImages() {
        background   = load("/battle/orman.jpg");   // default
        playerSprite = load("/battle/player.png");  // default
        enemySprite  = load("/battle/evil.png");    // fallback default
    }

    private Image load(String path) {
        try {
            java.net.URL url = getClass().getResource(path);
            if (url == null) {
                System.out.println("Asset bulunamadı (path yanlış): " + path);
                return null;
            }
            return ImageIO.read(url);
        } catch (IOException e) {
            System.out.println("Asset okunamadı: " + path);
            return null;
        }
    }

    //  BattlePanel'den yüklemek istersen
    public Image loadPublic(String path) {
        return load(path);
    }

    //  Enemy sprite dışarıdan set edilsin
    public void setEnemySprite(Image img) {
        this.enemySprite = img;
        repaint();
    }

    //  Background dışarıdan set edilsin (compile hatanı çözer)
    public void setBackgroundImage(Image img) {
        this.background = img;
        repaint();
    }

    // (İstersen player sprite da değiştirilebilir)
    public void setPlayerSprite(Image img) {
        this.playerSprite = img;
        repaint();
    }

    public void showDefendEffect() {
        playerDefending = true;
        shieldAlpha = 1.0f;
        repaint();
    }

    public void hideDefendEffectSmooth(int durationMs) {
        if (shieldTimer != null && shieldTimer.isRunning()) {
            shieldTimer.stop();
        }

        int interval = 40;
        int steps = Math.max(1, durationMs / interval);
        final int[] step = {0};

        shieldTimer = new Timer(interval, e -> {
            step[0]++;
            shieldAlpha = 1.0f - (step[0] / (float) steps);

            if (shieldAlpha <= 0f) {
                shieldAlpha = 0f;
                playerDefending = false;
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });

        shieldTimer.start();
    }

    //  Attack animasyonu
    public void animateEnemyHit() {
        final int[] step = {0};

        Timer timer = new Timer(60, e -> {
            if (step[0] == 0) enemyOffsetX = 15;
            else if (step[0] == 1) enemyOffsetX = -10;
            else {
                enemyOffsetX = 0;
                ((Timer) e.getSource()).stop();
            }
            step[0]++;
            repaint();
        });

        timer.start();
    }

    public void animateEnemyHitStrong() {
        final int[] step = {0};
        int[] pattern = {20, -15, 12, -8, 0};

        Timer timer = new Timer(50, e -> {
            enemyOffsetX = pattern[step[0]];
            repaint();
            step[0]++;

            if (step[0] >= pattern.length) {
                enemyOffsetX = 0;
                ((Timer) e.getSource()).stop();
            }
        });

        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // Player (sol-alt)
        int playerY = getHeight() - PLAYER_Y_FROM_BOTTOM;
        if (playerSprite != null) {
            g.drawImage(
                    playerSprite,
                    120 + playerOffsetX, getHeight() - 260,
                    180, 180,
                    null
            );

        }

        //  Defend efekti
        if (playerDefending && shieldAlpha > 0f) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int cx = PLAYER_X + (PLAYER_W / 2);
            int cy = playerY + (PLAYER_H / 2);
            int rOuter = 120;
            int rInner = 80;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f * shieldAlpha));
            g2.setStroke(new BasicStroke(10));
            g2.setColor(new Color(80, 200, 255));
            g2.drawOval(cx - rOuter, cy - rOuter, rOuter * 2, rOuter * 2);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f * shieldAlpha));
            g2.setStroke(new BasicStroke(4));
            g2.setColor(new Color(120, 220, 255));
            g2.drawOval(cx - rInner, cy - rInner, rInner * 2, rInner * 2);

            RadialGradientPaint paint = new RadialGradientPaint(
                    new Point(cx, cy),
                    rOuter,
                    new float[]{0f, 1f},
                    new Color[]{
                            new Color(120, 220, 255, (int) (80 * shieldAlpha)),
                            new Color(120, 220, 255, 0)
                    }
            );

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2.setPaint(paint);
            g2.fillOval(cx - rOuter, cy - rOuter, rOuter * 2, rOuter * 2);

            g2.dispose();
        }

        // Enemy (sağ-orta)
        if (enemySprite != null) {
            int enemyX = getWidth() - 360 + enemyOffsetX;
            int enemyY = getHeight() - 320;
            g.drawImage(enemySprite, enemyX, enemyY, ENEMY_W, ENEMY_H, null);
        }
    }
    public void animatePlayerHit() {
        // varsa önceki timerı durdur
        if (playerHitTimer != null && playerHitTimer.isRunning()) {
            playerHitTimer.stop();
        }

        final int[] step = {0};
        int[] pattern = {-18, 12, -8, 5, 0}; // sola geri, sonra toparla

        playerHitTimer = new Timer(50, e -> {
            playerOffsetX = pattern[step[0]];
            repaint();
            step[0]++;

            if (step[0] >= pattern.length) {
                playerOffsetX = 0;
                ((Timer) e.getSource()).stop();
            }
        });
        playerHitTimer.start();
    }

}
