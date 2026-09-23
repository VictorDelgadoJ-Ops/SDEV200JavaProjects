//Victor Delgado
//p. 232

import javax.swing.*;

public class EvenEntryLoop {
    public static void main(String[] args) {
        final int limit = 100;
        boolean keepGoing = true;

        System.out.print(limit + " is evenly divisible by ");
        while (keepGoing) {
            String entry = JOptionPane.showInputDialog(
                    "Enter a number between 2 and " + limit + ", or cancel to quit.");

            if (entry == null) {
                keepGoing = false;
                continue;
            }

            int number = Integer.parseInt(entry);
            if (number % 2 == 0) {
                System.out.print("This is an even number.");
            } else {
                System.out.print("Not an even number");
            }

            System.out.println();
        }
    }
}
