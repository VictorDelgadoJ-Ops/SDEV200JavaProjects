import java.util.Scanner;

public class BookArray {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        Book[] books = new Book[10];

        for (int i = 0; i < books.length; i++) {
            System.out.print("Enter the title of book " + (i + 1) + " >> ");
            String title = keyboard.nextLine();

            String type;
            while (true) {
                System.out.print("Is the book fiction or nonfiction? Enter F or N >> ");
                type = keyboard.nextLine().trim();
                if (type.equalsIgnoreCase("F") || type.equalsIgnoreCase("N")) {
                    break;
                }
                System.out.println("Please enter F for fiction or N for nonfiction.");
            }

            if (type.equalsIgnoreCase("F")) {
                books[i] = new Fiction(title);
            } else {
                books[i] = new NonFiction(title);
            }
        }

        System.out.println("\nBook details:");
        for (Book book : books) {
            String type = book instanceof Fiction ? "Fiction" : "Nonfiction";
            System.out.printf("%s: %s; price $%.2f%n",
                    type, book.getTitle(), book.getPrice());
        }
    }
}
