package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.Dream;
import models.User;
import play.mvc.Controller;
import play.mvc.With;
import util.Pair;

@With(Secure.class)
public class SimilarLists extends Controller {
    public static void list() {
        List<Dream> dreams = Dream.findWithSimilar(Security.getUserID());
        Map<Pair<String, Boolean>, List<Dream>> usersWithSimilarDreams = new HashMap<>();
        dreams.forEach(dream -> {
            Map<Long, Set<Long>> similarDreams = dream.convertSimilarDreams();

            similarDreams.keySet().forEach(userID -> {
                User user = User.findById(userID);
                if (user != null) {
                    Pair<String, Boolean> userTuple = new Pair<>(user.login, isFriend(user));
                    List<Dream> usersDreams = usersWithSimilarDreams.get(userTuple);
                    if (usersDreams == null) {
                        usersDreams = Dream.findPublicByUserID(userID);
                        usersDreams.forEach(d -> d.markedAsSimilar = similarDreams.get(userID).contains(d.id));
                        usersWithSimilarDreams.put(userTuple, usersDreams);
                    }
                }
            });
        });
        render(usersWithSimilarDreams);
    }

    private static boolean isFriend(User user) {
        User currentUser = User.findById(Security.getUserID());
        return currentUser.friends.contains(user);
    }
}
