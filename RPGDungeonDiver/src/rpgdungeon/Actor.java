package rpgdungeon;

/** Holds the health and basic combat rules shared by the player and all enemies. */
public abstract class Actor {
    private final String name;
    private int health;
    private int maximumHealth;
    private int attackPower;

    protected Actor(String name, int maximumHealth, int attackPower) {
        // Validate these values here so every actor starts with usable combat stats.
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

    /** Attack a living target and return how much health the hit actually removed. */
    public final int attack(Actor target) {
        // A missing or defeated target has no health left to reduce.
        if (target == null || !target.isAlive()) {
            return 0;
        }
        int oldHealth = target.getHealth();
        target.takeDamage(attackPower);
        return oldHealth - target.getHealth();
    }

    /** Apply incoming damage, with subclasses able to reduce it before health changes. */
    public void takeDamage(int damage) {
        // Negative values should not act like healing; healing has its own method.
        if (damage > 0) {
            health = Math.max(0, health - reduceDamage(damage));
        }
    }

    // The player overrides this hook for armor; other actors take the full hit.
    protected int reduceDamage(int damage) {
        return damage;
    }

    /** Restore health without exceeding the actor's maximum and return the amount restored. */
    public final int heal(int amount) {
        // Defeated actors cannot heal, and invalid or excess healing is ignored.
        if (amount <= 0 || !isAlive()) {
            return 0;
        }
        int oldHealth = health;
        health = Math.min(maximumHealth, health + amount);
        return health - oldHealth;
    }

    final void restoreStatistics(int savedHealth, int savedMaximumHealth, int savedAttackPower) {
        // Check loaded values before replacing the actor's current combat stats.
        if (savedMaximumHealth < 1 || savedAttackPower < 1
                || savedHealth < 0 || savedHealth > savedMaximumHealth) {
            throw new IllegalArgumentException("Saved character statistics are invalid.");
        }
        maximumHealth = savedMaximumHealth;
        health = savedHealth;
        attackPower = savedAttackPower;
    }

    protected final void increaseMaximumHealth(int amount) {
        // Leveling increases both the health limit and current health by the same amount.
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