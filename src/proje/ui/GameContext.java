package proje.ui;

import proje.model.character.Player;
import proje.model.world.GameMap;
import proje.service.BattleManager;

public class GameContext {

    private  Player player;
    private  final GameMap gameMap;
    private  BattleManager battleManager;

    public GameContext(Player player, GameMap gameMap, BattleManager battleManager) {
        this.player = player;
        this.gameMap = gameMap;
        this.battleManager = battleManager;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public BattleManager getBattleManager() {
        return battleManager;
    }
}
