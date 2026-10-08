package proje.model.combat;

public class ElementCalculator {

    // Saldıran silahın elementi vs düşmanın elementi
    public static double getAttackMultiplier(ElementType weapon, ElementType enemy) {

        if (weapon == ElementType.FIRE && enemy == ElementType.ICE) return 1.5;
        if (weapon == ElementType.ICE && enemy == ElementType.FIRE) return 1.5;

        if (weapon == enemy && weapon != ElementType.NEUTRAL) return 0.8;

        return 1.0;
    }


    // Kalkanın elementi vs saldırganın elementi (defans multiplier)
    public static double getDefenseMultiplier(ElementType shieldElement, ElementType attackerElement) {
        if (shieldElement == null || shieldElement == ElementType.NEUTRAL) {
            return 1.0;
        }

        if (shieldElement == attackerElement && attackerElement != ElementType.NEUTRAL) {
            return 0.7; // aynı elemente karşı %30 daha az hasar
        }

        // İstersen zıt element ilişkisinde ekstra ceza/bonus koyabilirsin
        return 1.0;
    }
}
