package proje.model.character;

import proje.model.combat.ElementType;

public class IceSkeleton extends Enemy {
    public IceSkeleton() {
        super("Ice Skeleton", 30, 10, 5, 20, 45, ElementType.ICE);
    }

    @Override
    public void attack(Character target) {
        System.out.println(getName() + " buzlu kılıcıyla saldırdı!");
        target.takeDamage(getAttackPower());
    }

    @Override
    public void useSkill(Character target) {
        System.out.println(getName() + " Donma Darbesi kullanıyor!");
        target.takeDamage(getAttackPower() + 5);
    }
}
