package com.github.alllef.battle_city.core.game_entity.tank.enemy.ai;

import com.badlogic.gdx.Preferences;
import com.github.alllef.battle_city.core.game_entity.bullet.BulletFactory;
import com.github.alllef.battle_city.core.game_entity.common.GameEntity;
import com.github.alllef.battle_city.core.game_entity.tank.enemy.EnemyTank;
import com.github.alllef.battle_city.core.game_entity.tank.player.PlayerTank;
import com.github.alllef.battle_city.core.util.Coords;
import com.github.alllef.battle_city.core.util.enums.Direction;
import com.github.alllef.battle_city.core.world.RTreeMap;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.geometry.internal.RectangleFloat;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReflexEnemyTank extends EnemyTank {

    protected RTreeMap rTreeMap;

    public ReflexEnemyTank(RTreeMap rTreeMap, BulletFactory bulletFactory, float x, float y, Preferences prefs) {
        super(bulletFactory, x, y, prefs);
        this.rTreeMap = rTreeMap;
    }

    @Override
    public void shoot() {
        System.out.println(rTreeMap.getRtreeSize() + " Size in ReflexEnemyTank");
        if (areTanksOnParallel()) {
            super.shoot();
        }
    }

    protected boolean areTanksOnParallel() {
        Coords coords = new Coords((int) sprite.getX(), (int) sprite.getY());
        var optionalIt = rTreeMap.getParallelObstacles(getDir(), coords);
        Iterator<Entry<GameEntity, RectangleFloat>> iterator;
        if (optionalIt.isPresent()) {
            iterator = optionalIt.get();

            while (iterator.hasNext()) {
                Entry<GameEntity, RectangleFloat> entry = iterator.next();
                //  if (entry.value() instanceof PlayerTank)
                return true;
            }
        }
        return false;
    }

    private Optional<Direction> getTanksOnAnyParallel() {
        Coords coords = new Coords((int) sprite.getX(), (int) sprite.getY());

        for (Direction dir : Direction.values()) {
            var optionalObstacles = rTreeMap.getParallelObstacles(dir, coords);
            if (optionalObstacles.isPresent()) {
                var iter = optionalObstacles.get();
                while (iter.hasNext()) {
                    if (iter.next() instanceof PlayerTank)
                        return Optional.of(dir);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReflexEnemyTank)) return false;
        if (!super.equals(o)) return false;

        ReflexEnemyTank that = (ReflexEnemyTank) o;

        return Objects.equals(rTreeMap, that.rTreeMap);
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
