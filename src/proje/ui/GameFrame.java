package proje.ui;

import proje.model.character.Player;
import proje.ui.screens.BattlePanel;
import proje.ui.screens.InventoryPanel;
import proje.ui.screens.LoginPanel;
import proje.ui.screens.VictoryPanel;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public static final String SCREEN_LOGIN = "SCREEN_LOGIN";
    public static final String SCREEN_VICTORY = "SCREEN_VICTORY";
    public static final String SCREEN_BATTLE = "SCREEN_BATTLE";
    public static final String SCREEN_INVENTORY = "SCREEN_INVENTORY";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel root = new JPanel(cardLayout);

    private final GameContext context;

    private final LoginPanel loginPanel;
    private final VictoryPanel victoryPanel;
    private final BattlePanel battlePanel;
    private final InventoryPanel inventoryPanel;

    public GameFrame(GameContext context) {
        super("Totally Accurate Battle Game");
        this.context = context;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setLocationRelativeTo(null);

        // Paneller
        loginPanel = new LoginPanel(this, context);
        victoryPanel = new VictoryPanel(this, context);
        battlePanel = new BattlePanel(this, context);
        inventoryPanel = new InventoryPanel(this, context);

        // Card layout kayıtları
        root.add(loginPanel, SCREEN_LOGIN);
        root.add(victoryPanel, SCREEN_VICTORY);
        root.add(battlePanel, SCREEN_BATTLE);
        root.add(inventoryPanel, SCREEN_INVENTORY);

        setContentPane(root);
        setVisible(true);

        // Açılışta giriş ekranı
        showScreen(SCREEN_LOGIN);
    }

    //Bitiş ekranı
    public void showVictoryScreen() {
        showScreen(SCREEN_VICTORY);
    }


    public void showScreen(String name) {
        cardLayout.show(root, name);
    }

    // BATTLE ekranına geçmek için tek yerden kontrol edelim
    public void showBattleScreen() {
        showScreen(SCREEN_BATTLE);
    }

    public void startBattleFromLevel1() {
        battlePanel.loadLevel(0);
        showBattleScreen();
    }

    public void backToMenu() {
        showScreen(SCREEN_LOGIN);
    }

    public void openInventoryFromBattle() {
        inventoryPanel.refresh();
        showScreen(SCREEN_INVENTORY);
    }

    public void backToBattle() {
        battlePanel.refreshUI();
        showBattleScreen();
    }

    // LoginPanel burayı çağıracak: isimle yeni oyun
    public void startNewGame(String playerName) {
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Finn";
        }
        playerName = playerName.trim();

        // Yeni player oluştur
        Player newPlayer = new Player(playerName);

        // Context güncelle
        context.setPlayer(newPlayer);

        // UI panellerini yeni player’a göre tazele
        battlePanel.loadLevel(0);
        battlePanel.refreshUI();
        inventoryPanel.refresh();

        battlePanel.unlockInput();

        showBattleScreen();
    }
    public BattlePanel getBattlePanel() {
        return battlePanel;
    }
}