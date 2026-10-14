package rpgdungeon;

/** Adds map position and defeat rewards to the shared combat behavior for enemies. */
public abstract class Enemy extends Actor {
    private int x;
    private int y;
    private final int experienceReward;
    private final int goldReward;

    protected Enemy(String name, int health, int attackPower, int experienceReward,
            int goldReward, int x, int y) {
        super(name, health, attackPower);
        this.experienceReward = experienceReward;
        this.goldReward = goldReward;
        this.x = x;
        this.y = y;
    }

    /** Return the one-character marker shown for this enemy on the dungeon map. */
    public abstract char getSymbol();

    /** Return the save-file identifier used to recreate this specific enemy type. */
    public abstract String getTypeId();

    /** Build an enemy from its saved type name, map coordinates, and current floor depth. */
    public static Enemy create(String type, int x, int y, int depth) {
        // Saves store short text IDs instead of class names, so map each known ID to its class.
        if ("skeleton".equals(type)) {
            return new Skeleton(x, y, depth);
        }
        if ("wraith".equals(type)) {
            return new Wraith(x, y, depth);
        }
        if ("goblin".equals(type)) {
            return new Goblin(x, y, depth);
        }
        throw new IllegalArgumentException("Unknown saved enemy type: " + type);
    }

    void applyDifficulty(Difficulty difficulty) {
        // Scale the enemy's base stats for this run before its saved stats, if any, are restored.
        int maximumHealth = difficulty.scaleHealth(getMaximumHealth());
        int attackPower = difficulty.scaleAttack(getAttackPower());
        restoreStatistics(maximumHealth, maximumHealth, attackPower);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getExperienceReward() {
        return experienceReward;
    }

    public int getGoldReward() {
        return goldReward;
    }
}