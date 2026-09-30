import javax.swing.*;

public class Bicycle extends Vehicle {
    private int wheels;

    public Bicycle() {
        super("a person", 2);
        setWheels();
    }

    public Bicycle(int wheels) {
        this();
    }

    public int getWheels() {
        return wheels;
    }

    public void setWheels() {
        String entry;
        entry = JOptionPane.showInputDialog("Enter the number of wheels on the bicycle: ");
        wheels = Integer.parseInt(entry);
    }

    @Override
    public void setPrice() {
        String entry;
        final int MAX = 10000;
        entry = JOptionPane.showInputDialog("Enter the price of the bicycle: ");
        price = Integer.parseInt(entry);
        if (price > MAX) {
            price = MAX;
        }
    }

    @Override
    public String toString() {
        return ("the " + getWheels() + " wheel bicycle is powered by " + getPowerSource() + " and has a price of $" + getPrice());
    }
}