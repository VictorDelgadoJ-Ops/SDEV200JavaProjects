//Victor Delgado
//p. 106
// Demonstrates overloaded methods

public class DemoOverload {
    public static void main(String[] args) {
        int month = 6;
        int day = 24;
        int year = 2023;

        displayDate(month);
        displayDate(month, day);
        displayDate(month, day, year);
    }

    public static void displayDate(int month) {
        System.out.println("Event date " + month + "/1/2024");
    }

    public static void displayDate(int month, int day) {
        System.out.println("Event date " + month + "/" + day + "/2024");
    }

    public static void displayDate(int month, int day, int year) {
        System.out.println("Event date " + month + "/" + day + "/" + year);
    }
}
