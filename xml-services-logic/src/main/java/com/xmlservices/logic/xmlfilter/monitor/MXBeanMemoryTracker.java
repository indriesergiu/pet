package com.xmlservices.logic.xmlfilter.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

/**
 * JVM memory tracker implemented with MemoryMXBean
 *
 * @author sergiu.indrie
 * @see java.lang.management.MemoryMXBean
 */
public class MXBeanMemoryTracker extends MemoryTracker {

    private static MXBeanMemoryTracker INSTANCE = new MXBeanMemoryTracker();

    private MXBeanMemoryTracker() {
    }

    public static MXBeanMemoryTracker getInstance() {
        return INSTANCE;
    }

    // start off with empty values
    private Usage heapMemoryUsage = new Usage();
    private Usage nonHeapMemoryUsage = new Usage();

    public String getMaxUsage() {
        StringBuilder result = new StringBuilder();
        result.append("\nHeap memory usage\n");
        result.append(heapMemoryUsage.toMemoryUsage());
        result.append("\nNon-Heap memory usage\n");
        result.append(nonHeapMemoryUsage.toMemoryUsage());
        return result.toString();
    }

    public void reset() {
        this.heapMemoryUsage = new Usage();
        this.nonHeapMemoryUsage = new Usage();
    }

    public void run() {
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();

        // update all values that are higher than the stored ones

        // heap memory
        if (this.heapMemoryUsage.committed < heapMemoryUsage.getCommitted()) {
            this.heapMemoryUsage.committed = heapMemoryUsage.getCommitted();
        }
        if (this.heapMemoryUsage.init < heapMemoryUsage.getInit()) {
            this.heapMemoryUsage.init = heapMemoryUsage.getInit();
        }
        if (this.heapMemoryUsage.max < heapMemoryUsage.getMax()) {
            this.heapMemoryUsage.max = heapMemoryUsage.getMax();
        }
        if (this.heapMemoryUsage.used < heapMemoryUsage.getUsed()) {
            this.heapMemoryUsage.used = heapMemoryUsage.getUsed();
        }

        // non heap memory
        if (this.nonHeapMemoryUsage.committed < nonHeapMemoryUsage.getCommitted()) {
            this.nonHeapMemoryUsage.committed = nonHeapMemoryUsage.getCommitted();
        }
        if (this.nonHeapMemoryUsage.init < nonHeapMemoryUsage.getInit()) {
            this.nonHeapMemoryUsage.init = nonHeapMemoryUsage.getInit();
        }
        if (this.nonHeapMemoryUsage.max < nonHeapMemoryUsage.getMax()) {
            this.nonHeapMemoryUsage.max = nonHeapMemoryUsage.getMax();
        }
        if (this.nonHeapMemoryUsage.used < nonHeapMemoryUsage.getUsed()) {
            this.nonHeapMemoryUsage.used = nonHeapMemoryUsage.getUsed();
        }

    }

    private class Usage {
        long init;
        long committed;
        long max;
        long used;

        public MemoryUsage toMemoryUsage() {
            return new MemoryUsage(init, used, committed, max);
        }
    }
}
