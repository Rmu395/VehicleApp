package org.example.model;

public abstract class Vehicle {
    String brand;
    String model;
    int year;
    float price;
    boolean rented;
    int vehicleId;
    public String toCSV() {
        return brand + ";" +
                model + ";" +
                year + ";" +
                price + ";" +
                rented + ";" +
                vehicleId + ";";
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
                '}';
    }

    @Override
    public int hashCode() {
        String stringPrice = Float.toString(this.price);
        int hashOfPrice = stringPrice.hashCode();
        String stringRented = Boolean.toString(this.rented);
        int hashOfRented = stringRented.hashCode();

        return this.brand.hashCode() +
                this.model.hashCode() +
                this.year +
                hashOfPrice +
                hashOfRented +
                this.vehicleId;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass()) &&
                this.getBrand().equals(((Car) obj).getBrand()) &&
                this.getModel().equals(((Car) obj).getModel()) &&
                this.getPrice() == ((Car) obj).getPrice() &&
                this.getYear() == ((Car) obj).getYear() &&
                this.isRented() == ((Car) obj).isRented() &&
                this.getVehicleId() == ((Car) obj).getVehicleId();
    }

    public Vehicle(String brand, String model, int year, float price, boolean rented, int vehicleId) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.rented = rented;
        this.vehicleId = vehicleId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public float getPrice() {
        return price;
    }

    public boolean isRented() {
        return rented;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }
}
