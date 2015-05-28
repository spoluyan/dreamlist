package controllers;

import java.util.List;

import models.Dream;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class MyList extends Controller {
    public static void list() {
        Long userID = Security.getUserID();
        List<Dream> dreams = Dream.findByUserID(userID);
        render(dreams);
    }

    public static void markDream(Long id) {
        Dream dream = Dream.findById(id);
        if (dream != null && dream.user.id.equals(Security.getUserID())) {
            dream.isDone = !dream.isDone;
            dream.save();
        }
        ok();
    }
}
