//Victor Delgado
//p. 81 ex8 Sep 2, 2026

import java.util.Scanner;
public class ChiliToGo {
    public static void main(String[] args) {
        int noAdultMeals;
        int noChildMeals;
        double priceAdultMeal = 7.0;
        double priceChildMeal = 4.0;
        double totalAdultMeal = 0.0;
        double totalChildMeal = 0.0;
        double totalSales = 0.0;
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the number of adult meals: ");
        noAdultMeals = input.nextInt();
        System.out.print("Enter the number of child meals: ");
        noChildMeals = input.nextInt();
        totalAdultMeal = noAdultMeals * priceAdultMeal;
        totalChildMeal = noChildMeals * priceChildMeal;
        totalSales = totalAdultMeal + totalChildMeal;
        System.out.printf("Total sales for adult meals: $%.2f%n", totalAdultMeal);
        System.out.printf("Total sales for child meals: $%.2f%n", totalChildMeal);
        System.out.printf("Total sales: $%.2f%n", totalSales);
        input.close();
    }
}
