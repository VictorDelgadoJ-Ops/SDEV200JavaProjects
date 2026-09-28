//victor delgado
//p. 334

public class DinnerPartyWithConstructor2 extends PartyWithContructor2 {
    private int dinnerChoice;

    public DinnerPartyWithConstructor2(int numGuests) {
        super(numGuests);
    }

    public int getDinnerChoice() {
        return dinnerChoice;
    }
    public void setDinnerChoice(int choice) {
        dinnerChoice = choice;
    }
    @Override 
    public void displayInvitation() {
        System.out.println("You are invited to a dinner party!");
    }
}

