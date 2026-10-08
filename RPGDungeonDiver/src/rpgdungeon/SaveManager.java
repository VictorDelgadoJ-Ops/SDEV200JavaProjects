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

/** Reads and writes saved runs and the local high-score table. */
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

    /** Persist the current complete run, including enemy positions and health. */
    public void save(GameSession session) throws IOException {
        // Make sure the save folder exists before writing the properties file.
        Files.createDirectories(dataDirectory);
        Properties values = session.toProperties();
        try (OutputStream output = Files.newOutputStream(saveFile)) {
            values.store(output, "RPG Dungeon Diver saved run");
        }
    }

    /** Load a saved run, reporting missing or damaged files as an IOException. */
    public GameSession load() throws IOException {
        // Give a clear error instead of trying to read a file that is not there.
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

    /** Add one completed run to the persistent score table. */
    public void recordScore(String name, int score) throws IOException {
        Files.createDirectories(dataDirectory);
        // Keep names on one row because the score file uses tabs and newlines as separators.
        String safeName = name.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ').trim();
        if (safeName.isEmpty()) {
            safeName = "Diver";
        }
        Files.write(scoreFile, Collections.singletonList(safeName + "\t" + score),
                StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    /** Return the five highest valid scores, ignoring malformed lines safely. */
    public List<String> readHighScores() throws IOException {
        if (!Files.isRegularFile(scoreFile)) {
            return Collections.emptyList();
        }
        List<ScoreEntry> scores = new ArrayList<ScoreEntry>();
        // Skip a broken row without losing other scores from the file.
        for (String line : Files.readAllLines(scoreFile, StandardCharsets.UTF_8)) {
            String[] fields = line.split("\t", 2);
            if (fields.length != 2) {
                continue;
            }
            try {
                scores.add(new ScoreEntry(fields[0], Integer.parseInt(fields[1])));
            } catch (NumberFormatException exception) {
                // Ignore individual damaged rows so the rest of the table remains available.
            }
        }
        Collections.sort(scores, new Comparator<ScoreEntry>() {
            @Override
            public int compare(ScoreEntry first, ScoreEntry second) {
                return Integer.compare(second.score, first.score);
            }
        });
        // Format only the five highest valid entries for the score dialog.
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