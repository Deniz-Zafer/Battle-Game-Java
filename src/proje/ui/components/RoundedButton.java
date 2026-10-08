package proje.ui.components;

import proje.ui.theme.UiTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Screenshot tarzı yuvarlatılmış buton.
 * Varsayılan: mavi çerçeve + açık dolgu.
 * Yeni: fill/border/hover renklerini dışarıdan tam kontrol edebilirsin.
 */
public class RoundedButton extends JButton {

    private int radius = UiTheme.RADIUS_M;
    private int stroke = UiTheme.STROKE_THICK;

    private Color borderColor = UiTheme.BORDER_BLUE;
    private Color fillColor = UiTheme.PANEL_FILL;
    private Color hoverFill = new Color(235, 242, 255);

    // Disabled görünümü (isteğe göre override edilebilir)
    private Color disabledFill = new Color(235, 235, 235);
    private Color disabledBorder = UiTheme.BORDER_BLUE;
    private Color disabledText = new Color(140, 140, 140);

    private boolean hover = false;
    private boolean useHover = true;

    public RoundedButton(String text) {
        super(text);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);

        // Default metin rengi
        setForeground(UiTheme.TEXT_BLUE);

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (!isEnabled() || !useHover) return;
                hover = true;
                repaint();
            }
            @Override public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    public void setRadius(int radius) { this.radius = radius; repaint(); }
    public void setStroke(int stroke) { this.stroke = stroke; repaint(); }
    public void setBorderColor(Color c) { this.borderColor = c; repaint(); }
    public void setFillColor(Color c) { this.fillColor = c; repaint(); }
    public void setHoverFill(Color c) { this.hoverFill = c; repaint(); }

    /** Hover efektini komple kapatmak istersen */
    public void setUseHover(boolean useHover) {
        this.useHover = useHover;
        if (!useHover) hover = false;
        repaint();
    }

    /** Disabled renkleri */
    public void setDisabledFill(Color c) { this.disabledFill = c; repaint(); }
    public void setDisabledBorder(Color c) { this.disabledBorder = c; repaint(); }
    public void setDisabledText(Color c) { this.disabledText = c; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int inset = Math.max(1, stroke / 2);

            boolean enabled = isEnabled();

            Color bg = enabled
                    ? (hover && useHover ? hoverFill : fillColor)
                    : disabledFill;

            Color strokeC = enabled ? borderColor : disabledBorder;

            // fill
            g2.setColor(bg);
            g2.fillRoundRect(inset, inset, w - 2 * inset, h - 2 * inset, radius, radius);

            // border
            g2.setStroke(new BasicStroke(stroke));
            g2.setColor(strokeC);
            g2.drawRoundRect(inset, inset, w - 2 * inset, h - 2 * inset, radius, radius);

            // Disabled yazı rengi otomatik
            if (!enabled) {
                super.setForeground(disabledText);
            }

            super.paintComponent(g2);
        } finally {
            g2.dispose();
        }
    }
}