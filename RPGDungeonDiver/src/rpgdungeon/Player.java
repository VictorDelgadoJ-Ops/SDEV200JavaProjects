package rpgdungeon;

/** Tracks the diver's leveling, gold, potions, and equipment upgrades. */
public final class Player extends Actor {
    private static final int MAX_POTIONS = 5;
    private static final int MAX_UPGRADE_LEVEL = 4;
    private int level = 1;
    private int experience;
    private int gold;
    private int potions = 2;
    private int weaponLevel;
    private int armorLevel;

    public Player(String name) {
        super(name, 34, 7);
    }

    /** Drink a potion and return the health restored, or zero if unavailable. */
    public int drinkPotion() {
        // Do not consume a potion if the player is out, defeated, or already at full health.
        if (potions == 0 || !isAlive() || getHealth() == getMaximumHealth()) {
            return 0;
        }
        potions--;
        return heal(14);
    }

    boolean addPotion() {
        // Keep enemy potion drops from putting more than the allowed number in the inventory.
        if (potions >= MAX_POTIONS) {
            return false;
        }
        potions++;
        return true;
    }

    boolean buyWeaponUpgrade() {
        // Check the level cap and price first, then pay for the upgrade and add two attack power.
        int cost = getWeaponUpgradeCost();
        if (weaponLevel >= MAX_UPGRADE_LEVEL || gold < cost) {
            return false;
        }
        gold -= cost;
        weaponLevel++;
        increaseAttackPower(2);
        return true;
    }

    boolean buyArmorUpgrade() {
        // Spend gold for one armor level; reduceDamage applies its effect when the player is hit.
        int cost = getArmorUpgradeCost();
        if (armorLevel >= MAX_UPGRADE_LEVEL || gold < cost) {
            return false;
        }
        gold -= cost;
        armorLevel++;
        return true;
    }

    int getWeaponUpgradeCost() {
        return 30 + weaponLevel * 25;
    }

    int getArmorUpgradeCost() {
        return 25 + armorLevel * 20;
    }

    @Override
    protected int reduceDamage(int damage) {
        return Math.max(1, damage - armorLevel);
    }

    /** Add earned experience and apply every level-up reached by the new total. */
    public int earnExperience(int amount) {
        // Use a loop because one enemy reward can carry the player across multiple level thresholds.
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
        // Reject invalid save data before restoring the player's level, supplies, and currency.
        if (savedLevel < 1 || savedExperience < 0 || savedGold < 0
                || savedPotions < 0 || savedPotions > MAX_POTIONS) {
            throw new IllegalArgumentException("Saved player progress is invalid.");
        }
        level = savedLevel;
        experience = savedExperience;
        gold = savedGold;
        potions = savedPotions;
    }

    void restoreEquipment(int savedWeaponLevel, int savedArmorLevel) {
        // Restrict loaded upgrade levels to the same range used by the shop.
        if (savedWeaponLevel < 0 || savedWeaponLevel > MAX_UPGRADE_LEVEL
                || savedArmorLevel < 0 || savedArmorLevel > MAX_UPGRADE_LEVEL) {
            throw new IllegalArgumentException("Saved equipment is invalid.");
        }
        weaponLevel = savedWeaponLevel;
        armorLevel = savedArmorLevel;
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

    public int getWeaponLevel() {
        return weaponLevel;
    }

    public int getArmorLevel() {
        return armorLevel;
    }

    public int getMaximumPotions() {
        return MAX_POTIONS;
    }
}