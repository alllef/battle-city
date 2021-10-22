package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.ai.ReflexEnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTank;

public class WorldMapManager extends WorldMap {

    private static final WorldMapManager worldMapManager = new WorldMapManager();

    public static WorldMapManager getInstance() {
        return worldMapManager;
    }

    private final RTreeMap rTreeMap = RTreeMap.getInstance();
    PlayerTank playerTank = PlayerTank.getInstance();
    ReflexEnemyTankManager enemyTank = ReflexEnemyTankManager.getInstance();

    @Override
    public void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
        playerTank.draw(spriteBatch);
        enemyTank.draw(spriteBatch);
    }

    @Override
    public void update() {
        rTreeMap.update();
        bulletFactory.updateBullets();
        enemyTank.ride();
        enemyTank.shoot();
        playerTank.ride();
    }

}
