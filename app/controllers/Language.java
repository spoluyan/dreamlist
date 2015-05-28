package controllers;

import play.i18n.Lang;
import play.mvc.Controller;

public class Language extends Controller {
    public static void chageLanguage(String lang) {
        Lang.change(lang);
        redirect("/my");
    }
}
