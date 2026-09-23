// Victor Delgado
// p.315
public class CarDemo {
    public static void main(String[] args) {
        Car firstCar = new Car(2021, Model.minivan, Color.blue);
        Car secondCar = new Car(2024, Model.convertible, Color.red);
        firstCar.display();
        secondCar.display();
    }
}
