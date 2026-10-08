package proje.ui.components;

import proje.model.item.*;

/**
 * Item -> PNG resource path eşlemesi.
 *
 * PNG'leri şu dizine koy: resources/items/
 *
 * Örn: resources/items/Sword_1.png
 */
public final class ItemIconResolver {

    private ItemIconResolver() {}

    public static String iconPath(Item item) {
        if (item == null) return null;

        // --- Weapons ---
        if (item instanceof Weapon w) {
            return "/items/Sword_" + clampLv(w.getLevel()) + ".png";
        }
        if (item instanceof Shield s) {
            return "/items/Shield_" + clampLv(s.getLevel()) + ".png";
        }
        if (item instanceof Staff st) {
            return "/items/Staff_" + clampLv(st.getLevel()) + ".png";
        }

        // --- Potions ---
        if (item instanceof StrengthPotion) {
            return "/items/Str_Pot_" + clampLv(levelFromName(item.getName())) + ".png";
        }
        if (item instanceof ManaPotion) {
            return "/items/Mana_Pot_" + clampLv(levelFromName(item.getName())) + ".png";
        }
        // Mevcut projede health potion sınıfı Potion
        if (item instanceof Potion) {
            return "/items/HP_Pot_" + clampLv(levelFromName(item.getName())) + ".png";
        }
        if (item instanceof HealthPotion) {
            return "/items/HP_Pot_" + clampLv(levelFromName(item.getName())) + ".png";
        }
        if (item instanceof DefensePotion) {
            return "/items/Def_Pot_" + clampLv(levelFromName(item.getName())) + ".png";
        }

        // --- Amulets ---
        if (item instanceof Amulet a) {
            int lv = clampLv(levelFromName(a.getName()));
            // bonus'a göre ikon seç
            if (a.getAttackBonusCurrent() > 0) return "/items/Str_Amulet_" + lv + ".png";
            if (a.getDefenseBonusCurrent() > 0) return "/items/Def_Amulet_" + lv + ".png";
            if (a.getMagicBonusCurrent() > 0) return "/items/Mana_Amulet_" + lv + ".png";
            // fallback
            return "/items/Mana_Amulet_" + lv + ".png";
        }

        // Fallback: bilinmeyen item
        return null;
    }

    private static int clampLv(int lv) {
        if (lv <= 0) return 1;
        if (lv > 3) return 3;
        return lv;
    }

    /**
     * İsimden "Lv1" / "Lv2" / "Lv3" ayıklar. Bulamazsa 1.
     */
    public static int levelFromName(String name) {
        if (name == null) return 1;
        String n = name.toLowerCase();
        int idx = n.indexOf("lv");
        if (idx < 0) return 1;
        // Lv sonrası rakamı oku
        for (int i = idx + 2; i < n.length(); i++) {
            char c = n.charAt(i);
            if (Character.isDigit(c)) {
                return c - '0';
            }
        }
        return 1;
    }
}
