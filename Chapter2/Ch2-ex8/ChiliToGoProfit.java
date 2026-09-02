//Victor Delgado
//p. 81 ex8 Sep 2, 2026

import java.util.Scanner;
public class ChiliToGoProfit {
    public static void main(String[] args) {
        int noAdultMeals;
        int noChildMeals;
        double priceAdultMeal = 7.0;
        double priceChildMeal = 4.0;
        double costAdultMeal = 4.35;
        double costChildMeal = 3.10;
        double totalAdultMeal = 0.0;
        double totalChildMeal = 0.0;
        double totalSales = 0.0;
        double totalProfitAdult = 0.0;
        double totalProfitChild = 0.0;
        double totalProfit = 0.0;
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the number of adult meals: ");
        noAdultMeals = input.nextInt();
        System.out.print("Enter the number of child meals: ");
        noChildMeals = input.nextInt();
        totalAdultMeal = noAdultMeals * priceAdultMeal;
        totalChildMeal = noChildMeals * priceChildMeal;
        totalSales = totalAdultMeal + totalChildMeal;
        totalProfitAdult = noAdultMeals * (priceAdultMeal - costAdultMeal);
        totalProfitChild = noChildMeals * (priceChildMeal - costChildMeal);
        totalProfit = totalProfitAdult + totalProfitChild;
        System.out.printf("Total sales for adult meals: $%.2f%n", totalAdultMeal);
        System.out.printf("Total sales for child meals: $%.2f%n", totalChildMeal);
        System.out.printf("Total sales: $%.2f%n", totalSales);
        System.out.printf("Total profit for adult meals: $%.2f%n", totalProfitAdult);
        System.out.printf("Total profit for child meals: $%.2f%n", totalProfitChild);
        System.out.printf("Total profit: $%.2f%n", totalProfit);
        input.close();
    }
}
