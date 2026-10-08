package proje.model.character;

import proje.model.combat.ElementType;

public class Enemy extends Character {

    private int rewardGold;
    private int rewardExp;

    public Enemy(String name, int maxHealth, int attackPower, int defense,
                 int rewardGold, int rewardExp, ElementType elementType) {

        super(name, maxHealth, attackPower, defense, elementType);
        this.rewardGold = rewardGold;
        this.rewardExp = rewardExp;
    }

    @Override
    public void attack(Character target) {
        System.out.println(getName() + " saldırıyor!");
        // Gerçek hasar hesaplamasını BattleManager içinde yapıyoruz.
    }

    @Override
    public void useSkill(Character target) {
        System.out.println(getName() + " özel bir saldırı kullanıyor!");
        target.takeDamage((int) (getAttackPower() * 1.5));
    }

    public int getRewardGold() {
        return rewardGold;
    }

    public int getRewardExp() {
        return rewardExp;
    }
}
