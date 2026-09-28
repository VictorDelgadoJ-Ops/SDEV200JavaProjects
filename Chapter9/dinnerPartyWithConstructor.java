//victor delgado
//p. 334

public class dinnerPartyWithConstructor extends PartyWithContructor {
    private int dinnerChoice;

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

