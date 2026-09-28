public class PartyWithContructor{
	private int guests;

	public PartyWithContructor() {
		System.out.println("Creating a party with a constructor.");
	}

	public int getGuests() {
		return guests;
	}

	public void setGuests(int numGuests) {
		guests = numGuests;
	}

	public void displayInvitation() {
		System.out.println("Please come to my party!");
	}
}
