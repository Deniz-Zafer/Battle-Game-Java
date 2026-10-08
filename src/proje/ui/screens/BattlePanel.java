package proje.ui.screens;

import proje.model.character.*;
import proje.model.item.Item;
import proje.ui.GameContext;
import proje.ui.GameFrame;
import proje.ui.components.*;
import proje.ui.theme.UiTheme;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class BattlePanel extends JPanel {

    private final GameFrame frame;
    private final GameContext context;

    private Enemy enemy;
    private int currentLevelIdx = 0;

    private BattleSceneView sceneView;
    private BackgroundPanel rootBg;

    private HealthBar playerHpBar;
    private ManaBar playerManaBar;
    private JLabel playerName;
    private JLabel playerAtk;
    private JLabel playerDef;

    private HealthBar enemyHpBar;
    private JLabel enemyAtk;
    private JLabel enemyDef;

    private SlotView equipSlot1;
    private SlotView equipSlot2;
    private SlotView amuletSlot1;
    private SlotView amuletSlot2;

    private JTextArea logArea;

    private JPanel playerStatusPanel;
    private JPanel enemyStatusPanel;

    private JButton attackBtn;
    private JButton defendBtn;
    private JButton skillBtn;
    private JButton invBtn;

    private boolean inputLocked = false;

    public BattlePanel(GameFrame frame, GameContext context) {
        this.frame = frame;
        this.context = context;

        setLayout(new BorderLayout());

        sceneView = new BattleSceneView(context);
        sceneView.setOpaque(false);

        Image bg = loadBackgroundForLevel(0);

        rootBg = new BackgroundPanel(bg);
        rootBg.setLayout(new BorderLayout());
        add(rootBg, BorderLayout.CENTER);

        rootBg.add(sceneView, BorderLayout.CENTER);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        playerStatusPanel = createPlayerStatus();
        enemyStatusPanel = createEnemyStatus();

        JPanel left = playerStatusPanel;
        JPanel right = enemyStatusPanel;
        left.setPreferredSize(new Dimension(460, 180));
        right.setPreferredSize(new Dimension(460, 120));

        top.add(left, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);

        rootBg.add(top, BorderLayout.NORTH);
        rootBg.add(createBottomArea(), BorderLayout.SOUTH);

        loadLevel(0);
    }

    public void loadLevel(int idx) {
        this.currentLevelIdx = idx;

        this.enemy = createEnemyForLevel(idx);

        if (sceneView != null) {
            sceneView.setEnemy(enemy);
            sceneView.playEntrance();
            sceneView.repaint();
}

        if (rootBg != null) {
            rootBg.setImage(loadBackgroundForLevel(idx));
        }

        refreshUI();
        if (logArea != null) {
            logArea.append("LEVEL " + (idx + 1) + " başladı: " + enemy.getName() + "\n");
        }
    }

    public void refreshUI() {
        Player p = context.getPlayer();
        if (p == null) return;

        playerName.setText(p.getName());

        if (playerStatusPanel != null && playerStatusPanel.getBorder() instanceof TitledBorder tb) {
            tb.setTitle(p.getName());
        }
        playerHpBar.update(p.getHealth(), p.getMaxHealth());
        playerManaBar.update(p.getMana(), p.getMaxMana());
        playerAtk.setText("ATK: " + p.getAttackPower());
        playerDef.setText("DEF: " + p.getDefense());

        updateSlot(equipSlot1, p.getEquipSlot(0));
        updateSlot(equipSlot2, p.getEquipSlot(1));
        updateSlot(amuletSlot1, p.getAmuletSlot(0));
        updateSlot(amuletSlot2, p.getAmuletSlot(1));

        if (enemy != null) {
            if (enemyStatusPanel != null && enemyStatusPanel.getBorder() instanceof TitledBorder tb) {
                tb.setTitle(enemy.getName());
            }
            enemyHpBar.update(enemy.getHealth(), enemy.getMaxHealth());
            enemyAtk.setText("ATK: " + enemy.getAttackPower());
            enemyDef.setText("DEF: " + enemy.getDefense());
        }

        revalidate();
        repaint();
        if (sceneView != null) sceneView.repaint();
    }

    private JPanel createPlayerStatus() {
        Player p = context != null ? context.getPlayer() : null;
        if (p == null) {
            p = new Player("Finn");
            if (context != null) context.setPlayer(p);
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        TitledBorder border = BorderFactory.createTitledBorder(p.getName());
        border.setTitleFont(UiTheme.TITLE_FONT);
        border.setTitleColor(UiTheme.TEXT_BLUE);
        panel.setBorder(border);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        playerName = new JLabel(" ");

        playerHpBar = new HealthBar("HP    ", p.getHealth(), p.getMaxHealth(), 220, 14);
        content.add(playerHpBar);
        content.add(Box.createVerticalStrut(4));

        // 18px daha stabil (label’lar sıkışmasın)
        playerManaBar = new ManaBar("MANA", p.getMana(), p.getMaxMana(), 220, 18);
        content.add(playerManaBar);

        panel.add(content, BorderLayout.CENTER);

        playerAtk = new JLabel("ATK: -");
        playerDef = new JLabel("DEF: -");

        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);

        JPanel stats = new JPanel();
        stats.setOpaque(false);
        stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));
        stats.add(playerAtk);
        stats.add(playerDef);
        south.add(stats, BorderLayout.WEST);

        JPanel slots = new JPanel(new GridLayout(1, 4, 8, 0));
        slots.setOpaque(false);
        equipSlot1 = new SlotView();
        equipSlot2 = new SlotView();
        amuletSlot1 = new SlotView();
        amuletSlot2 = new SlotView();
        slots.add(equipSlot1);
        slots.add(equipSlot2);
        slots.add(amuletSlot1);
        slots.add(amuletSlot2);
        south.add(slots, BorderLayout.EAST);

        panel.add(south, BorderLayout.SOUTH);

        return panel;
    }

    private enum Spell {
        FIREBALL("Ateş Topu", 10, 24),
        ICE_SPIKE("Buz Mızrağı", 8, 20),
        HEAL("İyileştir", 12, -30);

        final String title;
        final int manaCost;
        final int power;

        Spell(String title, int manaCost, int power) {
            this.title = title;
            this.manaCost = manaCost;
            this.power = power;
        }

        @Override
        public String toString() {
            return title + " (Mana " + manaCost + ")";
        }
    }

    private void showDeathDialog() {
        lockInput(true);

        JDialog dialog = new JDialog(frame, "Öldün", true);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("ÖLDÜN", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        root.add(title, BorderLayout.NORTH);

        JLabel info = new JLabel("Tekrar denemek ister misin?", SwingConstants.CENTER);
        info.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        root.add(info, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 12, 0));
        JButton retry = new JButton("Tekrar Dene");
        JButton menu = new JButton("Menü");

        retry.addActionListener(e -> {
            dialog.dispose();

            Player p = context.getPlayer();
            if (p != null) {
                p.setHealth(p.getMaxHealth());
                p.setMana(p.getMaxMana());
                p.setDefending(false);
            }

            loadLevel(0);
            lockInput(false);
        });

        menu.addActionListener(e -> {
            dialog.dispose();
            frame.backToMenu();
        });

        buttons.add(retry);
        buttons.add(menu);

        root.add(buttons, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setSize(420, 260);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private JPanel createEnemyStatus() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        Enemy e = (enemy != null) ? enemy : new Slime();
        enemy = e;

        if (sceneView != null) sceneView.setEnemy(enemy);

        TitledBorder border = BorderFactory.createTitledBorder(e.getName());
        border.setTitleFont(UiTheme.TITLE_FONT);
        border.setTitleColor(UiTheme.TEXT_BLUE);
        panel.setBorder(border);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        enemyHpBar = new HealthBar("HP", e.getHealth(), e.getMaxHealth(), 220, 14);
        content.add(enemyHpBar);
        panel.add(content, BorderLayout.CENTER);

        enemyAtk = new JLabel("ATK: " + e.getAttackPower());
        enemyDef = new JLabel("DEF: " + e.getDefense());
        JPanel stats = new JPanel();
        stats.setOpaque(false);
        stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));
        stats.add(enemyAtk);
        stats.add(enemyDef);
        panel.add(stats, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBottomArea() {
        JPanel bottom = new JPanel(new BorderLayout(16, 0));
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 12, 12, 12));

        logArea = new JTextArea(6, 20);
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);

        JScrollPane logScroll = new JScrollPane(logArea);
        TitledBorder logBorder = BorderFactory.createTitledBorder("Savaş açıklama bloğu");
        logBorder.setTitleFont(UiTheme.TITLE_FONT);
        logBorder.setTitleColor(UiTheme.TEXT_BLUE);
        logScroll.setBorder(logBorder);

        bottom.add(logScroll, BorderLayout.CENTER);

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        JPanel actions = new JPanel(new GridLayout(2, 2, 14, 14));
        actions.setOpaque(false);

        attackBtn = new RoundedButton("saldır");
        attackBtn.addActionListener(e -> {
            Player p = context.getPlayer();
            if (p == null || enemy == null) return;
            if (inputLocked) return;
            if (!enemy.isAlive()) {
                logArea.append("Düşman zaten öldü.\n");
                return;
            }
            p.attack(enemy);
            if (sceneView != null) sceneView.shakeEnemy();
            logArea.append(p.getName() + " saldırdı!\n");
            refreshUI();
            if (!checkEnemyDefeated()) {
                runEnemyTurn();
            }
        });

        defendBtn = new RoundedButton("savun");
        defendBtn.addActionListener(e -> {
            Player p = context.getPlayer();
            if (p == null) return;
            if (inputLocked) return;

            p.setDefending(true);
            //  Kalkanı göster
            if (sceneView != null) sceneView.showDefendEffect();

            logArea.append(p.getName() + " savunmaya geçti.\n");
            runEnemyTurn();
        });


        // SADECE 1 TANE skillBtn: büyü kullan
        skillBtn = new RoundedButton("büyü kullan");
        skillBtn.addActionListener(e -> {
            if (inputLocked) return;

            Player p = context.getPlayer();
            if (p == null || enemy == null) return;

            if (!enemy.isAlive()) {
                logArea.append("Düşman zaten öldü.\n");
                return;
            }

            Spell selected = (Spell) JOptionPane.showInputDialog(
                    this,
                    "Bir büyü seç:",
                    "Büyü Kullan",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    Spell.values(),
                    Spell.FIREBALL
            );

            if (selected == null) return;

            if (p.getMana() < selected.manaCost) {
                logArea.append("Yetersiz mana! Gerekli: " + selected.manaCost + "\n");
                refreshUI();
                return;
            }

            // mana düş
            p.setMana(p.getMana() - selected.manaCost);

            // heal
            if (selected.power < 0) {
                int heal = -selected.power;
                p.setHealth(Math.min(p.getMaxHealth(), p.getHealth() + heal));
                logArea.append(p.getName() + " " + selected.title + " kullandı! +" + heal + " HP\n");
                refreshUI();
                runEnemyTurn();
                return;
            }

            // damage
            int dmg = selected.power;
            enemy.setHealth(Math.max(0, enemy.getHealth() - dmg));


            if (sceneView != null) sceneView.shakeEnemy();
            logArea.append(p.getName() + " " + selected.title + " kullandı! " + dmg + " hasar.\n");
            refreshUI();

            if (!checkEnemyDefeated()) {
                runEnemyTurn();
            }
        });

        invBtn = new RoundedButton("envanter");
        invBtn.addActionListener(e -> {
            if (inputLocked) return;
            frame.openInventoryFromBattle();
        });

        actions.add(attackBtn);
        actions.add(defendBtn);
        actions.add(skillBtn);
        actions.add(invBtn);
        right.add(actions);

        bottom.add(right, BorderLayout.EAST);

        ConsoleToTextArea.redirect(logArea);

        return bottom;
    }

    private void updateSlot(SlotView slot, Item item) {
        if (slot == null) return;
        if (item == null) {
            slot.setToolTipText("Empty");
            slot.clear();
            return;
        }

        slot.setToolTipText(proje.ui.components.ItemTooltip.build(item));
        String path = proje.ui.components.ItemIconResolver.iconPath(item);
        if (path == null) {
            slot.clear();
            return;
        }

        java.net.URL url = getClass().getResource(path);
        if (url == null) {
            slot.clear();
            return;
        }

        slot.setIconImage(new ImageIcon(url).getImage());
    }

    private Image loadBackgroundForLevel(int idx) {
        String path = switch (idx) {
            case 0 -> "/battle/orman.jpg";
            case 1 -> "/battle/sisliyol.jpg";
            case 2 -> "/battle/buz_magarasi.jpg";
            case 3 -> "/battle/lav.jpg";
            case 4 -> "/battle/kule.jpg";
            default -> "/battle/orman.jpg";
        };
        return loadImage(path);
    }

    private Enemy createEnemyForLevel(int idx) {
        java.util.Random r = new java.util.Random();

        return switch (idx) {
            case 0 -> {
                int roll = r.nextInt(3);
                yield switch (roll) {
                    case 0 -> new Slime();
                    case 1 -> new BlueSlime();
                    default -> new FireSlime();
                };
            }
            case 1 -> {
                int roll = r.nextInt(3);
                yield switch (roll) {
                    case 0 -> new Skeleton();
                    case 1 -> new IceSkeleton();
                    default -> new FireSkeleton();
                };
            }
            case 2 -> new Yeti();
            case 3 -> new Evil();
            case 4 -> new Boss();
            default -> new Slime();
        };
    }

    private boolean checkEnemyDefeated() {
        if (enemy == null) return false;
        if (enemy.isAlive()) return false;

        logArea.append(enemy.getName() + " yenildi.\n");
        lockInput(true);

        // FINAL BOSS YENİLDİ → VICTORY
        if (currentLevelIdx >= 4) {
            logArea.append("Tüm bölümler tamamlandı.\n");
            SwingUtilities.invokeLater(() -> frame.showVictoryScreen());
            return true;
        }

        java.util.List<Item> drops = new java.util.ArrayList<>();
        int levelNumber = currentLevelIdx + 1;

        for (int i = 0; i < 3; i++) {
            Item it = proje.service.ItemFactory.getRandomItemForLevel(levelNumber);
            if (it != null) {
                context.getPlayer().addItem(it);
                drops.add(it);
            }
        }

        showLevelCompleteDialog(drops);
        return true;
    }

    private void runEnemyTurn() {
        if (enemy == null) return;
        Player p = context.getPlayer();
        if (p == null) return;
        if (!p.isAlive()) return;
        if (!enemy.isAlive()) return;

        lockInput(true);

        new javax.swing.Timer(450, evt -> {
            ((javax.swing.Timer) evt.getSource()).stop();

            if (!enemy.isAlive() || !p.isAlive()) {
                lockInput(false);
                return;
            }

            int baseDamage = enemy.getAttackPower();
            proje.model.combat.ElementType element = enemy.getElementType();
            p.receiveElementalAttack(baseDamage, element);

            // ANİMASYONLAR VE DEFEND
            if (sceneView != null) sceneView.shakePlayer();
            if (sceneView != null) sceneView.hideDefendEffectSmooth(300);
            p.setDefending(false);

            logArea.append(enemy.getName() + " saldırdı!\n");
            refreshUI();

            if (!p.isAlive()) {
                logArea.append("Oyuncu yenildi.\n");
                showDeathDialog();
                return;
            }

            lockInput(false);
        }).start();
    }

    private void lockInput(boolean locked) {
        inputLocked = locked;
        if (attackBtn != null) attackBtn.setEnabled(!locked);
        if (defendBtn != null) defendBtn.setEnabled(!locked);
        if (skillBtn != null) skillBtn.setEnabled(!locked);
        if (invBtn != null) invBtn.setEnabled(!locked);
    }

    private void showLevelCompleteDialog(java.util.List<Item> drops) {
        JDialog dialog = new JDialog(frame, true);
        dialog.setUndecorated(true); // ✅ çerçevesiz
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        LevelCompletePanel panel = new LevelCompletePanel(frame, context);

        StringBuilder sb = new StringBuilder();
        sb.append("Seviye tamamlandı.\n\n");
        if (drops.isEmpty()) {
            sb.append("Bu itemler düştü: (yok)\n");
        } else {
            sb.append("Bu itemler düştü:\n");
            for (Item it : drops) {
                sb.append("- ").append(it.getName()).append("\n");
            }
        }
        sb.append("\nDevam'a basınca bir sonraki bölüme geçilir.");
        panel.setDetailsText(sb.toString());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 90, 220), 3, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        wrapper.setOpaque(false);
        wrapper.add(panel, BorderLayout.CENTER);
        wrapper.add(panel, BorderLayout.CENTER);

        dialog.setContentPane(wrapper);

        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(frame);

        attachDialogButtons(panel, dialog);

        dialog.setVisible(true);
    }


    private void attachDialogButtons(Container root, JDialog dialog) {
        for (Component c : root.getComponents()) {
            if (c instanceof JButton btn) {
                String t = btn.getText();
                if (t != null && t.equalsIgnoreCase("Devam")) {
                    btn.addActionListener(e -> {
                        dialog.dispose();
                        int next = currentLevelIdx + 1;
                        if (next < 5) {
                            loadLevel(next);
                            lockInput(false);
                        } else {
                            dialog.dispose(); // level dialog’u kapansın
                            logArea.append("Tüm bölümler tamamlandı.\n");
                            lockInput(true);

                            //  FINAL: Victory ekranına geç
                            frame.showVictoryScreen();
                        }
                    });
                }
            } else if (c instanceof Container cont) {
                attachDialogButtons(cont, dialog);
            }
        }
    }

    public void unlockInput() {
        lockInput(false);
    }

    private Image loadImage(String path) {
        try {
            java.net.URL url = getClass().getResource(path);
            if (url == null) return null;
            return new ImageIcon(url).getImage();
        } catch (Exception e) {
            return null;
        }
    }
}
