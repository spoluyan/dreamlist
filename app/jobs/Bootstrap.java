package jobs;

import models.Dream;
import models.User;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.Crypto;
import play.test.Fixtures;

@OnApplicationStart
// TODO remove it before prod
public class Bootstrap extends Job {
    @Override
    public void doJob() {
        Fixtures.deleteDatabase();

        User user = new User("ninja", Crypto.passwordHash("123")).save();
        new Dream(user, "Do it!", false).save();
        new Dream(user, "Do it againg!", true).save();
    }
}
