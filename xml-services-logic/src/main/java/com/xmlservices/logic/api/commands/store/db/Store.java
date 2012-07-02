package com.xmlservices.logic.api.commands.store.db;

import com.xmlservices.logic.api.commands.store.model.Attribute;
import com.xmlservices.logic.api.commands.store.model.Element;
import com.xmlservices.logic.api.commands.store.model.File;

import java.util.Iterator;

/**
 * Provides basic store functionality (add, get etc.)
 *
 * @author Sergiu Indrie
 */
public interface Store {

    void startTransaction() throws StoreException;

    void endTransaction() throws StoreException;

    void cleanUp() throws StoreException;

    File addFile(File file) throws StoreException;

    Element addElement(Element element) throws StoreException;

    Element getElement(int fileId, int nr) throws StoreException;

    Attribute addAttribute(Attribute attribute) throws StoreException;

    Attribute getAttribute(int elementId, int nr) throws StoreException;

    Iterator<Element> getElementIterator(File file) throws StoreException;

    Iterator<Attribute> getAttributeIterator(Element element) throws StoreException;
}
