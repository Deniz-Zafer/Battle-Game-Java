package proje.model.item;

import proje.model.character.Player;

public class StrengthPotion extends UsableItem {

    private int attackBoost;

    public StrengthPotion(String name, String description, int attackBoost) {
        super(name, description);
        this.attackBoost = attackBoost;
    }

    @Override
    public void use(Player player) {
        System.out.println(getName() + " kullanıldı. Saldırı gücün +" + attackBoost);
        player.addTemporaryAttackBonus(attackBoost);
    }

    public int getAttackBoost() {
        return attackBoost;
    }
}
