//victor delgado
//p. 334

import java.util.*;
public class useParty {
    public static void main(String[] args) {
        int guests;
        party aParty = new party();
        Scanner keyboard = new Scanner(System.in);
        System.out.print("How many guests will you have? ");
        guests = keyboard.nextInt();
        aParty.setGuests(guests);
        System.out.println("You will have " + aParty.getGuests() + " guests at your party.");
        aParty.displayInvitation();
    }
}
