package com.github.alllef.battle_city.core.game_entity.tank.enemy.ai;

import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTank;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTank;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.Direction;
import com.github.alllef.battle_city.core.world.RTreeMap;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.geometry.internal.RectangleFloat;

import java.util.Iterator;

public class ReflexEnemyTank extends EnemyTank {
    protected RTreeMap rTreeMap;

    public ReflexEnemyTank(RTreeMap rTreeMap, BulletFactory bulletFactory, float x, float y) {
        super(bulletFactory, x, y);
        this.rTreeMap = rTreeMap;
    }

    @Override
    public void shoot() {
        if (areTanksOnParallel())
            super.shoot();
    }

    @Override
    public void ride(Direction dir) {
        super.ride(dir);
    }

    protected boolean areTanksOnParallel() {
        Coords coords = new Coords((int) sprite.getX(), (int) sprite.getY());
        Iterator<Entry<GameEntity, RectangleFloat>> iterator = rTreeMap.getParallelObstacles(getDir(), coords);

        while (iterator.hasNext()) {
            Entry<GameEntity, RectangleFloat> entry = iterator.next();
            if (entry.value() instanceof PlayerTank)
                return true;
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReflexEnemyTank)) return false;
        if (!super.equals(o)) return false;

        ReflexEnemyTank that = (ReflexEnemyTank) o;

        return rTreeMap != null ? rTreeMap.equals(that.rTreeMap) : that.rTreeMap == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (rTreeMap != null ? rTreeMap.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReflexEnemyTank{" +
                "rTreeMap=" + rTreeMap +
                '}';
    }
}
