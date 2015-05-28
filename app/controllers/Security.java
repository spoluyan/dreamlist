package controllers;

import models.User;
import play.libs.Crypto;

//TODO override login page
public class Security extends Secure.Security {
    private static final String USER_ID_SESSION_KEY = "userid";

    static boolean authenticate(String username, String password) {
        User user = User.findByLogin(username);
        if (user == null) {
            return false;
        }
        String hash = Crypto.passwordHash(password);
        return user.password.equals(hash);
    }

    static void onAuthenticated() {
        User user = User.findByLogin(connected());
        session.put(USER_ID_SESSION_KEY, user.id);
    }

    static void onDisconnected() {
        session.remove(USER_ID_SESSION_KEY);
    }

    public static Long getUserID() {
        String id = session.get(USER_ID_SESSION_KEY);
        if (id == null) {
            User user = User.findByLogin(connected());
            session.put(USER_ID_SESSION_KEY, user.id);
            return user.id;
        }
        return Long.parseLong(id);
    }
}
