package proje.model.item;

import proje.model.character.Player;

public class Staff extends EquipableItem {

    private int level;

    public Staff(String name, String description, int level,
                 int magicBonusCurrent, int magicBonusMax) {

        super(name, description,
                0, 0,
                0, 0,
                magicBonusCurrent, magicBonusMax);

        this.level = level;
    }

    public void use(Player player) {
        System.out.println("Asa doğrudan kullanılamaz, takılmalıdır.");
    }

    public int getLevel() {
        return level;
    }
}
