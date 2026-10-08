package proje.model.world;

import proje.model.character.Enemy;
import proje.service.EnemyFactory;

public class Level {

    private int levelNumber;
    private String name;
    private int difficulty; // 1-4 arası kullanacağız

    public Level(int levelNumber, String name, int difficulty) {
        this.levelNumber = levelNumber;
        this.name = name;
        this.difficulty = difficulty;
    }

    public Enemy createEnemy() {
        // Bu level için uygun zorlukta düşman oluştur
        return EnemyFactory.createRandomEnemy(difficulty);
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public String getName() {
        return name;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
