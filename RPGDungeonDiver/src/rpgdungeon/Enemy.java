package rpgdungeon;

/** Base class for enemies; concrete enemy types provide identity and rewards. */
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

    /** A compact symbol used by the dungeon renderer. */
    public abstract char getSymbol();

    /** Stable type identifier used to restore saved enemies. */
    public abstract String getTypeId();

    /** Create the requested enemy type at a position for the given floor. */
    public static Enemy create(String type, int x, int y, int depth) {
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