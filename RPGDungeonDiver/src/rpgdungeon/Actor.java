package rpgdungeon;

/** Shared health and combat behavior for every living game character. */
public abstract class Actor {
    private final String name;
    private int health;
    private int maximumHealth;
    private int attackPower;

    protected Actor(String name, int maximumHealth, int attackPower) {
        // Check the shared character stats before saving them.
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("An actor must have a name.");
        }
        if (maximumHealth < 1 || attackPower < 1) {
            throw new IllegalArgumentException("Health and attack power must be positive.");
        }
        this.name = name.trim();
        this.maximumHealth = maximumHealth;
        this.health = maximumHealth;
        this.attackPower = attackPower;
    }

    /** Apply this actor's attack and return the damage dealt. */
    public final int attack(Actor target) {
        // Characters cannot damage a missing or already defeated target.
        if (target == null || !target.isAlive()) {
            return 0;
        }
        int oldHealth = target.getHealth();
        target.takeDamage(attackPower);
        return oldHealth - target.getHealth();
    }

    /** Reduce health without allowing it to fall below zero. */
    public void takeDamage(int damage) {
        // Ignore negative damage so it cannot accidentally heal the target.
        if (damage > 0) {
            health = Math.max(0, health - reduceDamage(damage));
        }
    }

    // Subclasses can adjust damage before it is taken.
    protected int reduceDamage(int damage) {
        return damage;
    }

    /** Restore up to the actor's maximum health. */
    public final int heal(int amount) {
        // Healing only works for living characters and never exceeds max health.
        if (amount <= 0 || !isAlive()) {
            return 0;
        }
        int oldHealth = health;
        health = Math.min(maximumHealth, health + amount);
        return health - oldHealth;
    }

    final void restoreStatistics(int savedHealth, int savedMaximumHealth, int savedAttackPower) {
        // Reject save data that would give an actor impossible stats.
        if (savedMaximumHealth < 1 || savedAttackPower < 1
                || savedHealth < 0 || savedHealth > savedMaximumHealth) {
            throw new IllegalArgumentException("Saved character statistics are invalid.");
        }
        maximumHealth = savedMaximumHealth;
        health = savedHealth;
        attackPower = savedAttackPower;
    }

    protected final void increaseMaximumHealth(int amount) {
        // Keep the player's current health topped up when their max health grows.
        maximumHealth += amount;
        health += amount;
    }

    protected final void increaseAttackPower(int amount) {
        attackPower += amount;
    }

    public final String getName() {
        return name;
    }

    public final int getHealth() {
        return health;
    }

    public final int getMaximumHealth() {
        return maximumHealth;
    }

    public final int getAttackPower() {
        return attackPower;
    }

    public final boolean isAlive() {
        return health > 0;
    }
}