package proje.model.item;

import proje.model.character.Player;

public class ComboPotion extends UsableItem {

    private final int healAmount;
    private final int manaAmount;
    private final int attackBoost;
    private final int defenseBoost;

    public ComboPotion(String name, String description,
                       int healAmount, int manaAmount,
                       int attackBoost, int defenseBoost) {
        super(name, description);
        this.healAmount = healAmount;
        this.manaAmount = manaAmount;
        this.attackBoost = attackBoost;
        this.defenseBoost = defenseBoost;
    }

    @Override
    public void use(Player player) {
        if (healAmount > 0) {
            System.out.println(getName() + " kullanıldı. (" + healAmount + " can)");
            player.heal(healAmount);
        }
        if (manaAmount > 0) {
            System.out.println(getName() + " kullanıldı. (" + manaAmount + " mana)");
            player.setMana(player.getMana() + manaAmount);
        }
        if (attackBoost > 0) {
            System.out.println(getName() + " kullanıldı. (ATK +" + attackBoost + ")");
            player.addTemporaryAttackBonus(attackBoost);
        }
        if (defenseBoost > 0) {
            System.out.println(getName() + " kullanıldı. (DEF +" + defenseBoost + ")");
            player.addTempDefenseBonus(defenseBoost);
        }
    }

    public int getHealAmount() {
        return healAmount;
    }

    public int getManaAmount() {
        return manaAmount;
    }

    public int getAttackBoost() {
        return attackBoost;
    }

    public int getDefenseBoost() {
        return defenseBoost;
    }
}
