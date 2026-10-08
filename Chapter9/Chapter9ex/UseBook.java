import java.util.Scanner;

public class UseBook {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Enter the title of a fiction book >> ");
        Book fictionBook = new Fiction(keyboard.nextLine());

        System.out.print("Enter the title of a nonfiction book >> ");
        Book nonfictionBook = new NonFiction(keyboard.nextLine());

        displayBook(fictionBook);
        displayBook(nonfictionBook);
    }

    private static void displayBook(Book book) {
        System.out.printf("%s: %s; price $%.2f%n",
                book instanceof Fiction ? "Fiction" : "Nonfiction",
                book.getTitle(), book.getPrice());
    }
}
