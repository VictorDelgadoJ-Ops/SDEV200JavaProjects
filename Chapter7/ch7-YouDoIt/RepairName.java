import javax.swing.*;

public class RepairName {
    public static void main(String[] args) {
        String name = JOptionPane.showInputDialog("Please enter your first and last name");
        if (name == null) {
            return;
        }

        String originalName = name;
        String repairedName = repairName(name);
        JOptionPane.showMessageDialog(null,
            "Original name is " + originalName + "\nRepaired name is " + repairedName);
    }

    public static String repairName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }

        StringBuilder repaired = new StringBuilder();
        for (int i = 0; i < name.length(); ++i) {
            char c = name.charAt(i);

            if (i == 0 || name.charAt(i - 1) == ' ') {
                if (Character.isLetter(c)) {
                    c = Character.toUpperCase(c);
                }
            }
            else if (Character.isLetter(c)) {
                c = Character.toLowerCase(c);
            }

            repaired.append(c);
        }
        return repaired.toString();
    }
}
