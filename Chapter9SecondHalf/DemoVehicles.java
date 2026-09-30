import javax.swing.*;
public class DemoVehicles {
    public static void main(String[] args) {
        Sailboat aBoat = new Sailboat(0);
        Bicycle aBike = new Bicycle(0);
        JOptionPane.showMessageDialog(null,
                "\nVehicle descriptions:\n" +
                aBoat.toString() + "\n" + aBike.toString());
    }
}
