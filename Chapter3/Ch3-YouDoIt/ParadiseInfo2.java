//Victor Delgado
//p. 98
// Demonstrates a method that accepts parameters and returns a value

import java.util.Scanner;

// Demonstrates a method that accepts parameters and returns a value
public class ParadiseInfo2 {
    public static void main(String[] args) {
        double price;
        double discount;
        double savings;
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Enter cutoff price for the week's sale >> ");
        price = keyboard.nextDouble();
        System.out.print("Enter discount rate as a decimal >> ");
        discount = keyboard.nextDouble();

        displayInfo();
        savings = computeDiscountInfo(price, discount);
        System.out.printf("With a minimum purchase of $%.2f, you will save $%.2f.%n", price, savings);

        keyboard.close();
    }

    public static void displayInfo() {
        System.out.println("Welcome to Paradise!");
        System.out.println("We offer a variety of services to make your stay enjoyable.");
        System.out.println("Our amenities include a spa, pool, and fine dining.");
        System.out.println("We hope you have a wonderful time during your visit.");
    }

    public static double computeDiscountInfo(double price, double discount) {
        return price * discount;
    }
}
