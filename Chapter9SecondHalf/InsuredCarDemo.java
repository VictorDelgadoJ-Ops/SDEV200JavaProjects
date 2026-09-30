//victor delgado
//p. 375

import javax.swing.*;
public class InsuredCarDemo {
    public static void main(String[] args) {
        InsuredCar car = new InsuredCar();
        car.setCoverage();
        JOptionPane.showMessageDialog(null, car.toString());
    }
}
