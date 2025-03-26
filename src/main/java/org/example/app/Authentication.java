package org.example.app;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.repositories.IUserRepository;
import org.example.repositories.IVehicleRepository;

import java.util.Objects;

public class Authentication {
    public static boolean loggingIn(IUserRepository userRepository, String login, String password) {
        if (Objects.equals(userRepository.getUser(login).getPassword(), DigestUtils.sha256Hex(password))) {
            return true;
        }
        else {
            System.out.println("Wrong password");
            return false;
        }
    }
}
