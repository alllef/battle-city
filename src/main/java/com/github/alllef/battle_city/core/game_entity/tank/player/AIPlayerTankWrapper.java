package com.github.alllef.battle_city.core.game_entity.tank.player;

public class AIPlayerTankWrapper /*extends PlayerTank*/ {

   /* private static final PlayerTank aIPlayerTankWrapper = new AIPlayerTankWrapper(BulletFactory.getInstance());

    public static PlayerTank getInstance() {
        return aIPlayerTankWrapper;
    }

    private final PlayerTank playerTank = PlayerTank.getInstance();
    RTreeMap rTreeMap = RTreeMap.getInstance();
    Stack<Coords> coordsToTarget = new Stack<>();
    private Coords turnCoord = null;

    protected AIPlayerTankWrapper(BulletFactory bulletFactory) {
        super(bulletFactory);
        generatePath();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        playerTank.draw(spriteBatch);

        ShapeDrawer shapeDrawer = new ShapeDrawer(spriteBatch, new TextureRegion(new Texture(Gdx.files.internal("sprites/block.png"))));
        shapeDrawer.setColor(Color.YELLOW);
        coordsToTarget.forEach(coords ->
                shapeDrawer.line(coords.x(), coords.y(), coords.x() + 1, coords.y() + 1));

    }

    @Override
    public void shoot() {
        if (areTanksOnParallel())
        playerTank.shoot();
    }

    @Override
    public void ride() {
        if (turnCoord == null)
            turnCoord = getTurnCoord();

        Coords tankCoord = new Coords((int) this.getSprite().getX(), (int) this.getSprite().getY());

        Optional<Map.Entry<BiPredicate<Coords, Coords>, Direction>> predicateEntry = getPredicateMap()
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(this.getDir()))
                .findFirst();

        BiPredicate<Coords, Coords> predicate = null;

        if (predicateEntry.isPresent())
            predicate = predicateEntry.get().getKey();

        if (predicate.test(tankCoord, turnCoord)) {
            setRideLooping(true);
            playerTank.ride();
        } else
            turnCoord = getTurnCoord();
    }


    private Coords getTurnCoord() {
        Coords first = coordsToTarget.peek();
        coordsToTarget.pop();

        if (coordsToTarget.isEmpty()) {
            generatePath();
            getTurnCoord();
        }
        Coords second = coordsToTarget.peek();

        Map<BiPredicate<Coords, Coords>, Direction> predicateMap = getPredicateMap();

        for (BiPredicate<Coords, Coords> predicate : predicateMap.keySet()) {
            Direction dir = predicateMap.get(predicate);
            if (predicate.test(first, second) && getBlockedDirection() != dir) {
                this.setDir(dir);
                break;
            }
        }

        return first;
    }

    private void generatePath() {

        Coords coords = rTreeMap.getRandomNonObstacleCoord();
        Rectangle coordsRect = new Rectangle(coords.x(), coords.y(), 1, 1);
        AStarAlgo algo = new AStarAlgo(this.getSprite().getBoundingRectangle(), coordsRect, AlgoType.ASTAR_COORDS);
        coordsToTarget.addAll(algo.startAlgo());

    }

    private Map<BiPredicate<Coords, Coords>, Direction> getPredicateMap() {
        Map<BiPredicate<Coords, Coords>, Direction> predicateMap = new HashMap<>();

        predicateMap.put((coord1, coord2) -> coord1.x() < coord2.x(), Direction.RIGHT);
        predicateMap.put((coord1, coord2) -> coord1.x() > coord2.x(), Direction.LEFT);
        predicateMap.put((coord1, coord2) -> coord1.y() < coord2.y(), Direction.UP);
        predicateMap.put((coord1, coord2) -> coord1.y() > coord2.y(), Direction.DOWN);

        return predicateMap;
    }

    private boolean areTanksOnParallel() {
        Sprite tankSprite = getSprite();
        Coords coords = new Coords((int) tankSprite.getX(), (int) tankSprite.getY());
        Iterator<Entry<GameEntity, RectangleFloat>> iterator = rTreeMap.getParallelObstacles(getDir(), coords);

        while (iterator.hasNext()) {
            Entry<GameEntity, RectangleFloat> entry = iterator.next();
            if (entry.value() instanceof EnemyTank)
                return true;
        }

        return false;
    }

    @Override
    public boolean isRideLooping() {
        return playerTank.isRideLooping();
    }

    @Override
    public void setRideLooping(boolean rideLooping) {
        playerTank.setRideLooping(rideLooping);
    }

    @Override
    public Direction getDir() {
        return playerTank.getDir();
    }

    @Override
    public void setDir(Direction dir) {
        playerTank.setDir(dir);
    }

    @Override
    public Direction getBlockedDirection() {
        return playerTank.getBlockedDirection();
    }

    @Override
    public void setBlockedDirection(Direction blockedDirection) {
        playerTank.setBlockedDirection(blockedDirection);
    }

    @Override
    public void ride(Direction dir) {
        playerTank.ride(dir);
    }

    @Override
    public void overlapsObstacle(Obstacle obstacle) {
        playerTank.overlapsObstacle(obstacle);
    }


    @Override
    public Sprite getSprite() {
        return playerTank.getSprite();
    }

    @Override
    public void setSprite(Sprite sprite) {
        playerTank.setSprite(sprite);
    }
    */

}
