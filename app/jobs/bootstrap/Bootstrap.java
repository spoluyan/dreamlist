package jobs.bootstrap;

import java.util.stream.IntStream;

import models.Dream;
import models.User;
import play.Play;
import play.Play.Mode;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.Crypto;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job {
    private static final String LONG_TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum faucibus velit eget elit tempus blandit. Cras finibus ipsum ac bibendum placerat. Cras imperdiet neque sapien, in laoreet ante euismod quis. Suspendisse luctus est lectus, vel congue sed.";
    private static final String SHORT_TEXT = "Lorem ipsum dolor sit amet.";

    @Override
    public void doJob() {
        if (Play.mode == Mode.DEV) {
            Fixtures.deleteDatabase();

            User user = new User("ninja", Crypto.passwordHash("123")).save();
            user.save();

            IntStream.range(0, 10).forEach(i -> new Dream(user, LONG_TEXT, i % 2 == 0).save());

            IntStream.range(0, 10).forEach(
                    i -> {
                        User u = new User("test" + i, Crypto.passwordHash("" + i)).save();

                        IntStream.range(0, 10).forEach(
                                j -> new Dream(u, i % 2 == 0 ? LONG_TEXT : SHORT_TEXT, j % 2 == 0).save());
                        u.friends.add(user);
                        u.save();

                        user.friends.add(u);
                        user.save();
                    });
        }
    }
}
