package proje.model.character;

import proje.model.combat.ElementType;

public class Skeleton extends Enemy {
    public Skeleton() {
        super("Bone Skeleton", 30, 10, 4, 15, 40, ElementType.NEUTRAL);
    }
    @Override
    public void attack(Character target) {
        System.out.println(getName() + " paslı kılıcıyla saldırdı!");
        target.takeDamage(getAttackPower());
    }

    @Override
    public void useSkill(Character target) {
        System.out.println(getName() + " Kemik Yağmuru kullanıyor!");
        int skillDamage = getAttackPower() + 5;
        target.takeDamage(skillDamage);
    }
}