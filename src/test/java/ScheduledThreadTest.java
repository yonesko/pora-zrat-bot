import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.Test;

/**
 * @author Gleb Danichev
 */
public class ScheduledThreadTest {

    @Test
    public void test() throws InterruptedException {
        StringBuilder sb = new StringBuilder();
        ScheduledThread scheduledThread = new ScheduledThread(
            () -> sb.append(Instant.now().toEpochMilli()),
            () -> Instant.ofEpochMilli(6000),
            Clock.fixed(Instant.ofEpochMilli(5000), ZoneId.systemDefault())
        );


    }
}
