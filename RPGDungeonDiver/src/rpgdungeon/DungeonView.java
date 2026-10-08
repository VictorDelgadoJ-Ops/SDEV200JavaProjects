package rpgdungeon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/** Paints the dungeon grid, diver, enemies, stairs, and run-ending overlay. */
@SuppressWarnings("serial")
public final class DungeonView extends JPanel {
    private static final Color BACKGROUND = new Color(15, 23, 21);
    private static final Color FLOOR = new Color(36, 48, 43);
    private static final Color FLOOR_ALT = new Color(40, 52, 47);
    private static final Color WALL = new Color(73, 79, 68);
    private static final Color WALL_EDGE = new Color(104, 108, 91);
    private static final Color GOLD = new Color(231, 179, 89);
    private static final Color PLAYER = new Color(100, 219, 192);
    private final GameSession session;

    public DungeonView(GameSession session) {
        this.session = session;
        setBackground(BACKGROUND);
        setPreferredSize(new Dimension(760, 570));
        setMinimumSize(new Dimension(540, 420));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        // Draw with a copy so these graphics settings do not affect Swing.
        Graphics2D canvas = (Graphics2D) graphics.create();
        canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        canvas.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        DungeonFloor floor = session.getFloor();
        int tileSize = Math.min((getWidth() - 48) / floor.getWidth(), (getHeight() - 100) / floor.getHeight());
        tileSize = Math.max(20, tileSize);
        int mapWidth = tileSize * floor.getWidth();
        int mapHeight = tileSize * floor.getHeight();
        int left = (getWidth() - mapWidth) / 2;
        int top = Math.max(72, (getHeight() - mapHeight) / 2 + 18);

        canvas.setColor(new Color(204, 214, 196));
        canvas.setFont(new Font("Consolas", Font.BOLD, 13));
        canvas.drawString(String.format("FLOOR %02d", session.getDepth()), left, top - 30);
        canvas.setColor(new Color(132, 150, 138));
        canvas.setFont(new Font("Consolas", Font.PLAIN, 12));
        canvas.drawString(floor.getEnemyCount() + " HOSTILES REMAIN", left + 112, top - 30);

        // Paint the map tiles first so the characters appear on top of them.
        for (int y = 0; y < floor.getHeight(); y++) {
            for (int x = 0; x < floor.getWidth(); x++) {
                int tileX = left + x * tileSize;
                int tileY = top + y * tileSize;
                char tile = floor.getTile(x, y);
                if (tile == '#') {
                    canvas.setColor(WALL);
                    canvas.fillRoundRect(tileX, tileY, tileSize - 1, tileSize - 1, 4, 4);
                    canvas.setColor(WALL_EDGE);
                    canvas.drawLine(tileX + 4, tileY + 4, tileX + tileSize - 6, tileY + 4);
                } else {
                    canvas.setColor((x + y) % 2 == 0 ? FLOOR : FLOOR_ALT);
                    canvas.fillRect(tileX, tileY, tileSize - 1, tileSize - 1);
                    if (tile == '>') {
                        drawStairs(canvas, tileX, tileY, tileSize);
                    }
                }
            }
        }

        // Draw monsters and then the player so the diver is visible if tiles overlap.
        for (Enemy enemy : floor.getEnemies()) {
            if (enemy != null) {
                drawActor(canvas, left + enemy.getX() * tileSize, top + enemy.getY() * tileSize,
                        tileSize, enemy.getSymbol(), enemyColor(enemy));
            }
        }
        drawActor(canvas, left + session.getPlayerX() * tileSize,
                top + session.getPlayerY() * tileSize, tileSize, '@', PLAYER);

        canvas.setColor(new Color(85, 106, 94));
        canvas.setStroke(new BasicStroke(1.2f));
        canvas.drawRoundRect(left - 6, top - 6, mapWidth + 11, mapHeight + 11, 10, 10);
        if (session.isGameOver()) {
            // Cover the map with a score panel after the diver is defeated.
            drawGameOver(canvas);
        }
        canvas.dispose();
    }

    private void drawStairs(Graphics2D canvas, int x, int y, int size) {
        // Draw a gold set of lines inside the stair tile.
        int inset = Math.max(5, size / 5);
        canvas.setColor(new Color(113, 84, 47));
        canvas.fillRoundRect(x + inset, y + inset, size - inset * 2, size - inset * 2, 5, 5);
        canvas.setColor(GOLD);
        canvas.setStroke(new BasicStroke(2f));
        for (int line = 0; line < 3; line++) {
            int lineY = y + inset + 5 + line * 5;
            canvas.drawLine(x + inset + 4, lineY, x + size - inset - 4, lineY);
        }
    }

    private void drawActor(Graphics2D canvas, int x, int y, int size, char symbol, Color color) {
        // Use a circle and a centered letter to show each game character.
        int inset = Math.max(4, size / 6);
        canvas.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 58));
        canvas.fillOval(x + 2, y + 2, size - 5, size - 5);
        canvas.setColor(color);
        canvas.fillOval(x + inset, y + inset, size - inset * 2, size - inset * 2);
        canvas.setColor(new Color(15, 23, 21));
        canvas.setFont(new Font("Consolas", Font.BOLD, Math.max(14, size / 2)));
        FontMetrics metrics = canvas.getFontMetrics();
        String glyph = Character.toString(symbol);
        canvas.drawString(glyph, x + (size - metrics.stringWidth(glyph)) / 2,
                y + (size - metrics.getHeight()) / 2 + metrics.getAscent());
    }

    private Color enemyColor(Enemy enemy) {
        if (enemy instanceof Wraith) {
            return new Color(203, 138, 224);
        }
        if (enemy instanceof Skeleton) {
            return new Color(232, 196, 124);
        }
        return new Color(238, 124, 102);
    }

    private void drawGameOver(Graphics2D canvas) {
        int panelWidth = Math.min(360, getWidth() - 48);
        int panelHeight = 142;
        int x = (getWidth() - panelWidth) / 2;
        int y = (getHeight() - panelHeight) / 2;
        canvas.setColor(new Color(10, 16, 14, 228));
        canvas.fillRoundRect(x, y, panelWidth, panelHeight, 14, 14);
        canvas.setColor(new Color(190, 102, 79));
        canvas.setStroke(new BasicStroke(2f));
        canvas.drawRoundRect(x, y, panelWidth, panelHeight, 14, 14);
        canvas.setFont(new Font("Consolas", Font.BOLD, 27));
        canvas.setColor(new Color(248, 224, 199));
        centerText(canvas, "RUN ENDED", x + panelWidth / 2, y + 54);
        canvas.setFont(new Font("Consolas", Font.PLAIN, 14));
        canvas.setColor(new Color(193, 204, 190));
        centerText(canvas, "SCORE  " + session.getScore(), x + panelWidth / 2, y + 91);
        centerText(canvas, "Start a new run to dive again", x + panelWidth / 2, y + 116);
    }

    private void centerText(Graphics2D canvas, String text, int centerX, int baselineY) {
        canvas.drawString(text, centerX - canvas.getFontMetrics().stringWidth(text) / 2, baselineY);
    }
}