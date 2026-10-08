package proje.ui.components;

import proje.model.character.Enemy;
import proje.model.character.Player;
import proje.ui.GameContext;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BattleSceneView extends JPanel {

    private final GameContext context;
    private boolean playerDefending = false;
    private float shieldAlpha = 0f;
    private Timer shieldTimer;


    private Enemy enemy;

    private Image playerImg;
    private Image defaultEnemyImg;

    // Enemy türüne göre cache (her frame load etmesin)
    private final Map<String, Image> enemyCache = new HashMap<>();


// --- Animasyon state ---
private Timer animTimer;
private long entranceStartMs = -1L;
private long entranceDurationMs = 420L;

private int shakeFramesLeftPlayer = 0;
private int shakeFramesLeftEnemy = 0;

private int entranceOffsetPlayerX = 0;
private int entranceOffsetEnemyX = 0;

    public BattleSceneView(GameContext context) {
        this.context = context;

        animTimer = new Timer(16, e -> tickAnimations());
        animTimer.setRepeats(true);

        setOpaque(false);
        setDoubleBuffered(true);

        // Panelin layout içinde 0x0 kalmasını engellemeye yardımcı olur
        setPreferredSize(new Dimension(900, 450));

        // Base resimler
        playerImg = load("/battle/player.png");
        defaultEnemyImg = load("/battle/slime.png");

        // Debug: resource gerçekten bulunuyor mu?
        debugResource("/battle/player.png");
        debugResource("/battle/slime.png");
        debugResource("/battle/orman.jpg"); // arka planı da kontrol etmek için
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
        repaint(); // enemy değişince hemen çizimi tetikle
    }

    private void debugResource(String path) {
        URL url = getClass().getResource(path);
        System.out.println("[BattleSceneView] resource " + path + " => " + (url == null ? "NULL" : "OK"));
    }

    private Image load(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) return null;
            return new ImageIcon(url).getImage();
        } catch (Exception e) {
            return null;
        }
    }

    private Image resolveEnemyImage(Enemy e) {
        // Enemy hiç set edilmediyse default çiz
        if (e == null) return defaultEnemyImg;

        String name = e.getName() == null ? "" : e.getName().toLowerCase();

        // Cache key
        String key;
        if (name.contains("fire slime")) key = "fire_slime";
        else if (name.contains("blue slime")) key = "blue_slime";
        else if (name.contains("slime")) key = "slime";
        else if (name.contains("fire skeleton")) key = "fire_skeleton";
        else if (name.contains("ice skeleton")) key = "ice_skeleton";
        else if (name.contains("skeleton")) key = "skeleton";
        else if (name.contains("boss")) key = "boss";
        else if (name.contains("demon")) key = "evil";
        else if (name.contains("yeti")) key = "yeti";
        else key = "slime";

        // Cache’ten getir / yoksa yükle
        Image img = enemyCache.get(key);
        if (img != null) return img;

        String path = switch (key) {
            case "fire_slime" -> "/battle/fire_slime.png";
            case "blue_slime" -> "/battle/ice_slime.png";
            case "slime" -> "/battle/slime.png";
            case "fire_skeleton" -> "/battle/fire_skeleton.png";
            case "ice_skeleton" -> "/battle/ice_skeleton.png";
            case "skeleton" -> "/battle/skeleton.png";
            case "boss" -> "/battle/boss.png";
            case "evil" -> "/battle/evil.png";
            case "yeti" -> "/battle/Yeti.png";
            default -> "/battle/slime.png";
        };

        img = load(path);
        if (img == null) img = defaultEnemyImg;

        enemyCache.put(key, img);
        return img;
    }

    public void showDefendEffect() {
        playerDefending = true;
        shieldAlpha = 1f;
        repaint();
    }

    public void hideDefendEffectSmooth(int durationMs) {
        if (shieldTimer != null && shieldTimer.isRunning()) shieldTimer.stop();

        int interval = 40;
        int steps = Math.max(1, durationMs / interval);
        final int[] step = {0};

        shieldTimer = new Timer(interval, e -> {
            step[0]++;
            shieldAlpha = 1f - (step[0] / (float) steps);
            if (shieldAlpha <= 0f) {
                shieldAlpha = 0f;
                playerDefending = false;
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });

        shieldTimer.start();
    }

    public void playEntrance() {
    // Player soldan, enemy sağdan içeri kayarak gelsin
    entranceStartMs = System.currentTimeMillis();
    entranceOffsetPlayerX = -220;
    entranceOffsetEnemyX = 220;
    ensureTimerRunning();
    repaint();
}

public void shakePlayer() {
    shakeFramesLeftPlayer = 10; // ~160ms
    ensureTimerRunning();
}

public void shakeEnemy() {
    shakeFramesLeftEnemy = 10;
    ensureTimerRunning();
}

private void ensureTimerRunning() {
    if (animTimer != null && !animTimer.isRunning()) animTimer.start();
}

private void stopTimerIfIdle() {
    boolean entranceActive = entranceStartMs > 0;
    boolean shakeActive = shakeFramesLeftPlayer > 0 || shakeFramesLeftEnemy > 0;
    if (!entranceActive && !shakeActive) {
        if (animTimer != null) animTimer.stop();
    }
}

private void tickAnimations() {
    long now = System.currentTimeMillis();

    // Entrance interpolate
    if (entranceStartMs > 0) {
        long dt = now - entranceStartMs;
        double t = Math.min(1.0, dt / (double) entranceDurationMs);
        // easeOutCubic
        double eased = 1 - Math.pow(1 - t, 3);

        entranceOffsetPlayerX = (int) Math.round(-220 * (1 - eased));
        entranceOffsetEnemyX = (int) Math.round(220 * (1 - eased));

        if (t >= 1.0) {
            entranceStartMs = -1L;
            entranceOffsetPlayerX = 0;
            entranceOffsetEnemyX = 0;
        }
    }

    if (shakeFramesLeftPlayer > 0) shakeFramesLeftPlayer--;
    if (shakeFramesLeftEnemy > 0) shakeFramesLeftEnemy--;

    repaint();
    stopTimerIfIdle();
}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();
        if (w <= 1 || h <= 1) return;

        Enemy e = this.enemy;

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int groundY = (int) (h * 0.78);
            int yOffset = (int) (h * 0.10);
            groundY += yOffset;

            // ===== PLAYER ölçü/konumlarını bir kere hesapla =====
            int pw = (int) (w * 0.20);
            int ph = pw;
            int px = (int) (w * 0.12) + entranceOffsetPlayerX + shakeOffset(shakeFramesLeftPlayer);
            int py = groundY - ph;

            // PLAYER çiz
            if (playerImg != null) {
                g2.drawImage(playerImg, px, py, pw, ph, null);
            }

            // ===== DEFEND (kalkan) efektini burada çiz =====
            if (playerDefending && shieldAlpha > 0f) {
                int cx = px + (pw / 2);
                int cy = py + (ph / 2);

                int rOuter = (int) (pw * 0.70);
                int rInner = (int) (pw * 0.45);

                // dış halka
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f * shieldAlpha));
                g2.setStroke(new BasicStroke(10));
                g2.setColor(new Color(80, 200, 255));
                g2.drawOval(cx - rOuter, cy - rOuter, rOuter * 2, rOuter * 2);

                // iç halka
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f * shieldAlpha));
                g2.setStroke(new BasicStroke(4));
                g2.setColor(new Color(120, 220, 255));
                g2.drawOval(cx - rInner, cy - rInner, rInner * 2, rInner * 2);

                // hafif glow
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
            }

            // ===== ENEMY =====
            Image ei = resolveEnemyImage(e);
            if (ei != null) {
                int ew = (int) (w * 0.26);
                int eh = ew;
                int ex = (int) (w * 0.62) + entranceOffsetEnemyX + shakeOffset(shakeFramesLeftEnemy);
                int ey = groundY - eh;
                g2.drawImage(ei, ex, ey, ew, eh, null);
            }

        } finally {
            g2.dispose();
        }
    }


    private int shakeOffset(int framesLeft) {
        if (framesLeft <= 0) return 0;

        // Genlik: ilk frame'lerde daha büyük, sona doğru azalsın
        // 10 frame için: 8px -> 1px gibi
        int amplitude = Math.max(1, framesLeft);

        // Sağa/sola zıplat: tek/çift frame'e göre yön değiştir
        int dir = (framesLeft % 2 == 0) ? 1 : -1;

        return dir * amplitude;
    }
}
