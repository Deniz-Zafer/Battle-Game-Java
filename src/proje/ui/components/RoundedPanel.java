package proje.ui.components;

import proje.ui.theme.UiTheme;

import javax.swing.*;
import java.awt.*;

/**
 * İç dolgulu, yuvarlatılmış köşeli panel.
 * Varsayılan: doldur + çerçeve (eski stil)
 * Yeni: doldurma ve/veya çerçeve çizimini kapatabilirsin.
 */
public class RoundedPanel extends JPanel {

    private int radius = UiTheme.RADIUS_L;
    private int stroke = UiTheme.STROKE_THICK;
    private Color borderColor = UiTheme.BORDER_BLUE;
    private Color fillColor = UiTheme.PANEL_FILL;

    private boolean drawFill = true;
    private boolean drawBorder = true;

    public RoundedPanel() {
        setOpaque(false);
    }

    public RoundedPanel(int radius) {
        this();
        this.radius = radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }

    public void setStroke(int stroke) {
        this.stroke = stroke;
        repaint();
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        repaint();
    }

    /** Kart tamamen görünmesin istiyorsan drawFill=false yap. */
    public void setDrawFill(boolean drawFill) {
        this.drawFill = drawFill;
        repaint();
    }

    /** Kartın mavi çerçevesi görünmesin istiyorsan drawBorder=false yap. */
    public void setDrawBorder(boolean drawBorder) {
        this.drawBorder = drawBorder;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Eğer ikisi de kapalıysa hiç çizme (tam görünmez kart)
        if (!drawFill && !drawBorder) return;

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int inset = Math.max(1, stroke / 2);

            // fill
            if (drawFill) {
                g2.setColor(fillColor);
                g2.fillRoundRect(inset, inset, w - 2 * inset, h - 2 * inset, radius, radius);
            }

            // border
            if (drawBorder) {
                g2.setStroke(new BasicStroke(stroke));
                g2.setColor(borderColor);
                g2.drawRoundRect(inset, inset, w - 2 * inset, h - 2 * inset, radius, radius);
            }
        } finally {
            g2.dispose();
        }
    }
}