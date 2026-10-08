package proje.model.character;

import proje.model.combat.ElementType;

/** Fire elementli slime. */
public class FireSlime extends Enemy {
    public FireSlime() {
        super(
                "Fire Slime",
                20,
                9,
                2,
                12,
                32,
                ElementType.FIRE
        );
    }

    @Override
    public void attack(Character target) {
        System.out.println(getName() + " zıplayarak çarptı!");
        target.takeDamage(getAttackPower());
    }

    @Override
    public void useSkill(Character target) {
        System.out.println(getName() + " Alev Jel Patlaması kullanıyor!");
        target.takeDamage(getAttackPower() + 4);
    }
}
