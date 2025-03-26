package org.example.repositories;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.accounts.User;
import org.example.model.Car;
import org.example.model.Motorcycle;
import org.example.model.Vehicle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class UserRepository implements IUserRepository{
    private List<User> list = new ArrayList<>();

    public UserRepository() {
        try {
            File file = new File("users.csv");  // need to make one
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] values = data.split(";");
                // 0 - login, 1 - password, 2 - role, 3 - rented car (int)
                this.list.add(new User(
                        values[0],
                        values[1],
                        values[2],
                        Integer.parseInt(values[3])
                ));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUser(String login) {
        for (User user : this.list) {
            if (Objects.equals(user.getLogin(), login)) return user;
        }
        System.out.println("getUser() didn't find a user with specified login, returned the first user from list");
        return this.list.get(0);
    }

    @Override
    public List<User> getUsers() {
        List<User> copyOfList = new ArrayList<>();

        for (User user : this.list) {
            copyOfList.add(new User(
                    user.getLogin(),
                    user.getPassword(),
                    user.getRole(),
                    user.getRentedCar()
            ));
        }

        return copyOfList;
    }

    @Override
    public void save() {
        try {
            FileWriter fileWriter = new FileWriter("users.csv");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (User user : this.list)
            {
                printWriter.println(user.toCSV());
            }
            printWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
