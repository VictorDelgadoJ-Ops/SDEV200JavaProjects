//Victor Delgado
// p.133
import java.util.Scanner;

public class TestSandwich {
    public static void main(String[] args) {
    Sandwich firstSandwich = new Sandwich();
    Sandwich secondSandwich = new Sandwich();
    Sandwich thirdSandwich = new Sandwich("Ham", "Whole Wheat", 22.99);
        Scanner keyboard = new Scanner(System.in);
    firstSandwich = getData(firstSandwich, keyboard);
    secondSandwich = getData(secondSandwich, keyboard);
    System.out.println("First sandwich details: ");
    System.out.println(firstSandwich.getIngredient() + " " +
        firstSandwich.getBreadType() + " $" + firstSandwich.getPrice());
    System.out.println("Second sandwich details: ");
    System.out.println(secondSandwich.getIngredient() + " " +
        secondSandwich.getBreadType() + " $" + secondSandwich.getPrice());
    System.out.println("Third sandwich details: ");
    System.out.println(thirdSandwich.getIngredient() + " " +
        thirdSandwich.getBreadType() + " $" + thirdSandwich.getPrice());
    }
    public static Sandwich getData(Sandwich service, Scanner keyboard) {
        String ingredient;
        String breadType;
        double price;
        System.out.print("Enter the name of the ingredient >> ");
        ingredient = keyboard.nextLine();
        System.out.print("Enter the bread type >> ");
        breadType = keyboard.nextLine();
        System.out.print("Enter the price of the sandwich >> ");
        price = keyboard.nextDouble();
        keyboard.nextLine();
        service.setIngredient(ingredient);
        service.setBreadType(breadType);
        service.setPrice(price);
        return service;
    }
}
