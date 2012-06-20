package com.xmlservices.logic.api.commands.store.db;

/**
 * An error that occurred while performing a store operation.
 *
 * @author Sergiu Indrie
 */
public class StoreException extends Exception {

    public StoreException() {
    }

    public StoreException(String message) {
        super(message);
    }

    public StoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public StoreException(Throwable cause) {
        super(cause);
    }
}
