package controllers;

import models.User;
import play.mvc.Controller;

//TODO implement
public class AddFriends extends Controller {
    public static void addFriend(String login) {
        User current = User.findById(Security.getUserID());
        User friend = User.findByLogin(login);
        if (!current.friends.contains(friend)) {
            current.friends.add(friend);
            current.save();
        }
        ok();
    }
}
