package com.xmlservices.logic.monitor;

/**
 * Creates Memory Tracker objects.
 *
 * @author sergiu.indrie
 */
public class MemoryTrackerFactory {

    public static MemoryTracker getMemoryTracker(MemoryTrackerType type) {
        switch (type) {
            case RUNTIME:
                return RuntimeMemoryTracker.getInstance();
            case MX:
                return MXBeanMemoryTracker.getInstance();
        }
        throw new IllegalArgumentException("no tracker defined for " + type);
    }
}
