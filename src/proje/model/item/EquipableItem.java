package proje.model.item;

public abstract class EquipableItem extends Item {

    public enum StatType {
        NONE,
        ATTACK,
        DEFENSE,
        MAGIC
    }

    private int attackBonusCurrent;
    private int attackBonusMax;

    private int defenseBonusCurrent;
    private int defenseBonusMax;

    private int magicBonusCurrent;
    private int magicBonusMax;

    private StatType secondaryType = StatType.NONE;
    private int secondaryCurrent;
    private int secondaryMax;

    protected EquipableItem(String name, String description,
                            int attackBonusCurrent, int attackBonusMax,
                            int defenseBonusCurrent, int defenseBonusMax,
                            int magicBonusCurrent, int magicBonusMax) {

        super(name, description);
        this.attackBonusCurrent = attackBonusCurrent;
        this.attackBonusMax = attackBonusMax;
        this.defenseBonusCurrent = defenseBonusCurrent;
        this.defenseBonusMax = defenseBonusMax;
        this.magicBonusCurrent = magicBonusCurrent;
        this.magicBonusMax = magicBonusMax;
    }

    public int getAttackBonusCurrent() {
        int extra = (secondaryType == StatType.ATTACK) ? secondaryCurrent : 0;
        return attackBonusCurrent + extra;
    }

    public int getAttackBonusMax() {
        int extra = (secondaryType == StatType.ATTACK) ? secondaryMax : 0;
        return attackBonusMax + extra;
    }

    public int getDefenseBonusCurrent() {
        int extra = (secondaryType == StatType.DEFENSE) ? secondaryCurrent : 0;
        return defenseBonusCurrent + extra;
    }

    public int getDefenseBonusMax() {
        int extra = (secondaryType == StatType.DEFENSE) ? secondaryMax : 0;
        return defenseBonusMax + extra;
    }

    public int getMagicBonusCurrent() {
        int extra = (secondaryType == StatType.MAGIC) ? secondaryCurrent : 0;
        return magicBonusCurrent + extra;
    }

    public int getMagicBonusMax() {
        int extra = (secondaryType == StatType.MAGIC) ? secondaryMax : 0;
        return magicBonusMax + extra;
    }

    public StatType getPrimaryType() {
        if (attackBonusMax > 0) return StatType.ATTACK;
        if (defenseBonusMax > 0) return StatType.DEFENSE;
        if (magicBonusMax > 0) return StatType.MAGIC;
        return StatType.NONE;
    }

    public StatType getSecondaryType() {
        return secondaryType;
    }

    public boolean hasSecondary() {
        return secondaryType != StatType.NONE;
    }

    public void increaseAttackBonus(int amount) {
        attackBonusCurrent = Math.min(attackBonusCurrent + amount, attackBonusMax);
    }

    public void increaseDefenseBonus(int amount) {
        defenseBonusCurrent = Math.min(defenseBonusCurrent + amount, defenseBonusMax);
    }

    public void increaseMagicBonus(int amount) {
        magicBonusCurrent = Math.min(magicBonusCurrent + amount, magicBonusMax);
    }

    public void applyEnhancement(StatType type, int amount) {
        if (type == StatType.NONE || amount <= 0) return;

        StatType primary = getPrimaryType();

        if (!hasSecondary()) {
            if (type == primary) {
                if (isPrimaryAtMax(primary)) {
                    setSecondarySameAsPrimary(primary);
                    increaseSecondary(amount);
                } else {
                    increasePrimary(primary, amount);
                }
                return;
            }

            secondaryType = type;
            secondaryMax = Math.max(1, amount);
            secondaryCurrent = Math.max(1, amount);
            return;
        }

        if (secondaryType != type) return;
        increaseSecondary(amount);
    }

    private void setSecondarySameAsPrimary(StatType primary) {
        secondaryType = primary;
        secondaryMax = getPrimaryMax(primary);
        secondaryCurrent = 0;
    }

    private void increaseSecondary(int amount) {
        secondaryCurrent = Math.min(secondaryCurrent + amount, secondaryMax);
    }

    private void increasePrimary(StatType type, int amount) {
        if (type == StatType.ATTACK) increaseAttackBonus(amount);
        if (type == StatType.DEFENSE) increaseDefenseBonus(amount);
        if (type == StatType.MAGIC) increaseMagicBonus(amount);
    }

    private boolean isPrimaryAtMax(StatType type) {
        if (type == StatType.ATTACK) return attackBonusCurrent >= attackBonusMax;
        if (type == StatType.DEFENSE) return defenseBonusCurrent >= defenseBonusMax;
        if (type == StatType.MAGIC) return magicBonusCurrent >= magicBonusMax;
        return false;
    }

    private int getPrimaryMax(StatType type) {
        if (type == StatType.ATTACK) return attackBonusMax;
        if (type == StatType.DEFENSE) return defenseBonusMax;
        if (type == StatType.MAGIC) return magicBonusMax;
        return 0;
    }
}
