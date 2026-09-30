package rpgdungeon;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Small dependency-free regression suite for the game's rules and saved state. */
public final class GameLogicTest {
    private GameLogicTest() {
    }

    public static void main(String[] args) throws Exception {
        testInheritanceAndCombat();
        testPotionAndLevelProgression();
        testGridAndTurnRules();
        testDifficultyScaling();
        testSaveRoundTrip();
        testDiskPersistenceAndScores();
        testInvalidSaveRejected();
        System.out.println("All RPG Dungeon Diver logic tests passed.");
    }

    private static void testInheritanceAndCombat() {
        Player player = new Player("Test Diver");
        Enemy enemy = new Goblin(4, 4, 1);
        int oldHealth = enemy.getHealth();
        int damage = player.attack(enemy);
        check(damage == player.getAttackPower(), "Player attack returns its damage.");
        check(enemy.getHealth() == oldHealth - damage, "Inherited combat reduces enemy health.");
        check(enemy.getSymbol() == 'g', "Concrete enemy supplies its own map symbol.");
    }

    private static void testPotionAndLevelProgression() {
        Player player = new Player("Test Diver");
        player.takeDamage(20);
        int healed = player.drinkPotion();
        check(healed == 14, "Potion restores its configured amount.");
        check(player.getPotions() == 1, "Successful potion use consumes one item.");
        check(player.earnExperience(24) == 1, "Experience reaches the first level threshold.");
        check(player.getLevel() == 2, "Level increases when experience is earned.");
        check(player.getMaximumHealth() == 39, "Leveling increases maximum health.");
    }

    private static void testGridAndTurnRules() {
        GameSession session = new GameSession("Grid Tester", 8128L);
        check(session.getFloor().getEnemyCount() == 3, "First floor creates three enemies.");
        check(!session.move(-1, 0), "A wall blocks movement.");
        check(session.getTurns() == 0, "Blocked movement does not spend a turn.");
        check(session.move(0, 1), "An open tile accepts movement.");
        check(session.getTurns() == 1, "A valid move spends one turn.");
    }

    private static void testDifficultyScaling() {
        GameSession easy = new GameSession("Easy Diver", 31415L, Difficulty.EASY);
        GameSession medium = new GameSession("Medium Diver", 31415L, Difficulty.MEDIUM);
        GameSession hard = new GameSession("Hard Diver", 31415L, Difficulty.HARD);
        GameSession insane = new GameSession("Insane Diver", 31415L, Difficulty.INSANE);

        check(easy.getFloor().getEnemyCount() == 2, "Easy starts with fewer enemies.");
        check(medium.getFloor().getEnemyCount() == 3, "Medium starts with the standard enemy count.");
        check(hard.getFloor().getEnemyCount() == 4, "Hard starts with extra enemies.");
        check(insane.getFloor().getEnemyCount() == 5, "Insane fills every enemy slot.");
        Enemy easyEnemy = easy.getFloor().getEnemies()[0];
        Enemy mediumEnemy = medium.getFloor().getEnemies()[0];
        Enemy hardEnemy = hard.getFloor().getEnemies()[0];
        Enemy insaneEnemy = insane.getFloor().getEnemies()[0];
        check(easyEnemy.getMaximumHealth() < mediumEnemy.getMaximumHealth()
                && mediumEnemy.getMaximumHealth() < hardEnemy.getMaximumHealth()
                && hardEnemy.getMaximumHealth() < insaneEnemy.getMaximumHealth(),
                "Enemy health increases with difficulty.");
        check(hardEnemy.getAttackPower() > mediumEnemy.getAttackPower()
                && insaneEnemy.getAttackPower() > hardEnemy.getAttackPower(),
                "Enemy attack power increases on hard and insane.");
        check(easyEnemy.getAttackPower() < mediumEnemy.getAttackPower(),
            "Easy reduces enemy attack power.");
    }

    private static void testSaveRoundTrip() {
        GameSession original = new GameSession("Saved Diver", 24680L, Difficulty.HARD);
        original.move(0, 1);
        Properties saved = original.toProperties();
        GameSession restored = GameSession.fromProperties(saved);
        check(restored.getPlayer().getName().equals("Saved Diver"), "Save retains the player's name.");
        check(restored.getDifficulty() == Difficulty.HARD, "Save retains the selected difficulty.");
        check(restored.getPlayerX() == original.getPlayerX()
                && restored.getPlayerY() == original.getPlayerY(), "Save retains the player position.");
        check(restored.getTurns() == original.getTurns(), "Save retains the turn count.");
        check(restored.getFloor().getEnemyCount() == original.getFloor().getEnemyCount(),
                "Save restores the living enemies instead of generating replacements.");
        Enemy[] oldEnemies = original.getFloor().getEnemies();
        Enemy[] loadedEnemies = restored.getFloor().getEnemies();
        for (int slot = 0; slot < oldEnemies.length; slot++) {
            if (oldEnemies[slot] != null) {
                check(loadedEnemies[slot] != null, "Save retains enemy slots.");
                check(oldEnemies[slot].getX() == loadedEnemies[slot].getX()
                        && oldEnemies[slot].getY() == loadedEnemies[slot].getY(),
                        "Save retains enemy positions.");
                check(oldEnemies[slot].getHealth() == loadedEnemies[slot].getHealth(),
                        "Save retains enemy health.");
                check(oldEnemies[slot].getMaximumHealth() == loadedEnemies[slot].getMaximumHealth()
                    && oldEnemies[slot].getAttackPower() == loadedEnemies[slot].getAttackPower(),
                    "Save retains difficulty-scaled enemy stats.");
            }
        }

        Properties legacySave = new GameSession("Legacy Diver", 1357L).toProperties();
        legacySave.remove("difficulty");
        for (String key : legacySave.stringPropertyNames().toArray(new String[0])) {
            if (key.endsWith(".maxHealth") || key.endsWith(".attack")) {
                legacySave.remove(key);
            }
        }
        check(GameSession.fromProperties(legacySave).getDifficulty() == Difficulty.MEDIUM,
                "Legacy saves without difficulty load as medium.");
    }

    private static void testInvalidSaveRejected() {
        GameSession session = new GameSession("Invalid Save Tester", 9876L);
        Properties saved = session.toProperties();
        saved.setProperty("x", "999");
        boolean rejected = false;
        try {
            GameSession.fromProperties(saved);
        } catch (IllegalArgumentException exception) {
            rejected = true;
        }
        check(rejected, "Impossible saved positions are rejected.");
    }

    private static void testDiskPersistenceAndScores() throws Exception {
        Path directory = Files.createTempDirectory("rpg-dungeon-diver-test");
        try {
            SaveManager manager = new SaveManager(directory);
            GameSession original = new GameSession("Disk Tester", 13579L);
            original.move(0, 1);
            manager.save(original);
            GameSession loaded = manager.load();
            check(loaded.getPlayer().getName().equals("Disk Tester"), "Disk save restores the run.");
            check(loaded.getTurns() == original.getTurns(), "Disk save retains the run turn count.");

            manager.recordScore("Lower", 40);
            manager.recordScore("Higher", 90);
            Files.write(directory.resolve("high-scores.tsv"),
                    java.util.Collections.singletonList("damaged-score-row"), StandardCharsets.UTF_8,
                    java.nio.file.StandardOpenOption.APPEND);
            java.util.List<String> scores = manager.readHighScores();
            check(scores.size() == 2, "Malformed score rows are ignored.");
            check(scores.get(0).contains("Higher") && scores.get(0).endsWith("90"),
                    "High scores are sorted from highest to lowest.");
        } finally {
            Files.deleteIfExists(directory.resolve("saved-run.properties"));
            Files.deleteIfExists(directory.resolve("high-scores.tsv"));
            Files.deleteIfExists(directory);
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}