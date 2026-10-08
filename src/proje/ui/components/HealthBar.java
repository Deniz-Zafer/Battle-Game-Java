package proje.ui.components;

import javax.swing.*;
import java.awt.*;

public class HealthBar extends JPanel {

    private final JLabel nameLabel = new JLabel("", SwingConstants.LEFT);
    private final JProgressBar bar;
    private final JLabel valueLabel = new JLabel("", SwingConstants.RIGHT);

    private int max;

    public HealthBar(String name, int current, int max) {
        this(name, current, max, 220, 20);
    }

    public HealthBar(String name, int current, int max, int barWidth, int barHeight) {
        this.max = max;

        setLayout(new BorderLayout(6, 0));
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        nameLabel.setText(name);

        bar = new JProgressBar(0, Math.max(1, max));
        bar.setPreferredSize(new Dimension(barWidth, barHeight));
        bar.setMinimumSize(new Dimension(barWidth, barHeight));
        bar.setValue(current);
        bar.setStringPainted(false);

        update(current, max);

        add(nameLabel, BorderLayout.WEST);
        add(bar, BorderLayout.CENTER);
        add(valueLabel, BorderLayout.EAST);
    }

    public void update(int value, int max) {
        bar.setMaximum(max);
        bar.setValue(value);

        valueLabel.setText(value + "/" + max);

        double ratio = (double) value / max;

        if (ratio > 0.7) {
            bar.setForeground(Color.GREEN);
        } else if (ratio > 0.3) {
            bar.setForeground(Color.ORANGE);
        } else {
            bar.setForeground(Color.RED);
        }
    }
}
