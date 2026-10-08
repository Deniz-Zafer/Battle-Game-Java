package proje.model.character;

import proje.model.combat.ElementType;

public class Boss extends Enemy {

    public Boss() {
        super(
                "Necromancer Boss",
                50,  // maxHealth
                18,   // attackPower
                5,    // defense
                50,   // rewardGold
                120,  // rewardExp
                ElementType.NEUTRAL  //  hangi element olsun istiyorsan bunu seç
        );
    }

    @Override
    public void attack(Character target) {
        System.out.println(getName() + " karanlık büyü ile vurdu!");
        target.takeDamage(getAttackPower());
    }

    @Override
    public void useSkill(Character target) {
        System.out.println(getName() + " Ruh Emme kullanıyor!");
        int skillDamage = getAttackPower() + 10;
        target.takeDamage(skillDamage);
        heal(10);
    }
}
