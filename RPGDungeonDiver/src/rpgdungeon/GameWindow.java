package rpgdungeon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/** Main Swing window, connecting player input and game state to the display. */
@SuppressWarnings("serial")
public final class GameWindow extends JFrame {
    private static final Color BACKGROUND = new Color(19, 28, 25);
    private static final Color PANEL = new Color(29, 41, 36);
    private static final Color TEXT = new Color(229, 232, 216);
    private static final Color MUTED = new Color(153, 169, 153);
    private static final Color ACCENT = new Color(113, 204, 172);
    private static final Color GOLD = new Color(231, 179, 89);

    private final SaveManager saveManager = new SaveManager();
    private GameSession session;
    private JComboBox<Difficulty> difficultyChoice;
    private DungeonView dungeonView;
    private JPanel viewFrame;
    private JLabel nameLabel;
    private JLabel depthLabel;
    private JLabel statLabel;
    private JLabel potionLabel;
    private JLabel enemyLabel;
    private JProgressBar healthBar;
    private JProgressBar experienceBar;
    private JTextArea combatLog;
    private JButton saveButton;
    private boolean scoreRecorded;

    public GameWindow() {
        super("RPG Dungeon Diver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1030, 700));
        setSize(1210, 790);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND);
        showMainMenu();
    }

    private void showGame() {
        buildInterface();
        installKeyboardControls();
        refreshDisplay();
    }

    private void showMainMenu() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND);
        root.setBorder(new EmptyBorder(44, 56, 44, 56));

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("RPG / DUNGEON DIVER");
        title.setForeground(TEXT);
        title.setFont(new Font("Consolas", Font.BOLD, 34));
        JLabel subtitle = new JLabel("A TURN-BASED DESCENT INTO THE DEEP");
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("Consolas", Font.PLAIN, 14));
        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(10));
        titleBlock.add(subtitle);
        root.add(titleBlock, BorderLayout.NORTH);

        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(55, 0, 0, 0));
        menu.add(sectionLabel("CHOOSE YOUR DESCENT"));
        menu.add(Box.createVerticalStrut(10));
        difficultyChoice = new JComboBox<Difficulty>(Difficulty.values());
        difficultyChoice.setSelectedItem(session == null
                ? Difficulty.MEDIUM : session.getDifficulty());
        difficultyChoice.setMaximumSize(new Dimension(310, 38));
        difficultyChoice.setAlignmentX(LEFT_ALIGNMENT);
        difficultyChoice.setBackground(PANEL);
        difficultyChoice.setForeground(TEXT);
        difficultyChoice.setFont(new Font("Consolas", Font.BOLD, 13));
        menu.add(difficultyChoice);
        menu.add(Box.createVerticalStrut(12));

        JButton newRunButton = actionButton("NEW DIVE", ACCENT);
        newRunButton.addActionListener(event -> startNewRun());
        menu.add(newRunButton);
        if (session != null) {
            menu.add(Box.createVerticalStrut(7));
            JButton continueButton = actionButton("CONTINUE CURRENT RUN", TEXT);
            continueButton.setEnabled(!session.isGameOver());
            continueButton.addActionListener(event -> showGame());
            menu.add(continueButton);
        }
        menu.add(Box.createVerticalStrut(7));
        JButton loadButton = actionButton("LOAD SAVED RUN", GOLD);
        loadButton.addActionListener(event -> loadRun());
        menu.add(loadButton);
        menu.add(Box.createVerticalStrut(7));
        JButton scoresButton = actionButton("HALL OF THE FALLEN", TEXT);
        scoresButton.addActionListener(event -> showHighScores());
        menu.add(scoresButton);
        menu.add(Box.createVerticalStrut(7));
        JButton exitButton = actionButton("EXIT", MUTED);
        exitButton.addActionListener(event -> dispose());
        menu.add(exitButton);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(menu, BorderLayout.WEST);
        JLabel depthMark = new JLabel("DESCEND  /  SURVIVE  /  RETURN");
        depthMark.setForeground(new Color(104, 132, 115));
        depthMark.setFont(new Font("Consolas", Font.BOLD, 12));
        depthMark.setHorizontalAlignment(SwingConstants.RIGHT);
        center.add(depthMark, BorderLayout.SOUTH);
        root.add(center, BorderLayout.CENTER);
        setContentPane(root);
        revalidate();
        repaint();
    }

    private void buildInterface() {
        JPanel root = new JPanel(new BorderLayout(16, 14));
        root.setBackground(BACKGROUND);
        root.setBorder(new EmptyBorder(18, 20, 16, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("RPG / DUNGEON DIVER");
        title.setForeground(TEXT);
        title.setFont(new Font("Consolas", Font.BOLD, 23));
        JLabel subtitle = new JLabel("A turn-based descent into the deep");
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("Consolas", Font.PLAIN, 12));
        titles.add(title);
        titles.add(Box.createVerticalStrut(4));
        titles.add(subtitle);
        header.add(titles, BorderLayout.WEST);
        depthLabel = new JLabel();
        depthLabel.setForeground(GOLD);
        depthLabel.setFont(new Font("Consolas", Font.BOLD, 14));
        depthLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        header.add(depthLabel, BorderLayout.EAST);

        dungeonView = new DungeonView(session);
        viewFrame = new JPanel(new BorderLayout());
        viewFrame.setBackground(BACKGROUND);
        viewFrame.setBorder(BorderFactory.createLineBorder(new Color(54, 75, 64)));
        viewFrame.add(dungeonView, BorderLayout.CENTER);

        root.add(header, BorderLayout.NORTH);
        root.add(viewFrame, BorderLayout.CENTER);
        root.add(buildSidebar(), BorderLayout.EAST);
        root.add(buildFooter(), BorderLayout.SOUTH);
        setContentPane(root);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(PANEL);
        sidebar.setBorder(new EmptyBorder(15, 15, 15, 15));
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        nameLabel = sectionTitle("DIVER");
        sidebar.add(nameLabel);
        statLabel = bodyLabel("");
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(statLabel);
        sidebar.add(Box.createVerticalStrut(13));
        sidebar.add(sectionLabel("VITALS"));
        healthBar = createBar(new Color(192, 94, 77));
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(healthBar);
        experienceBar = createBar(new Color(98, 175, 143));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(sectionLabel("EXPERIENCE"));
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(experienceBar);

        sidebar.add(Box.createVerticalStrut(16));
        enemyLabel = sectionLabel("");
        sidebar.add(enemyLabel);
        potionLabel = bodyLabel("");
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(potionLabel);
        JLabel legend = bodyLabel("@ YOU   g GOBLIN   s SKELETON   w WRAITH");
        legend.setFont(new Font("Consolas", Font.PLAIN, 10));
        sidebar.add(Box.createVerticalStrut(7));
        sidebar.add(legend);

        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(sectionLabel("ACTIONS"));
        sidebar.add(Box.createVerticalStrut(8));
        JButton attackButton = actionButton("ATTACK", ACCENT);
        attackButton.addActionListener(event -> performAction(() -> session.attack()));
        JButton potionButton = actionButton("DRINK POTION", new Color(222, 140, 116));
        potionButton.addActionListener(event -> performAction(() -> session.drinkPotion()));
        JButton descendButton = actionButton("DESCEND STAIRS", GOLD);
        descendButton.addActionListener(event -> performAction(() -> session.descend()));
        sidebar.add(attackButton);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(potionButton);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(descendButton);

        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(sectionLabel("RUN"));
        sidebar.add(Box.createVerticalStrut(8));
        JPanel fileButtons = new JPanel(new GridLayout(2, 2, 6, 6));
        fileButtons.setOpaque(false);
        JButton newButton = smallButton("NEW");
        newButton.addActionListener(event -> showMainMenu());
        saveButton = smallButton("SAVE");
        saveButton.addActionListener(event -> saveRun());
        JButton loadButton = smallButton("LOAD");
        loadButton.addActionListener(event -> loadRun());
        JButton scoresButton = smallButton("SCORES");
        scoresButton.addActionListener(event -> showHighScores());
        JButton menuButton = smallButton("MENU");
        menuButton.addActionListener(event -> showMainMenu());
        fileButtons.add(newButton);
        fileButtons.add(saveButton);
        fileButtons.add(loadButton);
        fileButtons.add(scoresButton);
        fileButtons.add(menuButton);
        sidebar.add(fileButtons);

        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(sectionLabel("FIELD NOTES"));
        sidebar.add(Box.createVerticalStrut(7));
        combatLog = new JTextArea(7, 20);
        combatLog.setEditable(false);
        combatLog.setLineWrap(true);
        combatLog.setWrapStyleWord(true);
        combatLog.setForeground(new Color(203, 214, 197));
        combatLog.setBackground(new Color(21, 31, 27));
        combatLog.setFont(new Font("Consolas", Font.PLAIN, 11));
        combatLog.setBorder(new EmptyBorder(8, 8, 8, 8));
        JScrollPane logScroll = new JScrollPane(combatLog);
        logScroll.setBorder(BorderFactory.createLineBorder(new Color(55, 72, 61)));
        logScroll.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(logScroll);
        return sidebar;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        JLabel controls = new JLabel("MOVE  WASD / ARROWS     ATTACK  SPACE     POTION  H     STAIRS  E");
        controls.setForeground(MUTED);
        controls.setFont(new Font("Consolas", Font.PLAIN, 11));
        footer.add(controls, BorderLayout.WEST);
        JLabel goal = new JLabel("CLEAR THE FLOOR. SURVIVE THE DESCENT.");
        goal.setForeground(new Color(183, 192, 169));
        goal.setFont(new Font("Consolas", Font.BOLD, 10));
        footer.add(goal, BorderLayout.EAST);
        return footer;
    }

    private void installKeyboardControls() {
        InputMap inputs = dungeonView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        bindMove(inputs, "UP", "moveUp", 0, -1);
        bindMove(inputs, "W", "moveUpW", 0, -1);
        bindMove(inputs, "DOWN", "moveDown", 0, 1);
        bindMove(inputs, "S", "moveDownS", 0, 1);
        bindMove(inputs, "LEFT", "moveLeft", -1, 0);
        bindMove(inputs, "A", "moveLeftA", -1, 0);
        bindMove(inputs, "RIGHT", "moveRight", 1, 0);
        bindMove(inputs, "D", "moveRightD", 1, 0);
        bindAction(inputs, "SPACE", "attack", () -> session.attack());
        bindAction(inputs, "H", "potion", () -> session.drinkPotion());
        bindAction(inputs, "E", "stairs", () -> session.descend());
    }

    private void bindMove(InputMap inputs, String key, String actionName, int dx, int dy) {
        bindAction(inputs, key, actionName, () -> session.move(dx, dy));
    }

    private void bindAction(InputMap inputs, String key, String actionName, GameAction action) {
        inputs.put(KeyStroke.getKeyStroke(key), actionName);
        dungeonView.getActionMap().put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                performAction(action);
            }
        });
    }

    private void performAction(GameAction action) {
        if (!session.isGameOver()) {
            action.run();
            refreshDisplay();
        }
    }

    private void refreshDisplay() {
        Player player = session.getPlayer();
        nameLabel.setText(player.getName().toUpperCase());
        depthLabel.setText(String.format("DEPTH  %02d", session.getDepth()));
        statLabel.setText("LEVEL " + player.getLevel() + "     GOLD " + player.getGold()
            + "     SCORE " + session.getScore() + "     " + session.getDifficulty());
        healthBar.setMaximum(player.getMaximumHealth());
        healthBar.setValue(player.getHealth());
        healthBar.setString(player.getHealth() + " / " + player.getMaximumHealth() + " HP");
        experienceBar.setMaximum(player.getNextLevelExperience());
        experienceBar.setValue(player.getExperience());
        experienceBar.setString(player.getExperience() + " / " + player.getNextLevelExperience() + " XP");
        enemyLabel.setText("HOSTILES  " + session.getFloor().getEnemyCount());
        potionLabel.setText("POTIONS  " + player.getPotions() + "     TURNS  " + session.getTurns());
        StringBuilder log = new StringBuilder();
        for (String message : session.getMessages()) {
            if (log.length() > 0) {
                log.append('\n');
            }
            log.append(message);
        }
        combatLog.setText(log.toString());
        combatLog.setCaretPosition(combatLog.getDocument().getLength());
        saveButton.setEnabled(!session.isGameOver());
        dungeonView.repaint();

        if (session.isGameOver() && !scoreRecorded) {
            scoreRecorded = true;
            try {
                saveManager.recordScore(player.getName(), session.getScore());
            } catch (IOException exception) {
                showError("Your run ended, but its score could not be recorded.", exception);
            }
            JOptionPane.showMessageDialog(this,
                    "Run ended on floor " + session.getDepth() + ". Final score: " + session.getScore(),
                    "The dungeon claims another", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void startNewRun() {
        String name = JOptionPane.showInputDialog(this, "Name your diver:", "New Run",
                JOptionPane.QUESTION_MESSAGE);
        if (name == null) {
            return;
        }
        name = name.trim();
        if (name.isEmpty()) {
            name = "Diver";
        }
        if (name.length() > 18) {
            name = name.substring(0, 18);
        }
        Difficulty difficulty = difficultyChoice == null
            ? session == null ? Difficulty.MEDIUM : session.getDifficulty()
            : (Difficulty) difficultyChoice.getSelectedItem();
        session = new GameSession(name, System.nanoTime(), difficulty);
        scoreRecorded = false;
        showGame();
    }

    private void saveRun() {
        try {
            saveManager.save(session);
            JOptionPane.showMessageDialog(this, "Run saved on this computer.",
                    "Save complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException exception) {
            showError("The run could not be saved.", exception);
        }
    }

    private void loadRun() {
        try {
            session = saveManager.load();
            scoreRecorded = false;
            showGame();
        } catch (IOException exception) {
            showError("The saved run could not be loaded.", exception);
        }
    }

    private void showHighScores() {
        try {
            java.util.List<String> scores = saveManager.readHighScores();
            String text = scores.isEmpty() ? "No completed runs yet."
                    : String.join("\n", scores);
            JOptionPane.showMessageDialog(this, text, "Hall of the Fallen",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException exception) {
            showError("High scores could not be read.", exception);
        }
    }

    private void showError(String message, IOException exception) {
        JOptionPane.showMessageDialog(this, message + "\n" + exception.getMessage(),
                "Dungeon Diver", JOptionPane.ERROR_MESSAGE);
    }

    private JProgressBar createBar(Color color) {
        JProgressBar bar = new JProgressBar();
        bar.setStringPainted(true);
        bar.setForeground(color);
        bar.setBackground(new Color(17, 25, 22));
        bar.setBorderPainted(false);
        bar.setFont(new Font("Consolas", Font.BOLD, 10));
        bar.setAlignmentX(LEFT_ALIGNMENT);
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        return bar;
    }

    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(new Font("Consolas", Font.BOLD, 16));
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(GOLD);
        label.setFont(new Font("Consolas", Font.BOLD, 11));
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    private JLabel bodyLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(MUTED);
        label.setFont(new Font("Consolas", Font.PLAIN, 11));
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    private JButton actionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        button.setPreferredSize(new Dimension(240, 34));
        button.setBackground(new Color(39, 57, 48));
        button.setForeground(color);
        button.setFont(new Font("Consolas", Font.BOLD, 11));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(68, 96, 76)),
                new EmptyBorder(5, 8, 5, 8)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton smallButton(String text) {
        JButton button = actionButton(text, TEXT);
        button.setPreferredSize(new Dimension(105, 30));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        button.setFont(new Font("Consolas", Font.BOLD, 10));
        return button;
    }

    private interface GameAction {
        boolean run();
    }
}