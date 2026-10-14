package rpgdungeon;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Starts the Swing application and opens the main game window. */
public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        // Start Swing setup on its event-dispatch thread, which safely handles UI events.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Match the window controls and colors to the operating system when possible.
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception exception) {
                    // If the system style is unavailable, Swing keeps using its default appearance.
                }
                // Create the game window and make it visible after the style has been chosen.
                new GameWindow().setVisible(true);
            }
        });
    }
}