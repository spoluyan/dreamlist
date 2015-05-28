package controllers;

import models.User;

import org.apache.commons.lang.StringUtils;

import play.libs.Crypto;
import play.mvc.Before;
import play.mvc.Controller;

public class Registration extends Controller {
    @Before
    static void checkAuthentification() {
        if (Security.isConnected()) {
            redirect("/my");
        }
    }

    public static void registration() {
        render();
    }

    public static void register(String login, String password) throws Throwable {
        if (StringUtils.isEmpty(login)) {
            flash.error("Empty username");
            registration();
        }

        if (login.length() > 255) {
            login = login.substring(0, 255);
        }

        if (password == null) {
            password = StringUtils.EMPTY;
        }

        User user = User.findByLogin(login);
        if (user != null) {
            flash.error("Duplicate username");
            registration();
        }

        new User(login.toLowerCase(), Crypto.passwordHash(password)).save();
        redirect("/my");
    }
}
