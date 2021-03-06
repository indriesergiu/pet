package com.xmlservices.logic.api.commands.store.db;

import com.xmlservices.logic.api.commands.store.model.Element;
import com.xmlservices.logic.api.commands.store.model.File;
import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.Iterator;

/**
 * @author Sergiu Indrie
 */
public class ElementIterator implements Iterator<Element> {

    private static final Logger log = Logger.getLogger(ElementIterator.class);

    private static final int FIRST_ELEMENT = 0;
    private Store store;
    private File file;
    private Element current;

    public ElementIterator(Store store, File file) {
        if (store == null || file == null) {
            throw new IllegalArgumentException(MessageFormat.format("Invalid constructor arguments: store={0} and file={1}", store, file));
        }
        this.store = store;
        this.file = file;
    }

    @Override
    public boolean hasNext() {
        return getNext() != null;
    }

    @Override
    public Element next() {
        current = getNext();
        return current;
    }

    /**
     * Unsupported
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove operation is not supported.");
    }

    private Element getNext() {
        try {
            Element element;
            if (current == null) {
                element = store.getElement(file.getId(), FIRST_ELEMENT);
            } else {
                element = store.getElement(file.getId(), current.getNr() + 1);
            }
            return element;
        } catch (StoreException e) {
            String errorMessage = MessageFormat.format("An error occurred while performing the read element call to the DB using file={0} and current={1}", file, current);
            log.error(errorMessage);
            throw new IllegalStateException(errorMessage, e);
        }
    }
}
