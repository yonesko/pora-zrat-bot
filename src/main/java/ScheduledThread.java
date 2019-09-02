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
            try {
                Duration between = Duration.between(Instant.now(), nextWakeupSupplier.get());
                logger.info("Gonna sleep for " + between);
                sleep(between.toMillis());
            } catch (InterruptedException e) {
                logger.error("Interrupted", e);
                return;
            }
            super.run();
        }
    }
}
