package proje.service;

import proje.model.character.Boss;
import proje.model.character.Enemy;
import proje.model.character.Evil;
import proje.model.character.Skeleton;
import proje.model.character.Slime;

import java.util.Random;

public class EnemyFactory {

    private static final Random RANDOM = new Random();

    /**
     * Zorluk seviyesine göre rastgele bir düşman döndürür.
     * difficulty: 1 = kolay, 2 = orta, 3 = zor, 4 = boss
     */
    public static Enemy createRandomEnemy(int difficulty) {

        if (difficulty == 1) {
            return createEasyEnemy();
        } else if (difficulty == 2) {
            return createMediumEnemy();
        } else if (difficulty == 3) {
            return createHardEnemy();
        } else if (difficulty == 4) {
            return new Boss();
        } else {
            return createEasyEnemy();
        }
    }

    private static Enemy createEasyEnemy() {
        int choice = RANDOM.nextInt(2); // 0 veya 1

        if (choice == 0) {
            return new Slime();
        } else {
            return new Skeleton();
        }
    }

    private static Enemy createMediumEnemy() {
        int choice = RANDOM.nextInt(2); // 0 veya 1

        if (choice == 0) {
            return new Skeleton();
        } else {
            return new Evil();
        }
    }

    private static Enemy createHardEnemy() {
        // Şimdilik zor düşman hep FireDemon olsun
        return new Evil();
    }
}
