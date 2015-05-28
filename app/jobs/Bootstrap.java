package jobs;

import java.util.stream.IntStream;

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
        user.save();

        IntStream
                .range(0, 100)
                .forEach(
                        i -> new Dream(
                                user,
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum faucibus velit eget elit tempus blandit. Cras finibus ipsum ac bibendum placerat. Cras imperdiet neque sapien, in laoreet ante euismod quis. Suspendisse luctus est lectus, vel congue sed.",
                                i % 2 == 0).save());
    }
}
