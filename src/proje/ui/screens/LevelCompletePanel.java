package proje.ui.screens;

import proje.ui.GameContext;
import proje.ui.GameFrame;
import proje.ui.components.RoundedButton;
import proje.ui.components.RoundedPanel;
import proje.ui.theme.UiTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Seviye bitti ekranı (ara ekran).
 * BattlePanel içerisinden show/hide edilebilir veya CardLayout ile ayrı screen yapılabilir.
 */
public class LevelCompletePanel extends JPanel {

    private final GameFrame frame;
    private final GameContext context;

    private final JLabel title = new JLabel("SEVİYE TAMAMLANDI!", SwingConstants.CENTER);
    private final JTextArea details = new JTextArea(10, 22);


    public LevelCompletePanel(GameFrame frame, GameContext context) {
        this.frame = frame;
        this.context = context;

        setOpaque(false);
        setLayout(new GridBagLayout());

        RoundedPanel card = new RoundedPanel(UiTheme.RADIUS_L);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        title.setForeground(UiTheme.TEXT_BLUE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        details.setEditable(false);
        details.setOpaque(false);
        details.setForeground(UiTheme.TEXT_BLUE);
        details.setLineWrap(true);
        details.setWrapStyleWord(true);
        details.setText("Ödüller / Drop'lar burada listelenebilir.\n\nDevam'a basınca bir sonraki seviyeye geçebilirsin.");
        details.setPreferredSize(new Dimension(260, 240));
        details.setMaximumSize(new Dimension(260, 300));


        RoundedButton cont = new RoundedButton("Devam");
        cont.setAlignmentX(Component.CENTER_ALIGNMENT);
        cont.setPreferredSize(new Dimension(200, 46));
        cont.setMaximumSize(new Dimension(200, 46));
        cont.addActionListener(e -> frame.showBattleScreen());

        RoundedButton menu = new RoundedButton("Menü");
        menu.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.setPreferredSize(new Dimension(200, 46));
        menu.setMaximumSize(new Dimension(200, 46));
        menu.addActionListener(e -> frame.backToMenu());

        card.add(title);
        card.add(Box.createVerticalStrut(12));
        card.add(details);
        card.add(Box.createVerticalStrut(14));
        card.add(cont);
        card.add(Box.createVerticalStrut(10));
        card.add(menu);

        add(card, new GridBagConstraints());
    }

    public void setDetailsText(String text) {
        details.setText(text);
    }
}
