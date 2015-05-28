package controllers;

import java.util.List;

import models.Dream;
import models.User;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class MyList extends Controller {
    public static void list() {
        List<Dream> dreams = Dream.findByUserID(Security.getUserID());
        render(dreams);
    }

    public static void markDream(Long id) {
        Dream dream = Dream.findByIdAndUserID(id, Security.getUserID());
        if (dream != null) {
            dream.isDone = !dream.isDone;
            dream.save();
        }
        ok();
    }

    public static void removeDream(Long id) {
        Dream dream = Dream.findByIdAndUserID(id, Security.getUserID());
        if (dream != null) {
            dream.delete();
        }
        ok();
    }

    public static void updateDream(Long id, String text) {
        Dream dream = Dream.findByIdAndUserID(id, Security.getUserID());
        if (dream != null) {
            if (text.length() > 255) {
                text = text.substring(0, 255);
            }
            dream.dream = text;
            dream.save();
        }
        ok();
    }

    public static void addDream(String text, boolean isPrivate) {
        User user = User.findById(Security.getUserID());
        new Dream(user, text, isPrivate).save();
        list();
    }
}
