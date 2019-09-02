import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Gleb Danichev
 */
public class ScheduledThread extends Thread {

    private final static Logger logger = LogManager.getLogger();

    private final Supplier<Instant> nextWakeupSupplier;

    public ScheduledThread(Runnable runnable, Supplier<Instant> nextWakeupSupplier) {
        super(runnable);
        this.nextWakeupSupplier = nextWakeupSupplier;
    }

    @Override
    public final void run() {
        while (true) {
            Duration between = Duration.between(Instant.now(), nextWakeupSupplier.get());
            if (between.isNegative() || between.isZero()) {
                super.run();
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
}
