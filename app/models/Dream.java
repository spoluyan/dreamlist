package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(name = "dreams")
public class Dream extends Model {
    @ManyToOne
    public User user;

    @Required
    @MaxSize(value = 255)
    public String dream;
    public Date added;
    public boolean isDone;
    public boolean isPrivate;

    public Dream(User user, String dream, boolean isPrivate) {
        super();
        this.user = user;
        this.dream = dream;
        this.added = new Date();
        this.isPrivate = isPrivate;
    }
}
