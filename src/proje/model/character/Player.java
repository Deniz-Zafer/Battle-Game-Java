package proje.model.character;

import proje.model.combat.ElementCalculator;
import proje.model.combat.ElementType;
import proje.model.inventory.Inventory;
import proje.model.item.*;

public class Player extends Character {

    private int mana;
    private int maxMana;
    private int experience;
    private int level;
    private Inventory<Item> inventory;

    private EquipableItem equipSlot1;
    private EquipableItem equipSlot2;

    private Amulet amuletSlot1;
    private Amulet amuletSlot2;

    private int temporaryAttackBonus;
    private int baseMagicPower;

    private boolean defending;

    public Player(String name) {
        super(name, 100, 15, 5);
        this.maxMana = 50;
        this.mana = maxMana;
        this.level = 1;
        this.experience = 0;

        this.inventory = new Inventory<>();
        this.baseMagicPower = 10;
        this.temporaryAttackBonus = 0;
    }

    public int getTotalAttackPower() {
        int total = getAttackPower() + temporaryAttackBonus;

        if (equipSlot1 != null) total += equipSlot1.getAttackBonusCurrent();
        if (equipSlot2 != null) total += equipSlot2.getAttackBonusCurrent();
        if (amuletSlot1 != null) total += amuletSlot1.getAttackBonusCurrent();
        if (amuletSlot2 != null) total += amuletSlot2.getAttackBonusCurrent();

        return total;
    }

    @Override
    public int getTotalDefense() {
        int total = super.getTotalDefense();
        if (equipSlot1 != null) total += equipSlot1.getDefenseBonusCurrent();
        if (equipSlot2 != null) total += equipSlot2.getDefenseBonusCurrent();
        if (amuletSlot1 != null) total += amuletSlot1.getDefenseBonusCurrent();
        if (amuletSlot2 != null) total += amuletSlot2.getDefenseBonusCurrent();
        return total;
    }

    public int getTotalMagicPower() {
        int total = baseMagicPower;
        if (equipSlot1 != null) total += equipSlot1.getMagicBonusCurrent();
        if (equipSlot2 != null) total += equipSlot2.getMagicBonusCurrent();
        if (amuletSlot1 != null) total += amuletSlot1.getMagicBonusCurrent();
        if (amuletSlot2 != null) total += amuletSlot2.getMagicBonusCurrent();
        return total;
    }

    public void addTemporaryAttackBonus(int amount) {
        this.temporaryAttackBonus += amount;
    }

    @Override
    public void attack(Character target) {

        Weapon weapon = null;
        if (equipSlot1 instanceof Weapon) weapon = (Weapon) equipSlot1;
        else if (equipSlot2 instanceof Weapon) weapon = (Weapon) equipSlot2;

        if (weapon == null) {
            int dmg = getTotalAttackPower();
            System.out.println(getName() + " silahsız saldırıyor! Hasar: " + dmg);
            target.takeDamage(dmg);
            return;
        }

        ElementType weaponElement = weapon.getElementType();
        ElementType enemyElement = target.getElementType();

        int normalDamage = getTotalAttackPower() + weapon.getElementalDamageCurrent();
        double multiplier = ElementCalculator.getAttackMultiplier(weaponElement, enemyElement);
        int finalDamage = (int) Math.round(normalDamage * multiplier);

        System.out.println("=== Fiziksel Saldırı ===");
        System.out.println(getName() + " saldırıyor!");
        System.out.println("Silah: " + weapon.getName() + " (Element: " + weaponElement + ")");
        System.out.println("Düşman Elementi: " + enemyElement);
        System.out.println("Normal Hasar: " + normalDamage);
        System.out.println("Çarpan: " + multiplier);
        System.out.println("Gerçekleşen Hasar: " + finalDamage);

        target.takeDamage(finalDamage);
    }

    @Override
    public void useSkill(Character target) {
        int skillCost = 15;
        if (mana < skillCost) {
            System.out.println("Yeterli mana yok! (" + mana + "/" + skillCost + ")");
            return;
        }
        mana -= skillCost;

        int magic = getTotalMagicPower();
        int skillDamage = magic * 2;

        System.out.println(getName() + " güçlü bir büyü kullandı! (Büyü gücü: " + magic + ", hasar: " + skillDamage + ")");
        target.takeDamage(skillDamage);
    }

    public void gainExperience(int amount) {
        this.experience += amount;
        System.out.println(getName() + " " + amount + " XP kazandı. (Toplam: " + experience + ")");
        while (experience >= 100) {
            experience -= 100;
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        setMaxHealth(getMaxHealth() + 10);
        setHealth(getMaxHealth());
        setAttackPower(getAttackPower() + 3);
        System.out.println("Seviye atladın! Yeni seviye: " + level);
        printStatus();
    }

    public Inventory<Item> getInventory() {
        return inventory;
    }

    public void addItem(Item item) {
        inventory.addItem(item);
    }

    public boolean useFirstHealthPotion() {
        for (Item item : inventory.getItems()) {
            boolean ok =
                    (item instanceof Potion) ||
                            (item instanceof ComboPotion c && c.getHealAmount() > 0);

            if (ok) {
                if (item instanceof UsableItem u) {
                    u.use(this);
                    inventory.removeItem(item);
                    return true;
                }
            }
        }
        System.out.println("Hiç can iksirin yok!");
        return false;
    }

    public boolean useFirstManaPotion() {
        for (Item item : inventory.getItems()) {
            boolean ok =
                    (item instanceof ManaPotion) ||
                            (item instanceof ComboPotion c && c.getManaAmount() > 0);

            if (ok) {
                if (item instanceof UsableItem u) {
                    u.use(this);
                    inventory.removeItem(item);
                    return true;
                }
            }
        }
        System.out.println("Hiç mana iksirin yok!");
        return false;
    }

    public boolean useFirstStrengthPotion() {
        for (Item item : inventory.getItems()) {
            boolean ok =
                    (item instanceof StrengthPotion) ||
                            (item instanceof ComboPotion c && c.getAttackBoost() > 0);

            if (ok) {
                if (item instanceof UsableItem u) {
                    u.use(this);
                    inventory.removeItem(item);
                    return true;
                }
            }
        }
        System.out.println("Hiç strength iksirin yok!");
        return false;
    }

    public void defendThisTurn() {
        int baseBonus = 3;
        Shield shield = getEquippedShield();

        clearTempDefenseBonus();

        if (shield != null) {
            int bonus = shield.getDefenseBonusCurrent() * 2;
            addTempDefenseBonus(bonus);
            System.out.println("Kalkanla savunma! +" + bonus + " geçici DEF (1 tur)");
        } else {
            addTempDefenseBonus(baseBonus);
            System.out.println("Temel savunma! +" + baseBonus + " geçici DEF (1 tur)");
        }
    }

    public Shield getEquippedShield() {
        if (equipSlot1 instanceof Shield) return (Shield) equipSlot1;
        if (equipSlot2 instanceof Shield) return (Shield) equipSlot2;
        return null;
    }

    public void receiveElementalAttack(int baseDamage, ElementType attackerElement) {

        int defenseTotal = getTotalDefense();
        Shield shield = getEquippedShield();

        int damage = baseDamage - defenseTotal;
        if (damage < 0) damage = 0;

        ElementType shieldElement = ElementType.NEUTRAL;
        int elementalResist = 0;

        if (shield != null) {
            shieldElement = shield.getElementType();
            elementalResist = shield.getElementalResistCurrent();
        }

        double defenseMultiplier = ElementCalculator.getDefenseMultiplier(shieldElement, attackerElement);
        damage = (int) Math.round(damage * defenseMultiplier);

        damage -= elementalResist;
        if (damage < 0) damage = 0;

        System.out.println("=== Gelen Saldırı ===");
        System.out.println("Saldıran element: " + attackerElement);
        System.out.println("Kalkan elementin: " + shieldElement);
        System.out.println("Başlangıç hasar: " + baseDamage);
        System.out.println("Toplam defans (temp dahil): " + defenseTotal);
        System.out.println("Element çarpanı: " + defenseMultiplier);
        System.out.println("Elemental direnç: " + elementalResist);
        System.out.println("Uygulanan son hasar: " + damage);

        setHealth(getHealth() - damage);
        System.out.println(getName() + " " + damage + " hasar aldı. Kalan can: " + getHealth());
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        if (mana < 0) mana = 0;
        if (mana > maxMana) mana = maxMana;
        this.mana = mana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        if (maxMana <= 0) maxMana = 1;
        this.maxMana = maxMana;
        if (mana > maxMana) mana = maxMana;
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public void equipItem(EquipableItem item) {
        if (item instanceof Amulet amulet) equipAmulet(amulet);
        else equipGear(item);
    }

    public EquipableItem getEquipSlot(int idx) {
        return idx == 0 ? equipSlot1 : equipSlot2;
    }

    public Amulet getAmuletSlot(int idx) {
        return idx == 0 ? amuletSlot1 : amuletSlot2;
    }

    public EquipableItem setEquipSlot(int idx, EquipableItem item) {
        if (idx == 0) {
            EquipableItem prev = equipSlot1;
            equipSlot1 = item;
            return prev;
        } else {
            EquipableItem prev = equipSlot2;
            equipSlot2 = item;
            return prev;
        }
    }

    private void recalcMaxMana() {
        // Temel max mana (amuletsiz)
        int base = 50;

        int bonus = 0;
        if (amuletSlot1 != null) bonus += amuletSlot1.getMagicBonusCurrent();
        if (amuletSlot2 != null) bonus += amuletSlot2.getMagicBonusCurrent();

        this.maxMana = Math.max(1, base + bonus);

        // mevcut mana yeni max'ı aşmasın
        if (mana > maxMana) mana = maxMana;
    }


    public Amulet setAmuletSlot(int idx, Amulet amulet) {
        if (idx == 0) {
            Amulet prev = amuletSlot1;
            amuletSlot1 = amulet;
            recalcMaxMana();
            return prev;
        } else {
            Amulet prev = amuletSlot2;
            amuletSlot2 = amulet;
            recalcMaxMana();
            return prev;
        }
    }

    public EquipableItem clearEquipSlot(int idx) {
        if (idx == 0) {
            EquipableItem prev = equipSlot1;
            equipSlot1 = null;
            return prev;
        } else {
            EquipableItem prev = equipSlot2;
            equipSlot2 = null;
            return prev;
        }
    }

    public Amulet clearAmuletSlot(int idx) {
        if (idx == 0) {
            Amulet prev = amuletSlot1;
            amuletSlot1 = null;
            recalcMaxMana();
            return prev;
        } else {
            Amulet prev = amuletSlot2;
            amuletSlot2 = null;
            recalcMaxMana();
            return prev;
        }
    }

    public boolean isDefending() {
        return defending;
    }

    public void setDefending(boolean defending) {
        this.defending = defending;
    }

    private void equipAmulet(Amulet amulet) {
        if (amuletSlot1 == null) {
            amuletSlot1 = amulet;
            System.out.println(amulet.getName() + " 1. amulet slotuna takıldı.");
        } else if (amuletSlot2 == null) {
            amuletSlot2 = amulet;
            System.out.println(amulet.getName() + " 2. amulet slotuna takıldı.");
        } else {
            System.out.println("Tüm amulet slotları dolu. 1. slottaki " +
                    amuletSlot1.getName() + " çıkarıldı, yerine " + amulet.getName() + " takıldı.");
            amuletSlot1 = amulet;
        }

        recalcMaxMana();
    }


    private void equipGear(EquipableItem gear) {
        if (equipSlot1 == null) {
            equipSlot1 = gear;
            System.out.println(gear.getName() + " 1. ekipman slotuna takıldı.");
        } else if (equipSlot2 == null) {
            equipSlot2 = gear;
            System.out.println(gear.getName() + " 2. ekipman slotuna takıldı.");
        } else {
            System.out.println("Her iki ekipman slotu dolu. 1. slot değiştiriliyor.");
            equipSlot1 = gear;
        }
    }
}
