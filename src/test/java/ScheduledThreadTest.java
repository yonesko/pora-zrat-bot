import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Gleb Danichev
 */
public class ScheduledThreadTest {

    @Test
    public void testWakeUpCount() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger();
        final int interval = 500;
        ScheduledThread scheduledThread = new ScheduledThread(
            counter::incrementAndGet,
            () -> Instant.now().plusMillis(interval)
        );
        scheduledThread.start();
        final int count = 10;
        Thread.sleep(interval * count + interval / 2);
        scheduledThread.interrupt();
        Assert.assertEquals(count, counter.get());
    }
}
