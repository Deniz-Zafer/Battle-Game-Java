package proje.model.character;

import proje.model.combat.ElementType;

public class Yeti extends Enemy {

    public Yeti() {
        super(
                "Yeti",          // name
                40,             // maxHealth (ve başlangıç health genelde bununla aynı set edilir)
                22,              // attackPower
                8,               // defense
                60,              // rewardGold (sende bu alan yoksa Enemy ctor'una göre düzelt)
                45,              // rewardExp  (sende bu alan yoksa Enemy ctor'una göre düzelt)
                ElementType.ICE  // element
        );
    }
}
