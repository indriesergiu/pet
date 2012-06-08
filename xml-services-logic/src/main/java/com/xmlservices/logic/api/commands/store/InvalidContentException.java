package com.xmlservices.logic.api.commands.store;

/**
 * Signals an invalid item that couldn't be imported successfully in the store using the {@link StoreService}.
 *
 * @author Sergiu Indrie
 * @see StoreService
 */
public class InvalidContentException extends Exception {

    public InvalidContentException() {
    }

    public InvalidContentException(String message) {
        super(message);
    }

    public InvalidContentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidContentException(Throwable cause) {
        super(cause);
    }
}
