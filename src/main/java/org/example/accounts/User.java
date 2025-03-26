package org.example.accounts;

import org.apache.commons.codec.digest.DigestUtils;

public class User {
    private String login;
    private String password;
    private String role;    // this will differentiate between e.g. an admin and a normal user
    private int rentedCar = -1; // initialized as -1 so that it has a option of not having a rented car
                                // (vehicle ids start at 0)

    public User(String login, String password, String role, int rentedCar) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.rentedCar = rentedCar;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public int getRentedCar() {
        return rentedCar;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", rentedCar=" + rentedCar +
                '}';
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setRentedCar(int rentedCar) {
        this.rentedCar = rentedCar;
    }

    public String toCSV() {
        return this.login + ";" +
                this.password + ";" +
                this.role + ";" +
                this.rentedCar + ";";
    }
}
