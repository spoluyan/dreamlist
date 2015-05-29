package controllers;

import java.util.Set;
import java.util.stream.Collectors;

import models.User;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Friends extends Controller {
    public static void search() {
        render();
    }

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

    public static void performSearch(String query) {
        User current = User.findById(Security.getUserID());
        Set<String> result = User.search(query).stream().filter(user -> !current.friends.contains(user))
                .map(user -> user.login).collect(Collectors.toSet());
        renderJSON(result);
    }
}
