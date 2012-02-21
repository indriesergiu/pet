package com.main.xmlfilter.monitor;

import com.main.xmlfilter.config.Config;
import com.main.xmlfilter.util.SizePrinter;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Tracks memory usage throughout the application runtime. It runs as a scheduled task in a separate thread and keeps polling the system for the memory usage while storing the maximum value.
 *
 * @author sergiu.indrie
 */
public class RuntimeMemoryTracker extends MemoryTracker {

    private static RuntimeMemoryTracker INSTANCE = new RuntimeMemoryTracker();

    private long maxUsage = 0;

    // scheduling support
    private ScheduledFuture<?> trackingTask;

    private ScheduledExecutorService scheduler = Config.getInstance().getScheduler();

    private RuntimeMemoryTracker() {

    }

    public static RuntimeMemoryTracker getInstance() {
        return INSTANCE;
    }

    /**
     * Keeps polling the runtime for memory usage.
     */
    @Override
    public void run() {
        long usage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        if (maxUsage < usage) {
            maxUsage = usage;
        }
    }


    @Override
    public void startTracking() {
        reset();
        trackingTask = scheduler.scheduleAtFixedRate(this, 0, 10, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stopTracking() {
        trackingTask.cancel(true);
    }

    @Override
    public String getMaxUsage() {
        return SizePrinter.formatSize(maxUsage);
    }

    @Override
    public void reset() {
        maxUsage = 0;
    }
}
