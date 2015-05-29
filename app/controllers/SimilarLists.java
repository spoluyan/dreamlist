package controllers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import models.Dream;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class SimilarLists extends Controller {
    public static void list() {
        List<Dream> dreams = Dream.findWithSimilar(Security.getUserID());
        dreams.forEach(dream -> {
            Map<Long, Set<Long>> usersWithSimilarDreams = dream.convertSimilarDreams();
        });
        render(dreams);
    }
}
