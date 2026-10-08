package proje.ui.components;

import proje.ui.theme.UiTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Screenshot'taki küçük slot kutuları (equipped items / amulets).
 * İster item icon'u koy, ister sadece boş bırak.
 */
public class SlotView extends JComponent {

    private int radius = UiTheme.RADIUS_S;
    private int stroke = UiTheme.STROKE_THICK;
    private Color borderColor = UiTheme.BORDER_BLUE;
    private Color fillColor = UiTheme.PANEL_FILL;

    private Image icon;

    public SlotView() {
        setOpaque(false);

        Dimension d = new Dimension(60, 50);
        setPreferredSize(d);
        setMinimumSize(d);
        setMaximumSize(d);
    }


    public void setIconImage(Image icon) {
        this.icon = icon;
        repaint();
    }

    public void clear() {
        this.icon = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            int inset = Math.max(1, stroke / 2);

            g2.setColor(fillColor);
            g2.fillRoundRect(inset, inset, w - 2 * inset, h - 2 * inset, radius, radius);

            g2.setStroke(new BasicStroke(stroke));
            g2.setColor(borderColor);
            g2.drawRoundRect(inset, inset, w - 2 * inset, h - 2 * inset, radius, radius);

            if (icon != null) {
                int pad = 8;
                int iw = w - 2 * (inset + pad);
                int ih = h - 2 * (inset + pad);
                g2.drawImage(icon, inset + pad, inset + pad, iw, ih, this);
            }
        } finally {
            g2.dispose();
        }
    }
}
