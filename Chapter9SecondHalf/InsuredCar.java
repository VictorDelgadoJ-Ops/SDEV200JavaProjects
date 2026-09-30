//victor delgado
//p. 375

import javax.swing.*;
public class InsuredCar extends Vehicle implements Insured {
    private int coverage;

    public InsuredCar() {
        super("gasoline", 4);
        setCoverage();
    }
    public void setPrice() {
        String entry;
        final int MAX = 1000000;
        entry = JOptionPane.showInputDialog("Enter the price of the car: ");
        price = Integer.parseInt(entry);
        if (price > MAX) {
            price = MAX;
        }
    }
    public InsuredCar(int coverage) {
        this();
    }

    public void setCoverage() {
        coverage = (int) (getPrice() * 0.9);
    }
    public int getCoverage() {
        return coverage;
    }
    public String toString() {
        return ("the insured car is powered by " + getPowerSource() + ", it has " + getWheels() + " wheels, has a price of $" + getPrice() + " and has an insurance coverage of $" + getCoverage());
    }
    
}
