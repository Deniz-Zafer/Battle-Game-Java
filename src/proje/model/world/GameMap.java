package proje.model.world;

import java.util.ArrayList;
import java.util.List;
import proje.model.character.Enemy;
import proje.service.EnemyFactory;


public class GameMap {

    private List<Level> levels = new ArrayList<>();

    public GameMap() {
        // İstediğin gibi özelleştirebilirsin
        levels.add(new Level(1, "Ormanın Girişi", 1));      // kolay
        levels.add(new Level(2, "Sisli Yol", 1));           // kolay
        levels.add(new Level(3, "Kemik Mezarlığı", 2));     // orta
        levels.add(new Level(4, "Lav Mağarası", 3));        // zor
        levels.add(new Level(5, "Karanlık Kule (Boss)", 4));// boss
    }

    public List<Level> getLevels() {
        return levels;
    }

    public int getTotalLevels() {
        return levels.size();
    }
}
