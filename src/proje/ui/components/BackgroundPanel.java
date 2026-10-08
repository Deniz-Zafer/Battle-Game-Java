package proje.ui.components;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image image;

    public BackgroundPanel(Image image) {
        this.image = image;
        setLayout(new BorderLayout());
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int w = getWidth();
        int h = getHeight();

        int iw = image.getWidth(null);
        int ih = image.getHeight(null);
        if (iw <= 0 || ih <= 0) { g2.dispose(); return; }

        // Cover: ekranı tamamen doldur
        double scale = Math.max((double) w / iw, (double) h / ih);

        int dw = (int) (iw * scale);
        int dh = (int) (ih * scale);

        int x = (w - dw) / 2;
        int y = (h - dh) / 2;

        g2.drawImage(image, x, y, dw, dh, null);
        g2.dispose();
    }
}
