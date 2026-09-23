public class Car {
    private int year;
    private Model model;
    private Color color;

    public Car(int year, Model model, Color color) {
        this.year = year;
        this.model = model;
        this.color = color;
    }

    public void display() {
        System.out.println("Year: " + year + ", Model: " + model + ", Color: " + color);
    }
}
