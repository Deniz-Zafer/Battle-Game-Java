package proje.service;

import proje.model.combat.ElementType;
import proje.model.item.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemFactory {

    private static final Random RANDOM = new Random();
    private static final List<Item> ITEM_POOL = new ArrayList<>();

    static {
        ITEM_POOL.add(new Potion("Can İksiri Lv1", "25 can yeniler", 25));
        ITEM_POOL.add(new Potion("Can İksiri Lv2", "40 can yeniler", 40));
        ITEM_POOL.add(new Potion("Can İksiri Lv3", "70 can yeniler", 70));

        ITEM_POOL.add(new ManaPotion("Mana İksiri Lv1", "20 mana yeniler", 20));
        ITEM_POOL.add(new ManaPotion("Mana İksiri Lv2", "35 mana yeniler", 35));
        ITEM_POOL.add(new ManaPotion("Mana İksiri Lv3", "60 mana yeniler", 60));

        ITEM_POOL.add(new StrengthPotion("Güç İksiri Lv1", "Saldırı +3", 3));
        ITEM_POOL.add(new StrengthPotion("Güç İksiri Lv2", "Saldırı +5", 5));
        ITEM_POOL.add(new StrengthPotion("Güç İksiri Lv3", "Saldırı +8", 8));

        ITEM_POOL.add(new DefensePotion("Defans İksiri Lv1", "Defans +2", 2));
        ITEM_POOL.add(new DefensePotion("Defans İksiri Lv2", "Defans +4", 4));
        ITEM_POOL.add(new DefensePotion("Defans İksiri Lv3", "Defans +6", 6));

        ITEM_POOL.add(new Weapon("Kılıç Lv1", "Başlangıç seviye kılıç", 1,
                3, 8,
                ElementType.NEUTRAL,
                0, 10));

        ITEM_POOL.add(new Weapon("Kılıç Lv2", "Orta seviye kılıç", 2,
                6, 12,
                ElementType.NEUTRAL,
                0, 15));

        ITEM_POOL.add(new Weapon("Kılıç Lv3", "Yüksek seviye kılıç", 3,
                10, 18,
                ElementType.NEUTRAL,
                0, 20));

        ITEM_POOL.add(new Shield("Kalkan Lv1", "Basit bir kalkan", 1,
                3, 8,
                ElementType.NEUTRAL,
                0, 10));

        ITEM_POOL.add(new Shield("Kalkan Lv2", "Orta seviye kalkan", 2,
                6, 12,
                ElementType.NEUTRAL,
                0, 15));

        ITEM_POOL.add(new Shield("Kalkan Lv3", "Yüksek seviye kalkan", 3,
                10, 18,
                ElementType.NEUTRAL,
                0, 20));

        ITEM_POOL.add(new Staff("Asa Lv1", "Basit büyü asası", 1,
                4, 8));

        ITEM_POOL.add(new Staff("Asa Lv2", "Güçlü büyü asası", 2,
                7, 14));

        ITEM_POOL.add(new Staff("Asa Lv3", "Efsanevi büyü asası", 3,
                11, 20));

        ITEM_POOL.add(new Amulet("Güç Muskası Lv1", "ATK +2", 2, 6, 0, 0, 0, 0));
        ITEM_POOL.add(new Amulet("Güç Muskası Lv2", "ATK +4", 4, 10, 0, 0, 0, 0));
        ITEM_POOL.add(new Amulet("Güç Muskası Lv3", "ATK +6", 6, 14, 0, 0, 0, 0));

        ITEM_POOL.add(new Amulet("Defans Muskası Lv1", "DEF +2", 0, 0, 2, 6, 0, 0));
        ITEM_POOL.add(new Amulet("Defans Muskası Lv2", "DEF +4", 0, 0, 4, 10, 0, 0));
        ITEM_POOL.add(new Amulet("Defans Muskası Lv3", "DEF +6", 0, 0, 6, 14, 0, 0));

        ITEM_POOL.add(new Amulet("Mana Muskası Lv1", "MANA +2", 0, 0, 0, 0, 2, 6));
        ITEM_POOL.add(new Amulet("Mana Muskası Lv2", "MANA +4", 0, 0, 0, 0, 4, 10));
        ITEM_POOL.add(new Amulet("Mana Muskası Lv3", "MANA +6", 0, 0, 0, 0, 6, 14));
    }

    public static Item getRandomItem() {
        if (ITEM_POOL.isEmpty()) {
            System.out.println("UYARI: ITEM_POOL boş, ödül üretilemiyor.");
            return null;
        }
        Item base = ITEM_POOL.get(RANDOM.nextInt(ITEM_POOL.size()));
        return cloneItem(base);
    }

    public static Item getRandomItemForLevel(int levelNumber) {
        if (ITEM_POOL.isEmpty()) {
            System.out.println("UYARI: ITEM_POOL boş, ödül üretilemiyor.");
            return null;
        }

        int allowedTier;
        if (levelNumber <= 2) allowedTier = 1;
        else if (levelNumber == 3) allowedTier = 2;
        else allowedTier = 3;

        List<Item> candidates = new ArrayList<>();
        for (Item it : ITEM_POOL) {
            int tier = extractTier(it);
            if (tier <= allowedTier) {
                candidates.add(it);
            }
        }

        if (candidates.isEmpty()) {
            return getRandomItem();
        }

        Item base = candidates.get(RANDOM.nextInt(candidates.size()));
        return cloneItem(base);
    }

    private static int extractTier(Item item) {
        if (item instanceof Weapon w) return w.getLevel();
        if (item instanceof Shield s) return s.getLevel();
        if (item instanceof Staff st) return st.getLevel();

        String n = item.getName();
        int lv = parseLvFromName(n);
        return lv > 0 ? lv : 1;
    }

    private static int parseLvFromName(String name) {
        if (name == null) return -1;
        int idx = name.lastIndexOf("Lv");
        if (idx < 0) idx = name.lastIndexOf("LV");
        if (idx < 0) return -1;
        String num = name.substring(idx + 2).trim();
        try {
            return Integer.parseInt(num);
        } catch (Exception e) {
            return -1;
        }
    }

    private static Item cloneItem(Item base) {
        if (base instanceof Potion p) {
            return new Potion(p.getName(), p.getDescription(), p.getHealAmount());
        }
        if (base instanceof ManaPotion m) {
            return new ManaPotion(m.getName(), m.getDescription(), m.getManaAmount());
        }
        if (base instanceof StrengthPotion s) {
            return new StrengthPotion(s.getName(), s.getDescription(), s.getAttackBoost());
        }
        if (base instanceof DefensePotion d) {
            return new DefensePotion(d.getName(), d.getDescription(), d.getDefenseBoost());
        }
        if (base instanceof Weapon w) {
            return new Weapon(
                    w.getName(), w.getDescription(), w.getLevel(),
                    w.getAttackBonusCurrent(), w.getAttackBonusMax(),
                    w.getElementType(),
                    w.getElementalDamageCurrent(), w.getElementalDamageMax()
            );
        }
        if (base instanceof Shield sh) {
            return new Shield(
                    sh.getName(), sh.getDescription(), sh.getLevel(),
                    sh.getDefenseBonusCurrent(), sh.getDefenseBonusMax(),
                    sh.getElementType(),
                    sh.getElementalResistCurrent(), sh.getElementalResistMax()
            );
        }
        if (base instanceof Staff st) {
            return new Staff(
                    st.getName(), st.getDescription(), st.getLevel(),
                    st.getMagicBonusCurrent(), st.getMagicBonusMax()
            );
        }
        if (base instanceof Amulet a) {
            return new Amulet(
                    a.getName(), a.getDescription(),
                    a.getAttackBonusCurrent(), a.getAttackBonusMax(),
                    a.getDefenseBonusCurrent(), a.getDefenseBonusMax(),
                    a.getMagicBonusCurrent(), a.getMagicBonusMax()
            );
        }
        return base;
    }
}
