package rpgdungeon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

/** Handles the files used to save a run and keep the five best local scores. */
public final class SaveManager {
    private final Path dataDirectory;
    private final Path saveFile;
    private final Path scoreFile;

    public SaveManager() {
        this(Paths.get(System.getProperty("user.home"), ".rpg-dungeon-diver"));
    }

    SaveManager(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
        saveFile = dataDirectory.resolve("saved-run.properties");
        scoreFile = dataDirectory.resolve("high-scores.tsv");
    }

    /** Write the current run's properties to the user's save file. */
    public void save(GameSession session) throws IOException {
        // Create the app's data folder on the first save so the file can be written there.
        Files.createDirectories(dataDirectory);
        Properties values = session.toProperties();
        try (OutputStream output = Files.newOutputStream(saveFile)) {
            values.store(output, "RPG Dungeon Diver saved run");
        }
    }

    /** Read a saved run and report missing or invalid save data as an IOException. */
    public GameSession load() throws IOException {
        // Report a missing save directly instead of opening a path that does not exist.
        if (!Files.isRegularFile(saveFile)) {
            throw new IOException("No saved run was found.");
        }
        Properties values = new Properties();
        try (InputStream input = Files.newInputStream(saveFile)) {
            values.load(input);
        }
        try {
            return GameSession.fromProperties(values);
        } catch (RuntimeException exception) {
            throw new IOException("The saved run is damaged or uses an unsupported format.", exception);
        }
    }

    /** Append a completed run's name and score as one row in the local score file. */
    public void recordScore(String name, int score) throws IOException {
        Files.createDirectories(dataDirectory);
        // Replace row separators in names so one diver cannot corrupt the tab-separated score rows.
        String safeName = name.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ').trim();
        if (safeName.isEmpty()) {
            safeName = "Diver";
        }
        Files.write(scoreFile, Collections.singletonList(safeName + "\t" + score),
                StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    /** Return up to five scores sorted from highest to lowest, skipping invalid rows. */
    public List<String> readHighScores() throws IOException {
        if (!Files.isRegularFile(scoreFile)) {
            return Collections.emptyList();
        }
        List<ScoreEntry> scores = new ArrayList<ScoreEntry>();
        // Parse each tab-separated row independently so one malformed entry does not stop the list.
        for (String line : Files.readAllLines(scoreFile, StandardCharsets.UTF_8)) {
            String[] fields = line.split("\t", 2);
            if (fields.length != 2) {
                continue;
            }
            try {
                scores.add(new ScoreEntry(fields[0], Integer.parseInt(fields[1])));
            } catch (NumberFormatException exception) {
                // A non-numeric score invalidates this row, but the following rows can still be read.
            }
        }
        Collections.sort(scores, new Comparator<ScoreEntry>() {
            @Override
            public int compare(ScoreEntry first, ScoreEntry second) {
                return Integer.compare(second.score, first.score);
            }
        });
        // Build display-ready strings for only the top five entries shown in the menu.
        List<String> rows = new ArrayList<String>();
        for (int index = 0; index < Math.min(5, scores.size()); index++) {
            ScoreEntry entry = scores.get(index);
            rows.add(String.format("%d. %-18s %d", index + 1, entry.name, entry.score));
        }
        return rows;
    }

    private static final class ScoreEntry {
        private final String name;
        private final int score;

        private ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }
}