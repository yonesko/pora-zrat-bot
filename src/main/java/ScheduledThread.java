import java.time.Duration;
import java.time.Instant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Gleb Danichev
 */
public class ScheduledThread extends Thread {

    private final static Logger logger = LogManager.getLogger();

    private Instant nextWakeup;

    public ScheduledThread(Instant nextWakeup, Runnable runnable) {
        super(runnable);
        this.nextWakeup = nextWakeup;
    }

    @Override
    public void run() {
        while (true) {
            Duration between = Duration.between(Instant.now(), nextWakeup);
            if (between.isNegative() || between.isZero()) {
                super.run();
                nextWakeup = calcNextInvocation();
            } else {
                try {
                    sleep(between.toMillis());
                } catch (InterruptedException e) {
                    logger.error("Interrupted", e);
                    return;
                }
            }
        }
    }

    private Instant calcNextInvocation() {
        return nextWakeup.plus(null);
    }
}
