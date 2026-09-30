package rpgdungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/** Owns the turn-based rules and state for one complete roguelike run. */
public final class GameSession {
    private static final int MAX_LOG_ENTRIES = 7;
    private final Player player;
    private final long seed;
    private final Difficulty difficulty;
    private final List<String> messages = new ArrayList<String>();
    private int depth = 1;
    private int playerX;
    private int playerY;
    private int turns;
    private boolean gameOver;
    private DungeonFloor floor;

    public GameSession(String playerName, long seed) {
        this(playerName, seed, Difficulty.MEDIUM);
    }

    public GameSession(String playerName, long seed, Difficulty difficulty) {
        if (difficulty == null) {
            throw new IllegalArgumentException("A run must have a difficulty.");
        }
        player = new Player(playerName);
        this.seed = seed;
        this.difficulty = difficulty;
        createFloor();
        addMessage("The descent begins. Clear the floor and find the stairs.");
    }

    private void createFloor() {
        floor = new DungeonFloor(seed, depth, difficulty);
        playerX = floor.getStartX();
        playerY = floor.getStartY();
    }

    /** Move one tile; walking into an enemy performs a melee attack instead. */
    public boolean move(int deltaX, int deltaY) {
        if (gameOver || Math.abs(deltaX) + Math.abs(deltaY) != 1) {
            return false;
        }
        int nextX = playerX + deltaX;
        int nextY = playerY + deltaY;
        if (!floor.isWalkable(nextX, nextY)) {
            return false;
        }
        Enemy target = floor.enemyAt(nextX, nextY);
        if (target != null) {
            strike(target);
        } else {
            playerX = nextX;
            playerY = nextY;
            if (floor.hasStairsAt(playerX, playerY)) {
                addMessage("You found the stairs. Clear the floor to descend.");
            }
        }
        finishPlayerTurn();
        return true;
    }

    /** Attack the closest enemy next to the player. */
    public boolean attack() {
        if (gameOver) {
            return false;
        }
        Enemy target = closestAdjacentEnemy();
        if (target == null) {
            addMessage("No enemy is close enough to strike.");
            return false;
        }
        strike(target);
        finishPlayerTurn();
        return true;
    }

    /** Use one healing potion and spend a turn when healing succeeds. */
    public boolean drinkPotion() {
        if (gameOver) {
            return false;
        }
        int healed = player.drinkPotion();
        if (healed == 0) {
            addMessage(player.getPotions() == 0
                    ? "You are out of potions."
                    : "You are already at full health.");
            return false;
        }
        addMessage("Potion restores " + healed + " health.");
        finishPlayerTurn();
        return true;
    }

    /** Descend only from the stair tile after defeating every enemy on the floor. */
    public boolean descend() {
        if (gameOver) {
            return false;
        }
        if (!floor.hasStairsAt(playerX, playerY)) {
            addMessage("Find the gold stairs before descending.");
            return false;
        }
        if (floor.getEnemyCount() > 0) {
            addMessage("The stairs are sealed while enemies remain.");
            return false;
        }
        depth++;
        turns++;
        createFloor();
        addMessage("Floor " + depth + ": the air grows colder.");
        return true;
    }

    private void strike(Enemy enemy) {
        int damage = player.attack(enemy);
        addMessage("You strike the " + enemy.getName() + " for " + damage + " damage.");
        if (!enemy.isAlive()) {
            floor.removeEnemy(enemy);
            player.addGold(enemy.getGoldReward());
            int levels = player.earnExperience(enemy.getExperienceReward());
            addMessage(enemy.getName() + " defeated. +" + enemy.getGoldReward() + " gold.");
            if (levels > 0) {
                addMessage("Level up! You are now level " + player.getLevel() + ".");
            }
        }
    }

    private Enemy closestAdjacentEnemy() {
        Enemy closest = null;
        int closestDistance = Integer.MAX_VALUE;
        for (Enemy enemy : floor.getEnemies()) {
            if (enemy == null) {
                continue;
            }
            int distance = Math.abs(enemy.getX() - playerX) + Math.abs(enemy.getY() - playerY);
            if (distance <= 1 && distance < closestDistance) {
                closest = enemy;
                closestDistance = distance;
            }
        }
        return closest;
    }

    private void finishPlayerTurn() {
        turns++;
        Enemy[] enemies = floor.getEnemies();
        for (Enemy enemy : enemies) {
            if (enemy == null || !enemy.isAlive()) {
                continue;
            }
            int distance = Math.abs(enemy.getX() - playerX) + Math.abs(enemy.getY() - playerY);
            if (distance <= 1) {
                int damage = enemy.attack(player);
                addMessage(enemy.getName() + " hits you for " + damage + " damage.");
                if (!player.isAlive()) {
                    gameOver = true;
                    addMessage("You have fallen in the dungeon.");
                    break;
                }
            } else {
                int[] next = floor.nextStepToward(enemy, playerX, playerY);
                if (next[0] == playerX && next[1] == playerY) {
                    int damage = enemy.attack(player);
                    addMessage(enemy.getName() + " hits you for " + damage + " damage.");
                    if (!player.isAlive()) {
                        gameOver = true;
                        addMessage("You have fallen in the dungeon.");
                        break;
                    }
                } else if (next[0] != enemy.getX() || next[1] != enemy.getY()) {
                    enemy.setPosition(next[0], next[1]);
                }
            }
        }
    }

    private void addMessage(String message) {
        messages.add(message);
        while (messages.size() > MAX_LOG_ENTRIES) {
            messages.remove(0);
        }
    }

    /** Export all run state needed to reproduce this floor after loading. */
    Properties toProperties() {
        Properties values = new Properties();
        values.setProperty("version", "1");
        values.setProperty("name", player.getName());
        values.setProperty("seed", Long.toString(seed));
        values.setProperty("difficulty", difficulty.name());
        values.setProperty("depth", Integer.toString(depth));
        values.setProperty("x", Integer.toString(playerX));
        values.setProperty("y", Integer.toString(playerY));
        values.setProperty("turns", Integer.toString(turns));
        values.setProperty("health", Integer.toString(player.getHealth()));
        values.setProperty("maxHealth", Integer.toString(player.getMaximumHealth()));
        values.setProperty("attack", Integer.toString(player.getAttackPower()));
        values.setProperty("level", Integer.toString(player.getLevel()));
        values.setProperty("experience", Integer.toString(player.getExperience()));
        values.setProperty("gold", Integer.toString(player.getGold()));
        values.setProperty("potions", Integer.toString(player.getPotions()));
        Enemy[] slots = floor.getEnemySlots();
        values.setProperty("enemySlots", Integer.toString(slots.length));
        for (int slot = 0; slot < slots.length; slot++) {
            Enemy enemy = slots[slot];
            String key = "enemy." + slot + ".";
            values.setProperty(key + "type", enemy == null ? "none" : enemy.getTypeId());
            if (enemy != null) {
                values.setProperty(key + "x", Integer.toString(enemy.getX()));
                values.setProperty(key + "y", Integer.toString(enemy.getY()));
                values.setProperty(key + "health", Integer.toString(enemy.getHealth()));
                values.setProperty(key + "maxHealth", Integer.toString(enemy.getMaximumHealth()));
                values.setProperty(key + "attack", Integer.toString(enemy.getAttackPower()));
            }
        }
        return values;
    }

    /** Restore a saved run and reject incomplete or impossible state. */
    static GameSession fromProperties(Properties values) {
        if (!"1".equals(values.getProperty("version"))) {
            throw new IllegalArgumentException("Unsupported save version.");
        }
        String name = values.getProperty("name");
        long seed = Long.parseLong(values.getProperty("seed"));
        Difficulty difficulty = Difficulty.fromId(values.getProperty("difficulty"));
        GameSession session = new GameSession(name, seed, difficulty);
        session.depth = positiveInt(values, "depth");
        session.floor = new DungeonFloor(seed, session.depth, difficulty, false);
        session.playerX = nonnegativeInt(values, "x");
        session.playerY = nonnegativeInt(values, "y");
        if (!session.floor.isWalkable(session.playerX, session.playerY)) {
            throw new IllegalArgumentException("Saved player position is invalid.");
        }
        session.turns = nonnegativeInt(values, "turns");
        session.player.restoreStatistics(nonnegativeInt(values, "health"),
                positiveInt(values, "maxHealth"), positiveInt(values, "attack"));
        session.player.restoreProgress(positiveInt(values, "level"),
                nonnegativeInt(values, "experience"), nonnegativeInt(values, "gold"),
                nonnegativeInt(values, "potions"));

        int slots = positiveInt(values, "enemySlots");
        if (slots != session.floor.getEnemySlots().length) {
            throw new IllegalArgumentException("Saved enemy list has an invalid size.");
        }
        for (int slot = 0; slot < slots; slot++) {
            String key = "enemy." + slot + ".";
            String type = values.getProperty(key + "type");
            if (type == null) {
                throw new IllegalArgumentException("Saved enemy data is incomplete.");
            }
            if (!"none".equals(type)) {
                int enemyX = nonnegativeInt(values, key + "x");
                int enemyY = nonnegativeInt(values, key + "y");
                int enemyHealth = positiveInt(values, key + "health");
                Enemy enemy = Enemy.create(type, enemyX, enemyY, session.depth);
                enemy.applyDifficulty(difficulty);
                int maximumHealth = optionalPositiveInt(values, key + "maxHealth",
                        enemy.getMaximumHealth());
                int attackPower = optionalPositiveInt(values, key + "attack", enemy.getAttackPower());
                if (enemyHealth > maximumHealth) {
                    throw new IllegalArgumentException("Saved enemy health is invalid.");
                }
                enemy.restoreStatistics(enemyHealth, maximumHealth, attackPower);
                session.floor.restoreEnemy(slot, enemy);
            }
        }
        session.messages.clear();
        session.addMessage("Saved run restored. The dungeon remembers.");
        return session;
    }

    private static int positiveInt(Properties values, String key) {
        int value = Integer.parseInt(values.getProperty(key));
        if (value < 1) {
            throw new IllegalArgumentException("Saved value must be positive: " + key);
        }
        return value;
    }

    private static int nonnegativeInt(Properties values, String key) {
        int value = Integer.parseInt(values.getProperty(key));
        if (value < 0) {
            throw new IllegalArgumentException("Saved value cannot be negative: " + key);
        }
        return value;
    }

    private static int optionalPositiveInt(Properties values, String key, int defaultValue) {
        return values.containsKey(key) ? positiveInt(values, key) : defaultValue;
    }

    public Player getPlayer() {
        return player;
    }

    public DungeonFloor getFloor() {
        return floor;
    }

    public int getDepth() {
        return depth;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public int getTurns() {
        return turns;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getScore() {
        return depth * 100 + player.getGold() + player.getLevel() * 50;
    }

    public List<String> getMessages() {
        return Collections.unmodifiableList(new ArrayList<String>(messages));
    }
}