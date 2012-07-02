package com.xmlservices.logic.api.commands.store.db;

import com.xmlservices.logic.api.commands.store.model.Attribute;
import com.xmlservices.logic.api.commands.store.model.Element;
import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.Iterator;

/**
 * @author Sergiu Indrie
 */
public class AttributeIterator implements Iterator<Attribute> {

    private static final Logger log = Logger.getLogger(AttributeIterator.class);

    private static final int FIRST_ATTRIBUTE = 0;
    private Store store;
    private Element element;
    private Attribute current;

    public AttributeIterator(Store store, Element element) {
        if (store == null || element == null) {
            throw new IllegalArgumentException(MessageFormat.format("Invalid constructor arguments: store={0} and element={1}", store, element));
        }
        this.store = store;
        this.element = element;
    }

    @Override
    public boolean hasNext() {
        return getNext() != null;
    }

    @Override
    public Attribute next() {
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

    private Attribute getNext() {
        try {
            Attribute attribute;
            if (this.current == null) {
                attribute = store.getAttribute(element.getId(), FIRST_ATTRIBUTE);
            } else {
                attribute = store.getAttribute(element.getId(), current.getNr() + 1);
            }
            return attribute;
        } catch (StoreException e) {
            String errorMessage = MessageFormat.format("An error occurred while performing the read attribute call to the DB using element={0} and current={1}", element, current);
            log.error(errorMessage);
            throw new IllegalStateException(errorMessage, e);
        }
    }
}
