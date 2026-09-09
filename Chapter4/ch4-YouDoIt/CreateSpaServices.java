//Victor Delgado
// p.133
import java.util.Scanner;

public class CreateSpaServices {
    public static void main(String[] args) {
    SpaService firstService = new SpaService();
    SpaService secondService = new SpaService();
    SpaService thirdService = new SpaService("facial", 22.99);
        Scanner keyboard = new Scanner(System.in);
    firstService = getData(firstService, keyboard);
    // secondService = getData(secondService, keyboard);
    System.out.println("First service details: ");
    System.out.println(firstService.getServiceDescription() +
        " $" + firstService.getPrice());
    System.out.println("Second service details: ");
    System.out.println(secondService.getServiceDescription() +
        " $" + secondService.getPrice());
    System.out.println("Third service details: ");
    System.out.println(thirdService.getServiceDescription() +
        " $" + thirdService.getPrice());
    }
    public static SpaService getData(SpaService service, Scanner keyboard) {
        String serviceDescription;
        double price;
        System.out.print("Enter the name of the service >> ");
        serviceDescription = keyboard.nextLine();
        System.out.print("Enter the price of the service >> ");
        price = keyboard.nextDouble();
        keyboard.nextLine();
        service.setServiceDescription(serviceDescription);
        service.setPrice(price);
        return service;
    }
}
