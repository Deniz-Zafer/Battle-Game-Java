package proje.ui.screens;

import proje.ui.GameContext;
import proje.ui.GameFrame;
import proje.ui.components.BackgroundPanel;
import proje.ui.components.RoundedButton;
import proje.ui.components.RoundedPanel;
import proje.ui.theme.UiTheme;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class LoginPanel extends JPanel {

    private final GameFrame frame;
    private final GameContext context;

    private final JTextField nameField = new JTextField();

    public LoginPanel(GameFrame frame, GameContext context) {
        this.frame = frame;
        this.context = context;

        setLayout(new BorderLayout());

        BackgroundPanel bg = createBackground("/menu/menu.png");
        add(bg, BorderLayout.CENTER);
        bg.setLayout(new GridBagLayout());

        // ===== Palette (parşömen/kahverengi) =====
        Color darkText = new Color(255, 255, 255);
        Color parchment = new Color(210, 190, 164);
        Color parchmentHover = new Color(210, 190, 164);
        Color borderBrown = new Color(150, 120, 70);
        Color disabledText = new Color(140, 140, 140);

        // ===== Card (RoundedPanel ama görünmez) =====
        RoundedPanel card = new RoundedPanel(UiTheme.RADIUS_L);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // RoundedPanel kendi çizdiği için: gerçekten görünmez yapmak için bunlar şart
        card.setDrawFill(false);
        card.setDrawBorder(false);

        // İçerik boşlukları
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setOpaque(false);
        card.setBackground(new Color(0, 0, 0, 0));

        // ===== Buttons (önce oluştur: font referansı) =====
        RoundedButton start = new RoundedButton("Maceraya Başla");
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.setPreferredSize(new Dimension(200, 40));
        start.setMaximumSize(new Dimension(200, 40));
        start.setEnabled(false);

        RoundedButton exit = new RoundedButton("Çıkış");
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.setPreferredSize(new Dimension(200, 40));
        exit.setMaximumSize(new Dimension(200, 40));

        // ===== Tüm fontlar start fontu ile aynı olsun =====
        Font baseFont = start.getFont().deriveFont(Font.BOLD, 26f);

        JLabel subtitle = new JLabel("Kahraman adını gir");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(baseFont);
        subtitle.setForeground(darkText);

        // ===== Name field =====
        nameField.setHorizontalAlignment(SwingConstants.CENTER);
        nameField.setPreferredSize(new Dimension(420, 50));
        nameField.setMaximumSize(new Dimension(420, 50));
        nameField.setFont(baseFont);
        nameField.setForeground(darkText);
        nameField.setCaretColor(borderBrown);
        nameField.setOpaque(true);
        nameField.setBackground(parchment);
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderBrown, 2, true),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));

        // ===== Button styling (RoundedButton artık kendi renklerini kullanıyor) =====
        start.setFont(baseFont);
        start.setFillColor(parchment);
        start.setBorderColor(borderBrown);
        start.setHoverFill(parchmentHover);
        start.setForeground(darkText);

        // disabled durumda da parşömen kalsın, yazı soluk olsun
        start.setDisabledFill(parchment);
        start.setDisabledBorder(borderBrown);
        start.setDisabledText(disabledText);

        exit.setFont(baseFont);
        exit.setFillColor(parchment);
        exit.setBorderColor(borderBrown);
        exit.setHoverFill(parchmentHover);
        exit.setForeground(darkText);

        // ===== Enable/disable start based on name =====
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                boolean ok = !nameField.getText().trim().isEmpty();
                start.setEnabled(ok);
                if (ok) start.setForeground(darkText);
            }
            @Override public void insertUpdate(DocumentEvent e) { update(); }
            @Override public void removeUpdate(DocumentEvent e) { update(); }
            @Override public void changedUpdate(DocumentEvent e) { update(); }
        });

        // Enter ile başlat
        nameField.addActionListener(e -> tryStart());
        start.addActionListener(e -> tryStart());
        exit.addActionListener(e -> System.exit(0));

        // ===== Layout spacing =====
        card.add(subtitle);
        card.add(Box.createVerticalStrut(14));
        card.add(nameField);
        card.add(Box.createVerticalStrut(22));
        card.add(start);
        card.add(Box.createVerticalStrut(7));
        card.add(exit);

        // Konum
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(-225, 0, 0, 0);

        bg.add(card, gbc);
    }

    private BackgroundPanel createBackground(String resourcePath) {
        URL bgUrl = getClass().getResource(resourcePath);

        if (bgUrl == null) {
            setBackground(UiTheme.BG);
            return new BackgroundPanel(null);
        }

        try {
            BufferedImage img = ImageIO.read(bgUrl);
            return new BackgroundPanel(img);
        } catch (IOException e) {
            e.printStackTrace();
            setBackground(UiTheme.BG);
            return new BackgroundPanel(null);
        }
    }

    private void tryStart() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) return;
        frame.startNewGame(name);
    }
}