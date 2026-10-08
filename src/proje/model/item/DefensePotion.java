package proje.model.item;

import proje.model.character.Player;

/** Geçici defans iksiri. */
public class DefensePotion extends UsableItem {

    private final int defenseBoost;

    public DefensePotion(String name, String description, int defenseBoost) {
        super(name, description);
        this.defenseBoost = defenseBoost;
    }

    @Override
    public void use(Player player) {
        System.out.println(getName() + " kullanıldı. Defans +" + defenseBoost + " (geçici)");
        player.addTempDefenseBonus(defenseBoost);
    }

    public int getDefenseBoost() {
        return defenseBoost;
    }
}
