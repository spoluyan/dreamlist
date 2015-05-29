package models;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Entity
@Table(name = "dreams")
public class Dream extends Model {
    @Transient
    public static final Gson GSON = new Gson();

    @ManyToOne
    public User user;

    @Required
    @MaxSize(value = 255)
    public String dream;
    public Date added;
    public boolean isDone;
    public boolean isPrivate;
    public String guessedLanguage;
    @Lob
    public String similarDreams;
    @Transient
    public boolean markedAsSimilar;

    public Dream(User user, String dream, boolean isPrivate) {
        super();
        this.user = user;
        this.dream = dream;
        this.added = new Date();
        this.isPrivate = isPrivate;
    }

    public static List<Dream> findByUserID(Long userID) {
        return find("user.id = ? ORDER BY isDone ASC", userID).fetch();
    }

    public static List<Dream> findPublicByUserID(Long userID) {
        return find("user.id = ? AND isPrivate = ? ORDER BY isDone ASC", userID, false).fetch();
    }

    public static Dream findByIdAndUserID(Long id, Long userID) {
        return find("id = ? AND user.id = ?", id, userID).first();
    }

    public static List<Dream> findByLanguageIsNull() {
        return find("byGuessedLanguageIsNull").fetch();
    }

    public static List<Dream> findAllPublicWithLanguage() {
        return find("byIsPrivateAndGuessedLanguageIsNotNull", false).fetch();
    }

    public static List<Dream> findWithSimilar(Long userID) {
        return find("user.id = ? AND similarDreams IS NOT NULL", userID).fetch();
    }

    public Map<Long, Set<Long>> convertSimilarDreams() {
        if (similarDreams == null) {
            return null;
        }
        Type type = new TypeToken<Map<Long, Set<Long>>>() {
        }.getType();
        return GSON.fromJson(similarDreams, type);
    }

    public void convertSimilarDreams(Map<Long, Set<Long>> dreams) {
        this.similarDreams = GSON.toJson(dreams);
    }

    @Override
    public String toString() {
        return dream;
    }
}
