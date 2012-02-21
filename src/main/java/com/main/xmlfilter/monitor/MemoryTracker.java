package com.main.xmlfilter.monitor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Tracks memory usage throughout the application runtime. It runs as a scheduled task in a separate thread and keeps polling the system for the memory usage while storing the maximum value.
 *
 * @author sergiu.indrie
 */
public class MemoryTracker implements Runnable {

    private static MemoryTracker INSTANCE = new MemoryTracker();

    private long maxUsage = 0;
    private Object lock = new Object();

    // scheduling support
    private ScheduledFuture<?> trackingTask;
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private MemoryTracker() {

    }

    public static MemoryTracker getInstance() {
        return INSTANCE;
    }

    /**
     * Keeps polling the runtime for memory usage.
     */
    public void run() {
        // TODO read about gc idea
        long usage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        synchronized (lock) {
            if (maxUsage < usage) {
                maxUsage = usage;
            }
        }
    }

    /**
     * Starts memory usage tracking.
     */
    public void startTracking() {
        reset();
        trackingTask = scheduler.scheduleAtFixedRate(this, 0, 10, TimeUnit.MILLISECONDS);
    }

    public void stopTracking() {
        trackingTask.cancel(true);
    }

    /**
     * Shutdown the scheduler.
     */
    public void shutdown() {
        scheduler.shutdownNow();
    }

    /**
     * Get the maximum memory usage.
     *
     * @return the maximum memory usage.
     */
    public long getMaxUsage() {
        return maxUsage;
    }

    /**
     * Reset the usage information.
     */
    public void reset() {
        synchronized (lock) {
            maxUsage = 0;
        }
    }
}
