package rpgdungeon;

/** Shared health and combat behavior for every living game character. */
public abstract class Actor {
    private final String name;
    private int health;
    private int maximumHealth;
    private int attackPower;

    protected Actor(String name, int maximumHealth, int attackPower) {
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
        if (target == null || !target.isAlive()) {
            return 0;
        }
        target.takeDamage(attackPower);
        return attackPower;
    }

    /** Reduce health without allowing it to fall below zero. */
    public final void takeDamage(int damage) {
        if (damage > 0) {
            health = Math.max(0, health - damage);
        }
    }

    /** Restore up to the actor's maximum health. */
    public final int heal(int amount) {
        if (amount <= 0 || !isAlive()) {
            return 0;
        }
        int oldHealth = health;
        health = Math.min(maximumHealth, health + amount);
        return health - oldHealth;
    }

    final void restoreStatistics(int savedHealth, int savedMaximumHealth, int savedAttackPower) {
        if (savedMaximumHealth < 1 || savedAttackPower < 1
                || savedHealth < 0 || savedHealth > savedMaximumHealth) {
            throw new IllegalArgumentException("Saved character statistics are invalid.");
        }
        maximumHealth = savedMaximumHealth;
        health = savedHealth;
        attackPower = savedAttackPower;
    }

    protected final void increaseMaximumHealth(int amount) {
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