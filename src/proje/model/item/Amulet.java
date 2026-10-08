package proje.model.item;

import proje.model.character.Player;

public class Amulet extends EquipableItem {

    public Amulet(String name, String description,
                  int attackBonusCurrent, int attackBonusMax,
                  int defenseBonusCurrent, int defenseBonusMax,
                  int magicBonusCurrent, int magicBonusMax) {

        super(name, description,
                attackBonusCurrent, attackBonusMax,
                defenseBonusCurrent, defenseBonusMax,
                magicBonusCurrent, magicBonusMax);
    }

    public void use(Player player) {
        System.out.println("Amulet doğrudan kullanılamaz, takılmalıdır.");
    }
}
