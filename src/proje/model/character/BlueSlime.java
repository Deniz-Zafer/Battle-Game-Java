package proje.model.character;

import proje.model.combat.ElementType;

/** Ice elementli slime. */
public class BlueSlime extends Enemy {
    public BlueSlime() {
        super(
                "Blue Slime",
                20,
                8,
                2,
                10,
                30,
                ElementType.ICE
        );
    }

    @Override
    public void attack(Character target) {
        System.out.println(getName() + " zıplayarak çarptı!");
        target.takeDamage(getAttackPower());
    }

    @Override
    public void useSkill(Character target) {
        System.out.println(getName() + " Jel Yenileme kullanıyor ve can dolduruyor!");
        heal(15);
    }
}
