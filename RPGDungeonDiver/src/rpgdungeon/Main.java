package rpgdungeon;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Application entry point. */
public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception exception) {
                    // Swing's built-in look and feel is an acceptable fallback.
                }
                new GameWindow().setVisible(true);
            }
        });
    }
}