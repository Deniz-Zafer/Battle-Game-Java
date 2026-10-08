package proje.model.character;

import proje.model.combat.ElementType;

public class Evil extends Enemy{
        public Evil() {
            super("Fire Demon", 40, 16, 3, 25, 60, ElementType.FIRE);
        }
    @Override
    public void attack(Character target) {
        System.out.println(getName() + " alevli pençesiyle saldırdı!");
        target.takeDamage(getAttackPower());
    }

    @Override
    public void useSkill(Character target) {
        System.out.println(getName() + " Cehennem Ateşi kullanıyor!");
        int skillDamage = getAttackPower() * 2;
        target.takeDamage(skillDamage);
    }
}
