package org.example.services;

import org.example.models.User;
import org.example.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;
public class AuthService {

    private final UserRepository userRepo;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public boolean register(String login, String rawPassword, String role) {
        if (userRepo.findByLogin(login).isPresent()) {
            System.out.println("User with that login already exists");
            return false;
        }

        String hashed = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        User user = User.builder()
                .login(login)
                .password(hashed)
                .role(role)
                .build();

        userRepo.save(user);
        System.out.println("Successfully registered");
        return true;
    }

    public Optional<User> login(String login, String rawPassword) {
        return userRepo.findByLogin(login)
                .filter(user -> BCrypt.checkpw(rawPassword, user.getPassword()));
    }

    public void showUserInfo(String userId) {
        Optional<User> user = userRepo.findById(userId);

        if (user.isPresent()) {
            System.out.println("Login: " + user.get().getLogin() +
                    "\nPassword (hashed): " + user.get().getPassword() +
                    "\nRole: " + user.get().getRole() +
                    "\nUser Id: " + user.get().getId());
        }
        else {
            System.out.println("There was a problem with getting user");
        }
    }

    public void showAllUsers() {
        for (User u : userRepo.findAll()) {
            System.out.println(u.toString());
        }
    }
}
