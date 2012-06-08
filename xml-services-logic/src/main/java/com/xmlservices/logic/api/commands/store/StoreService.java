package com.xmlservices.logic.api.commands.store;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Abstraction layer over DB.
 *
 * @author Sergiu Indrie
 */
public interface StoreService {

    /**
     * Imports the xml file's content into the DB and at the same time ensures the imported content's validity.
     *
     * @param filePath the xml file's path
     * @throws InvalidContentException if the file content does not pass the validity check
     * @throws java.io.FileNotFoundException if given file couldn't be found
     */
    void importXml(String filePath) throws FileNotFoundException, InvalidContentException;

    /**
     * Imports the xml file's content into the DB and at the same time ensures the imported content's validity.
     *
     * @param xmlInputStream the xml content's input stream
     * @throws InvalidContentException if the file content does not pass the validity check
     */
    void importXml(InputStream xmlInputStream) throws InvalidContentException;
}
