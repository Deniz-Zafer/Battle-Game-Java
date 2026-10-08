package proje.model.item;

import proje.model.character.Player;

/** Can iksiri. */
public class HealthPotion extends UsableItem {

    private final int healAmount;

    public HealthPotion(String name, String description, int healAmount) {
        super(name, description);
        this.healAmount = healAmount;
    }

    @Override
    public void use(Player player) {
        System.out.println(getName() + " kullanıldı. (+" + healAmount + " HP)");
        player.setHealth(player.getHealth() + healAmount);
    }

    public int getHealAmount() {
        return healAmount;
    }
}
