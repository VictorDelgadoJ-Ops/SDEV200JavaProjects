package rpgdungeon;

/** The diver controlled by the player, including progression and supplies. */
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
        // Save potions for when they can actually restore some health.
        if (potions == 0 || !isAlive() || getHealth() == getMaximumHealth()) {
            return 0;
        }
        potions--;
        return heal(14);
    }

    boolean addPotion() {
        // Potion drops stop being useful once the player reaches the carry limit.
        if (potions >= MAX_POTIONS) {
            return false;
        }
        potions++;
        return true;
    }

    boolean buyWeaponUpgrade() {
        // Each weapon level adds damage, and later upgrades cost more gold.
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
        // Armor levels reduce incoming damage without letting hits reach zero.
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

    /** Award combat rewards and increase stats whenever an experience threshold is met. */
    public int earnExperience(int amount) {
        // Keep adding levels if one reward happens to pass more than one threshold.
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
        // Make sure loaded progress values are valid before restoring them.
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