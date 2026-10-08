package proje.model.character;

import proje.model.combat.ElementType;

public class FireSkeleton extends Enemy {
    public FireSkeleton() {
        super("Fire Skeleton", 30, 11, 4, 20, 45, ElementType.FIRE);
    }

    @Override
    public void attack(Character target) {
        System.out.println(getName() + " alevli kılıcıyla saldırdı!");
        target.takeDamage(getAttackPower());
    }

    @Override
    public void useSkill(Character target) {
        System.out.println(getName() + " Alev Yağmuru kullanıyor!");
        target.takeDamage(getAttackPower() + 6);
    }
}
