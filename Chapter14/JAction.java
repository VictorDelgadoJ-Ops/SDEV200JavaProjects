//victor Delgado
//p. 561

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JAction extends JFrame implements ActionListener {
    private final JLabel label = new JLabel("Name?");
    private final JTextField field = new JTextField(12);
    private final JButton button = new JButton("OK");

    public JAction() {
        super("Action");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        add(label);
        add(field);
        add(button);
        button.addActionListener(this);
        field.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        label.setText("Thank you so much!");
        button.setText("Application done");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JAction frame = new JAction();
                frame.setSize(250, 150);
                frame.setVisible(true);
            }
        });
    }
}
