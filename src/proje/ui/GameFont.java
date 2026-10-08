package proje.ui;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

public final class GameFont {

    private static Font TYPECAST;

    static {
        try (InputStream is = GameFont.class.getResourceAsStream("/fonts/Typecast-Bold.ttf")) {

            System.out.println("Font stream = " + is); // <- null mı görücez

            if (is == null) {
                throw new RuntimeException("Font resource not found: /fonts/Typecast-Bold.ttf");
            }

            // 1) Önce base font'u oluştur
            Font base = Font.createFont(Font.TRUETYPE_FONT, is);

            // 2) Register et (base ile)
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(base);

            // 3) UI’da kullanacağın font
            TYPECAST = base.deriveFont(16f);

            System.out.println("Loaded font: " + TYPECAST.getFontName());

        } catch (Exception e) {
            e.printStackTrace(); // <- bunu mutlaka gör
            TYPECAST = new Font("Arial", Font.BOLD, 16);
        }
    }

    private GameFont() {}

    public static Font get(float size) {
        return TYPECAST.deriveFont(size);
    }
}
