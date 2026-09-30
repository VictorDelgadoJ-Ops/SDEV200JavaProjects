package rpgdungeon;

/** Enemy scaling and population rules for a new run. */
public enum Difficulty {
    EASY("Easy", 80, 75),
    MEDIUM("Medium", 100, 100),
    HARD("Hard", 125, 125),
    INSANE("Insane", 150, 150);

    private final String label;
    private final int healthPercent;
    private final int attackPercent;

    Difficulty(String label, int healthPercent, int attackPercent) {
        this.label = label;
        this.healthPercent = healthPercent;
        this.attackPercent = attackPercent;
    }

    public int getEnemyCount(int depth) {
        int standardCount = Math.min(3 + (depth - 1) / 2, 5);
        if (this == EASY) {
            return Math.max(1, standardCount - 1);
        }
        if (this == HARD) {
            return Math.min(5, standardCount + 1);
        }
        if (this == INSANE) {
            return 5;
        }
        return standardCount;
    }

    int scaleHealth(int health) {
        return (health * healthPercent + 99) / 100;
    }

    int scaleAttack(int attack) {
        int scaledAttack = attack * attackPercent;
        if (attackPercent >= 100) {
            scaledAttack += 99;
        }
        return Math.max(1, scaledAttack / 100);
    }

    static Difficulty fromId(String id) {
        if (id == null) {
            return MEDIUM;
        }
        try {
            return valueOf(id);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unknown saved difficulty: " + id, exception);
        }
    }

    @Override
    public String toString() {
        return label;
    }
}