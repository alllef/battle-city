package com.github.alllef.battle_city.core.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTankManager;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTank;

public class WorldMapManager extends WorldMap {

    private static final WorldMapManager worldMapManager = new WorldMapManager();

    public static WorldMapManager getInstance() {
        return worldMapManager;
    }

    private final RTreeMap rTreeMap = RTreeMap.getInstance();
    PlayerTank aiPlayerTank = PlayerTank.getInstance();
    EnemyTankManager aiEnemyTank =EnemyTankManager.getInstance();

    @Override
    public void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
        aiPlayerTank.draw(spriteBatch);
        aiEnemyTank.draw(spriteBatch);
    }

    @Override
    public void update() {
        rTreeMap.update();
        bulletFactory.updateBullets();
        aiEnemyTank.ride();
        aiEnemyTank.shoot();
        aiPlayerTank.ride();
    }

}
