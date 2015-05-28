package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Dream;
import models.User;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class FriendsLists extends Controller {
    public static void list() {
        User user = User.findById(Security.getUserID());
        Map<String, List<Dream>> friendsLists = new HashMap<>();

        user.friends.forEach(friend -> {
            List<Dream> dreams = Dream.findPublicByUserID(friend.id);
            friendsLists.put(friend.login, dreams);
        });

        render(friendsLists);
    }

    public static void copy(Long dreamId) {
        Dream dream = Dream.findById(dreamId);
        User user = User.findById(Security.getUserID());
        if (dream != null && !dream.isPrivate && user.friends.contains(dream.user)) {
            new Dream(user, dream.dream, false).save();
        }
        ok();
    }
}
