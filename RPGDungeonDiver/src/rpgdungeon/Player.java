package rpgdungeon;

/** The diver controlled by the player, including progression and supplies. */
public final class Player extends Actor {
    private int level = 1;
    private int experience;
    private int gold;
    private int potions = 2;

    public Player(String name) {
        super(name, 34, 7);
    }

    /** Drink a potion and return the health restored, or zero if unavailable. */
    public int drinkPotion() {
        if (potions == 0 || !isAlive() || getHealth() == getMaximumHealth()) {
            return 0;
        }
        potions--;
        return heal(14);
    }

    /** Award combat rewards and increase stats whenever an experience threshold is met. */
    public int earnExperience(int amount) {
        experience += Math.max(0, amount);
        int levelsGained = 0;
        while (experience >= level * 24) {
            experience -= level * 24;
            level++;
            levelsGained++;
            increaseMaximumHealth(5);
            increaseAttackPower(1);
        }
        return levelsGained;
    }

    void restoreProgress(int savedLevel, int savedExperience, int savedGold, int savedPotions) {
        if (savedLevel < 1 || savedExperience < 0 || savedGold < 0 || savedPotions < 0) {
            throw new IllegalArgumentException("Saved player progress is invalid.");
        }
        level = savedLevel;
        experience = savedExperience;
        gold = savedGold;
        potions = savedPotions;
    }

    void addGold(int amount) {
        gold += Math.max(0, amount);
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getNextLevelExperience() {
        return level * 24;
    }

    public int getGold() {
        return gold;
    }

    public int getPotions() {
        return potions;
    }
}