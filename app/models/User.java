package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Entity
@Table(name = "users")
public class User extends Model {
    @Required
    @Unique
    @MaxSize(value = 255)
    public String login;

    @Required
    @MaxSize(value = 255)
    public String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static User findByLogin(String login) {
        return find("byLogin", login).first();
    }
}
