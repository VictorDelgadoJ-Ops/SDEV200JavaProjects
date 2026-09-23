import java.util.*;
import javax.swing.*;

public class MenuSearch {
    public static void main(String[] args) {
        String[] menuChoices = new String[10];
        String entry = "", menuString = "";
        int x = 0;
        int numEntered;
        int highestSub = menuChoices.length - 1;

        Arrays.fill(menuChoices, "zzzzzz");
        menuChoices[x] = JOptionPane.showInputDialog(null,
            "Enter an item for today's menu, or zz to quit");

        while (!menuChoices[x].equals("zz") && x < highestSub) {
            menuString = menuString + menuChoices[x] + "\n";
            ++x;
            if (x < highestSub)
                menuChoices[x] = JOptionPane.showInputDialog(null,
                    "Enter an item for today's menu, or zz to quit");
        }

        numEntered = x;
        entry = JOptionPane.showInputDialog(null,
            "Today's menu is:\n" + menuString +
            "Please make a selection");
        Arrays.sort(menuChoices, 0, numEntered);
        x = Arrays.binarySearch(menuChoices, entry);
        if (x >= 0 && x < numEntered)
            JOptionPane.showMessageDialog(null,
                "Excellent choice! " + entry + " is on the menu");
        else
            JOptionPane.showMessageDialog(null,
                "Your selection is not on the menu. Please try again");
    }
}
