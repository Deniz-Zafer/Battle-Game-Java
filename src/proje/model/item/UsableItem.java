package proje.model.item;

import proje.model.character.Player;

public abstract class UsableItem extends Item {

    public UsableItem(String name, String description) {
        super(name, description);
    }

    public abstract void use(Player player);
}
