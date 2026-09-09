//Victor Delgado
//Book Store credit practice
//p. 112

import java.util.Scanner;

public class BookStoreCredit {
    public static void main(String[] args) {
        String name;
        double gpa;

        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter the name of the student >> ");
        name = keyboard.nextLine();
        System.out.print("What is this student's gpa? >> ");
        gpa = keyboard.nextDouble();
        computeCredit(name, gpa);
        keyboard.close();
    }

    public static void computeCredit(String name, double gpa) {
        double credit = gpa * 10;
        System.out.printf("%s has a %.1f grade point average and receives a $%.2f bookstore credit.%n",
                name, gpa, credit);
    }
}
