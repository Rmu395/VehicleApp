package org.example.model;

public class Motorcycle extends Vehicle {
    String category;

    public Motorcycle(String brand, String model, int year, float price, boolean rented, int vehicleId, String category) {
        super(brand, model, year, price, rented, vehicleId);
        this.category = category;
    }

    @Override
    public String toCSV() {
        return brand + ";" +
                model + ";" +
                year + ";" +
                price + ";" +
                rented + ";" +
                vehicleId + ";" +
                category + ";";
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", rented=" + rented +
                ", vehicleId=" + vehicleId +
                ", category=" + category +
                '}';
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this.category.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj))
            return this.getCategory().equals(((Motorcycle) obj).getCategory());
        return false;
    }
}
