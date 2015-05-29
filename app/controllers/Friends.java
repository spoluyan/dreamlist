package controllers;

import models.User;
import play.mvc.Controller;

public class Friends extends Controller {
    public static void addFriend(String login) {
        User current = User.findById(Security.getUserID());
        User friend = User.findByLogin(login);
        if (!current.friends.contains(friend)) {
            current.friends.add(friend);
            current.save();
        }
        ok();
    }

    public static void deleteFriend(String login) {
        User current = User.findById(Security.getUserID());
        User friend = User.findByLogin(login);
        if (current.friends.contains(friend)) {
            current.friends.remove(friend);
            current.save();
        }
        ok();
    }
}
