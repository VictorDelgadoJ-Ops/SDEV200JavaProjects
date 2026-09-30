//victor delgado
//p/ 355
public abstract class Vehicle {
    private String powerSource;
    private int wheels;
    protected int price;
    
    public Vehicle(String powerSource, int wheels) {
        setPowerSource(powerSource);
        setWheels(wheels);
        setPrice();
    }
    public String getPowerSource() {
        return powerSource;
    }
    public int getWheels() {
        return wheels;
    }
    public int getPrice() {
        return price;
    }
    public void setPowerSource(String powerSource) {
        this.powerSource = powerSource;
    }
    public void setWheels(int numWheels) {
        this.wheels = numWheels;
    }
    public abstract void setPrice();
    
}

