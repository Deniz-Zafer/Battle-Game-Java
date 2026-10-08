package proje.model.item;

import proje.model.character.Player;
import proje.model.combat.ElementType;

public class Shield extends EquipableItem {

    private int level;

    private ElementType elementType;
    private int elementalResistCurrent;
    private int elementalResistMax;

    public Shield(String name, String description, int level,
                  int defenseBonusCurrent, int defenseBonusMax,
                  ElementType elementType,
                  int elementalResistCurrent, int elementalResistMax) {

        super(name, description,
                0, 0,
                defenseBonusCurrent, defenseBonusMax,
                0, 0);

        this.level = level;
        this.elementType = elementType;
        this.elementalResistCurrent = elementalResistCurrent;
        this.elementalResistMax = elementalResistMax;
    }

    public void use(Player player) {
        System.out.println("Kalkan doğrudan kullanılamaz, takılmalıdır.");
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

    public int getElementalResistCurrent() {
        return elementalResistCurrent;
    }

    public int getElementalResistMax() {
        return elementalResistMax;
    }

    public void increaseElementalResist(int amount) {
        elementalResistCurrent += amount;
        if (elementalResistCurrent > elementalResistMax) {
            elementalResistCurrent = elementalResistMax;
        }
    }
}
