package controllers;

import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
// TODO override error pages
public class Application extends Controller {

    public static void index() {
        redirect("/my");
    }

}