package proje.model.item;

import proje.model.character.Player;
import proje.model.combat.ElementType;

public class Weapon extends EquipableItem {

    private int level;

    private ElementType elementType;
    private int elementalDamageCurrent;
    private int elementalDamageMax;

    public Weapon(String name, String description, int level,
                  int attackBonusCurrent, int attackBonusMax,
                  ElementType elementType,
                  int elementalDamageCurrent, int elementalDamageMax) {

        super(name, description,
                attackBonusCurrent, attackBonusMax,
                0, 0,
                0, 0);

        this.level = level;
        this.elementType = elementType;
        this.elementalDamageCurrent = elementalDamageCurrent;
        this.elementalDamageMax = elementalDamageMax;
    }

    public void use(Player player) {
        System.out.println("Silah doğrudan kullanılamaz, takılmalıdır.");
    }

    public int getLevel() {
        return level;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }

    public int getElementalDamageCurrent() {
        return elementalDamageCurrent;
    }

    public int getElementalDamageMax() {
        return elementalDamageMax;
    }

    public void increaseElementalDamage(int amount) {
        elementalDamageCurrent += amount;
        if (elementalDamageCurrent > elementalDamageMax) {
            elementalDamageCurrent = elementalDamageMax;
        }
    }
}
