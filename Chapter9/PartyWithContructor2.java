public class PartyWithContructor2 {
	private int guests;

	public PartyWithContructor2(int numGuests) {
		guests = numGuests;
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
