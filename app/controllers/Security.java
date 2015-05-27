package controllers;

import models.User;
import play.libs.Crypto;

//TODO override login page
public class Security extends Secure.Security {
    static boolean authenticate(String username, String password) {
        User user = User.findByLogin(username);
        if (user == null) {
            return false;
        }
        String hash = Crypto.passwordHash(password);
        return user.password.equals(hash);
    }
}
