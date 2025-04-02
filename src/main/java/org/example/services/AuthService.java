package org.example.services;

import org.example.models.User;
import org.example.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthService {
    public static Optional<User> login(UserRepository userRepository, String login, String password) {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isPresent()) {
            if (BCrypt.checkpw(password, user.get().getPassword())) {
                return user;
            }
        }
        return Optional.empty();
    }

    public static Optional<User> register(UserRepository userRepository, String login, String password) {
        if (userRepository.findByLogin(login).isEmpty()) {
            User user = User.builder()
                    .login(login)
                    .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                    .role("USER")
                    .build();   // userId is handled in userRepository.save() (in case we dont handle it here so im gonna use that one)
                                // the role is "USER" - it wouldnt make much sense if we allowed people to create admin accounts
            userRepository.save(user);
            return Optional.ofNullable(user);
        }
        return Optional.empty();
    }
}
