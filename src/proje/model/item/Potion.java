package proje.model.item;

import proje.model.character.Player;

public class Potion extends UsableItem {

    private int healAmount;

    public Potion(String name, String description, int healAmount) {
        super(name, description);
        this.healAmount = healAmount;
    }

    @Override
    public void use(Player player) {
        System.out.println(getName() + " kullanıldı. (" + healAmount + " can)");
        player.heal(healAmount);
    }

    public int getHealAmount() {
        return healAmount;
    }
}
