package rpgdungeon;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Application entry point. */
public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        // Swing components should be created on its event thread.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Use the computer's normal window style when it is available.
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception exception) {
                    // Swing's built-in look and feel is an acceptable fallback.
                }
                // Build and display the main game window.
                new GameWindow().setVisible(true);
            }
        });
    }
}