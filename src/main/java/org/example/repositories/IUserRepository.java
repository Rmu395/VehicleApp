package org.example.repositories;

import org.example.accounts.User;

import java.util.List;

public interface IUserRepository {
    public User getUser(String login);
    public List<User> getUsers();
    public void save();
}
