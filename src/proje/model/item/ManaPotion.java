package proje.model.item;

import proje.model.character.Player;

public class ManaPotion extends UsableItem {

    private int manaAmount;

    public ManaPotion(String name, String description, int manaAmount) {
        super(name, description);
        this.manaAmount = manaAmount;
    }

    @Override
    public void use(Player player) {
        System.out.println(getName() + " kullanıldı. (" + manaAmount + " mana)");
        player.setMana(player.getMana() + manaAmount);  // += değil, setMana ile
    }

    public int getManaAmount() {
        return manaAmount;
    }
}
