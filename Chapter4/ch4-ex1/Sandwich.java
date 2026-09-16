// Victor Delgado
// p.133

public class Sandwich {
	private String ingredient;
	private String BreadType;
	private double price;

	public Sandwich() {
		this("Turkey", "White", 0.0);
	}

	public Sandwich(String desc) {
		ingredient = desc;
		BreadType = "White";
		price = 0;
	}

	public Sandwich(String ingredient, String breadType, double price) {
		this.ingredient = ingredient;
		this.BreadType = breadType;
		this.price = price;
	}

	public void setIngredient(String ing) {
		ingredient = ing;
	}
	public void setBreadType(String bt) {
		BreadType = bt;
	}

	public void setPrice(double servicePrice) {
		price = servicePrice;
	}

	public String getIngredient() {
		return ingredient;
	}

	public String getBreadType() {
		return BreadType;
	}

	public double getPrice() {
		return price;
	}
}
