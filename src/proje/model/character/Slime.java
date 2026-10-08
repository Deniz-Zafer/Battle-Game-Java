package proje.model.character;
import proje.model.combat.ElementType;
public class Slime extends Enemy {

    public Slime() {
        super(
                "Slime",
                20,   // maxHealth
                8,    // attackPower
                2,    // defense
                10,   // rewardGold
                30,    // rewardExp
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
        // hedefe saldırmak yerine kendini iyileştirsin
        heal(15);
    }
}
