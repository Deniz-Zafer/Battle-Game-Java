package proje.ui.theme;

import java.awt.*;

/**
 * Tek yerden UI renk/ölçü yönetimi.
 */
public final class UiTheme {
    private UiTheme() {}

    // Screenshot temasına uygun (mavi çizgi + beyaz zemin)
    public static final Color BG = Color.WHITE;
    public static final Color BORDER_BLUE = new Color(52, 80, 210);
    public static final Color TEXT_BLUE = new Color(25, 110, 255);

    public static final Color PANEL_FILL = new Color(255, 255, 255, 230);
    public static final Color OVERLAY_DARK = new Color(0, 0, 0, 160);

    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 16);
    public static final Font UI_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font UI_FONT_BOLD = new Font("Arial", Font.BOLD, 14);

    public static final int STROKE_THICK = 3;
    public static final int RADIUS_L = 28;
    public static final int RADIUS_M = 18;
    public static final int RADIUS_S = 14;

    public static final int PAD = 14;
}
