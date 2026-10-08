package proje.ui.components;

import proje.model.combat.ElementType;
import proje.model.item.*;

/**
 * HTML tooltip üretir (renkli statlar).
 * ATK kırmızı, DEF yeşil, MANA mavi, Element turuncu.
 */
public final class ItemTooltip {

    private ItemTooltip() {}

    public static String build(Item item) {
        if (item == null) return "Empty";

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<b>").append(escape(item.getName())).append("</b><br>");

        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            sb.append("<span style='color:#777777;'>").append(escape(item.getDescription())).append("</span><br>");
        }

        if (item instanceof Weapon w) {
            stat(sb, "ATK", w.getAttackBonusCurrent(), "#ff3b30");
            if (w.getElementalDamageCurrent() > 0) {
                sb.append("<span style='color:#ff9500;'>ELEMENT DMG: ")
                        .append(w.getElementalDamageCurrent()).append("</span><br>");
            }
            ElementType el = w.getElementType();
            if (el != null) {
                sb.append("<span style='color:#ff9500;'>ELEMENT: ").append(el).append("</span><br>");
            }
        } else if (item instanceof Shield sh) {
            stat(sb, "DEF", sh.getDefenseBonusCurrent(), "#34c759");
            if (sh.getElementalResistCurrent() > 0) {
                sb.append("<span style='color:#ff9500;'>RESIST: ")
                        .append(sh.getElementalResistCurrent()).append("</span><br>");
            }
            ElementType el = sh.getElementType();
            if (el != null) {
                sb.append("<span style='color:#ff9500;'>ELEMENT: ").append(el).append("</span><br>");
            }
        } else if (item instanceof Staff st) {
            stat(sb, "MANA", st.getMagicBonusCurrent(), "#0a84ff");
            stat(sb, "ATK", st.getAttackBonusCurrent(), "#ff3b30");
        } else if (item instanceof Amulet a) {
            if (a.getAttackBonusCurrent() > 0) stat(sb, "ATK", a.getAttackBonusCurrent(), "#ff3b30");
            if (a.getDefenseBonusCurrent() > 0) stat(sb, "DEF", a.getDefenseBonusCurrent(), "#34c759");
            if (a.getMagicBonusCurrent() > 0) stat(sb, "MANA", a.getMagicBonusCurrent(), "#0a84ff");
        } else if (item instanceof StrengthPotion sp) {
            stat(sb, "ATK", sp.getAttackBoost(), "#ff3b30");
        } else if (item instanceof ManaPotion mp) {
            stat(sb, "MANA", mp.getManaAmount(), "#0a84ff");
        } else if (item instanceof Potion pot) {
            sb.append("<span style='color:#ff2d55;'>HP: +").append(pot.getHealAmount()).append("</span><br>");
        } else if (item instanceof HealthPotion hp) {
            sb.append("<span style='color:#ff2d55;'>HP: +").append(hp.getHealAmount()).append("</span><br>");
        } else if (item instanceof DefensePotion dp) {
            stat(sb, "DEF", dp.getDefenseBoost(), "#34c759");
        }

        sb.append("</html>");
        return sb.toString();
    }

    private static void stat(StringBuilder sb, String name, int value, String color) {
        sb.append("<span style='color:").append(color).append(";'>")
                .append(name).append(": +").append(value)
                .append("</span><br>");
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
