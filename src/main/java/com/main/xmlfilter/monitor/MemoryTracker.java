package com.main.xmlfilter.monitor;

import com.main.xmlfilter.config.Config;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * JVM Memory tracker
 *
 * @author sergiu.indrie
 */
public abstract class MemoryTracker implements Runnable {

    // scheduling support
    private ScheduledFuture<?> trackingTask;

    private ScheduledExecutorService scheduler = Config.getInstance().getScheduler();

    /**
     * Starts memory usage tracking.
     */
    public void startTracking() {
        reset();
        trackingTask = scheduler.scheduleAtFixedRate(this, 0, 10, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops the memory usage tracking.
     */
    public void stopTracking() {
        trackingTask.cancel(true);
    }

    /**
     * Get the maximum memory usage.
     *
     * @return the maximum memory usage.
     */
    public abstract String getMaxUsage();

    /**
     * Reset the usage information.
     */
    public abstract void reset();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void run();
}
