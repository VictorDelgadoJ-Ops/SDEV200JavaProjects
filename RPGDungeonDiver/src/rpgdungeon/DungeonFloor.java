package rpgdungeon;

import java.util.Random;

/** A grid-based dungeon floor, its stair tile, and its enemy array. */
public final class DungeonFloor {
    private static final String[] BLUEPRINT = {
        "###################",
        "#.................#",
        "#..###......##....#",
        "#..#..............#",
        "#..#..####........#",
        "#.....#...........#",
        "#..##..#..###.....#",
        "#.......#.........#",
        "#.......#..##.....#",
        "#.................#",
        "###################"
    };
    private static final int ENEMY_SLOTS = 5;
    private static final int START_X = 1;
    private static final int START_Y = 1;
    private static final int STAIRS_X = 17;
    private static final int STAIRS_Y = 9;

    private final char[][] tiles;
    private final Enemy[] enemies = new Enemy[ENEMY_SLOTS];
    private int enemyCount;

    public DungeonFloor(long seed, int depth) {
        this(seed, depth, Difficulty.MEDIUM, true);
    }

    public DungeonFloor(long seed, int depth, Difficulty difficulty) {
        this(seed, depth, difficulty, true);
    }

    DungeonFloor(long seed, int depth, boolean populateEnemies) {
        this(seed, depth, Difficulty.MEDIUM, populateEnemies);
    }

    DungeonFloor(long seed, int depth, Difficulty difficulty, boolean populateEnemies) {
        // Copy the map strings into a grid so the stairs and enemies can be added.
        tiles = new char[BLUEPRINT.length][BLUEPRINT[0].length()];
        for (int y = 0; y < BLUEPRINT.length; y++) {
            tiles[y] = BLUEPRINT[y].toCharArray();
        }
        tiles[STAIRS_Y][STAIRS_X] = '>';
        if (populateEnemies) {
            placeEnemies(new Random(seed + depth * 7919L), depth, difficulty);
        }
    }

    private void placeEnemies(Random random, int depth, Difficulty difficulty) {
        // Place each enemy on an open tile, away from the player and the stairs.
        int count = Math.min(difficulty.getEnemyCount(depth), ENEMY_SLOTS);
        for (int slot = 0; slot < count; slot++) {
            int x;
            int y;
            int attempts = 0;
            do {
                x = 1 + random.nextInt(getWidth() - 2);
                y = 1 + random.nextInt(getHeight() - 2);
                attempts++;
            } while (attempts < 1000 && (!isOpenFloor(x, y)
                    || distance(x, y, START_X, START_Y) < 7
                    || distance(x, y, STAIRS_X, STAIRS_Y) < 2
                    || enemyAt(x, y) != null));

            if (isOpenFloor(x, y) && enemyAt(x, y) == null) {
                int roll = random.nextInt(100);
                if (depth >= 3 && roll < 30) {
                    enemies[slot] = new Wraith(x, y, depth);
                } else if (depth >= 2 && roll < 65) {
                    enemies[slot] = new Skeleton(x, y, depth);
                } else {
                    enemies[slot] = new Goblin(x, y, depth);
                }
                enemies[slot].applyDifficulty(difficulty);
                enemyCount++;
            }
        }
    }

    // Keep coordinate checks in one place so pathfinding and enemy placement agree.
    private boolean isOpenFloor(int x, int y) {
        return isInside(x, y) && (tiles[y][x] == '.' || tiles[y][x] == '>');
    }

    private boolean isInside(int x, int y) {
        return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
    }

    private static int distance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    /** Find the first open step on a shortest route toward the diver. */
    public int[] nextStepToward(Enemy enemy, int targetX, int targetY) {
        int width = getWidth();
        int height = getHeight();
        // These arrays remember visited tiles and how the search reached each one.
        int[][] previousX = new int[height][width];
        int[][] previousY = new int[height][width];
        boolean[][] visited = new boolean[height][width];
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                previousX[row][column] = -1;
                previousY[row][column] = -1;
            }
        }

        int[] queueX = new int[width * height];
        int[] queueY = new int[width * height];
        int head = 0;
        int tail = 0;
        queueX[tail] = enemy.getX();
        queueY[tail++] = enemy.getY();
        visited[enemy.getY()][enemy.getX()] = true;
        previousX[enemy.getY()][enemy.getX()] = enemy.getX();
        previousY[enemy.getY()][enemy.getX()] = enemy.getY();
        int[] directionX = {0, 1, 0, -1};
        int[] directionY = {-1, 0, 1, 0};

        // Search outward one tile at a time to find a shortest route.
        while (head < tail && !visited[targetY][targetX]) {
            int currentX = queueX[head];
            int currentY = queueY[head++];
            for (int direction = 0; direction < directionX.length; direction++) {
                int nextX = currentX + directionX[direction];
                int nextY = currentY + directionY[direction];
                if (!isOpenFloor(nextX, nextY) || visited[nextY][nextX]) {
                    continue;
                }
                Enemy blocker = enemyAt(nextX, nextY);
                if (blocker != null && blocker != enemy) {
                    continue;
                }
                visited[nextY][nextX] = true;
                previousX[nextY][nextX] = currentX;
                previousY[nextY][nextX] = currentY;
                queueX[tail] = nextX;
                queueY[tail++] = nextY;
            }
        }

        if (!visited[targetY][targetX]) {
            return new int[] {enemy.getX(), enemy.getY()};
        }
        // Follow the saved path backward to find just the enemy's next move.
        int stepX = targetX;
        int stepY = targetY;
        while (previousX[stepY][stepX] != enemy.getX()
                || previousY[stepY][stepX] != enemy.getY()) {
            int oldX = previousX[stepY][stepX];
            int oldY = previousY[stepY][stepX];
            stepX = oldX;
            stepY = oldY;
        }
        return new int[] {stepX, stepY};
    }

    /** Return the enemy occupying a tile, or null when it is empty. */
    public Enemy enemyAt(int x, int y) {
        for (Enemy enemy : enemies) {
            if (enemy != null && enemy.getX() == x && enemy.getY() == y) {
                return enemy;
            }
        }
        return null;
    }

    void removeEnemy(Enemy enemy) {
        // Clear the matching slot when an enemy is defeated.
        for (int slot = 0; slot < enemies.length; slot++) {
            if (enemies[slot] == enemy) {
                enemies[slot] = null;
                enemyCount--;
                return;
            }
        }
    }

    void restoreEnemy(int slot, Enemy enemy) {
        // Check saved enemies so they do not overlap or end up inside a wall.
        if (slot < 0 || slot >= enemies.length || enemies[slot] != null || enemy == null) {
            throw new IllegalArgumentException("Saved enemy placement is invalid.");
        }
        if (!isOpenFloor(enemy.getX(), enemy.getY()) || enemyAt(enemy.getX(), enemy.getY()) != null) {
            throw new IllegalArgumentException("Saved enemy position is invalid.");
        }
        enemies[slot] = enemy;
        enemyCount++;
    }

    Enemy[] getEnemySlots() {
        return enemies.clone();
    }

    public Enemy[] getEnemies() {
        return enemies.clone();
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public int getWidth() {
        return tiles[0].length;
    }

    public int getHeight() {
        return tiles.length;
    }

    public char getTile(int x, int y) {
        if (!isInside(x, y)) {
            return '#';
        }
        return tiles[y][x];
    }

    public boolean isWalkable(int x, int y) {
        return isOpenFloor(x, y);
    }

    public boolean hasStairsAt(int x, int y) {
        return isInside(x, y) && tiles[y][x] == '>';
    }

    public int getStartX() {
        return START_X;
    }

    public int getStartY() {
        return START_Y;
    }

    public int getStairsX() {
        return STAIRS_X;
    }

    public int getStairsY() {
        return STAIRS_Y;
    }
}