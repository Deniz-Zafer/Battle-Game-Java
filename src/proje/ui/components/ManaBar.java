package proje.ui.components;

import javax.swing.*;
import java.awt.*;

public class ManaBar extends JPanel {

    private final JLabel nameLabel;
    private final JLabel valueLabel;
    private final JProgressBar bar;

    public ManaBar(String name, int value, int max) {
        this(name, value, max, 220, 14);
    }

    public ManaBar(String name, int value, int max, int barWidth, int barHeight) {
        setLayout(new BorderLayout(6, 0));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        // Sol etiket (MANA)
        nameLabel = new JLabel(name);

        // Sağ değer etiketi (xx/yy)
        valueLabel = new JLabel();

        // Progress bar
        bar = new JProgressBar(0, Math.max(1, max));
        bar.setPreferredSize(new Dimension(barWidth, barHeight));
        bar.setMinimumSize(new Dimension(barWidth, barHeight));
        bar.setMaximumSize(new Dimension(barWidth, barHeight));
        bar.setStringPainted(false);

        // Mana rengi (HP'den farkı burada)
        bar.setForeground(new Color(90, 140, 255));
        bar.setBackground(new Color(40, 40, 60));

        update(value, max);

        add(nameLabel, BorderLayout.WEST);
        add(bar, BorderLayout.CENTER);
        add(valueLabel, BorderLayout.EAST);
    }

    public void update(int value, int max) {
        bar.setMaximum(Math.max(1, max));
        bar.setValue(Math.max(0, Math.min(value, max)));
        valueLabel.setText(bar.getValue() + "/" + bar.getMaximum());
    }
}
