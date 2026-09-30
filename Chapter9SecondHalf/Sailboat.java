//victor delgado
//p. 355

import javax.swing.*;

public class Sailboat extends Vehicle {
    private int length;

    public Sailboat() {
        super("wind", 0);
        setLength();
    }

    public Sailboat(int length) {
        this();
    }

    public int getLength() {
        return length;
    }

    public void setLength() {
        String entry;
        entry = JOptionPane.showInputDialog("Enter the length of the sailboat in feet: ");
        length = Integer.parseInt(entry);
    }

    @Override
    public void setPrice() {
        String entry;
        final int MAX = 100000;
        entry = JOptionPane.showInputDialog("Enter the price of the sailboat: ");
        price = Integer.parseInt(entry);
        if (price > MAX) {
            price = MAX;
        }
    }

    @Override
    public String toString() {
        return ("the " + getLength() + " foot sailboat is powered by " + getPowerSource() + ", it has " + getWheels() + " wheels" + " and has a price of $" + getPrice());
    }
}
