package controllers;

import java.util.List;

import models.Dream;
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
}
