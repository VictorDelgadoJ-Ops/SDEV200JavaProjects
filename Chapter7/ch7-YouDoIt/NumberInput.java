//Victor Delgado
//p. 252

import javax.swing.*;

public class NumberInput {
    public static void main(String[] args) {
        String inputString;
        int inputNumber;
        int result;
        final int factor = 10;

        inputString = JOptionPane.showInputDialog("Enter a number");
        inputNumber = Integer.parseInt(inputString);
        result = inputNumber * factor;
        JOptionPane.showMessageDialog(null,
            inputNumber + " * " + factor + " = " + result);
    }
}
