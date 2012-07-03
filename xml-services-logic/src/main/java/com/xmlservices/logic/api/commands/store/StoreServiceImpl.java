package com.xmlservices.logic.api.commands.store;

import com.xmlservices.logic.api.commands.store.db.MySQLStore;
import com.xmlservices.logic.api.commands.store.db.Store;
import com.xmlservices.logic.api.commands.store.db.StoreException;
import com.xmlservices.logic.api.commands.store.model.Attribute;
import com.xmlservices.logic.api.commands.store.model.Element;
import com.xmlservices.logic.api.commands.store.model.File;
import com.xmlservices.logic.api.commands.xml.CommandParser;
import com.xmlservices.logic.api.commands.xml.StAXGenericParser;
import com.xmlservices.logic.api.commands.xml.elements.ElementType;
import com.xmlservices.logic.api.commands.xml.elements.XmlElement;
import com.xmlservices.logic.config.Config;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * @author Sergiu Indrie
 */
public class StoreServiceImpl implements StoreService {

    private static final Logger log = Logger.getLogger(StoreServiceImpl.class);

    private static final String UNKNOWN_FILE_PREFIX = "Unknown - ";

    private Store store;

    public StoreServiceImpl(Store store) {
        this.store = store;
    }

    // TODO sergiu.indrie - Importing should be done synchronized (although the use case does not specify it)

    @Override
    public void importXml(String filePath) throws FileNotFoundException, InvalidContentException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        importXml(fileInputStream);
    }

    @Override
    public void importXml(InputStream xmlInputStream) throws InvalidContentException {
        Reader reader;
        CommandParser commandParser;
        try {
            reader = new InputStreamReader(xmlInputStream, Config.ENCODING);
            commandParser = new StAXGenericParser(reader);
        } catch (Exception e) {
            String message = "Failed to process the input source";
            log.error(message, e);
            throw new IllegalArgumentException(message, e);
        }
        try {
            processImport(commandParser);
        } catch (InvalidContentException e) {
            String message = "Failed to successfully import the content";
            log.error(message, e);
            try {
                store.cleanUp();
            } catch (StoreException e1) {
                log.error(e1);
                throw new IllegalStateException(e1);
            }
            throw new IllegalStateException(message, e);
        } catch (StoreException e) {
            String message = "Failed to successfully import the content";
            log.error(message, e);
            try {
                store.cleanUp();
            } catch (StoreException e1) {
                log.error(e1);
                throw new IllegalStateException(e1);
            }
            throw new IllegalStateException(message, e);
        } catch (Exception e) {
            String message = "Failed to successfully import the content";
            log.error(message, e);
            throw new IllegalStateException(message, e);
        }
    }

    private void processImport(CommandParser commandParser) throws Exception {
        log.debug("Beginning import process..");
        store.startTransaction();

        // create the file
        File file = store.addFile(new File(0, UNKNOWN_FILE_PREFIX + Calendar.getInstance().getTime().toString()));

        // add the elements
        XmlElement element = commandParser.getNextElement();
        Element lastElement = null;
        while (element != null) {

            validateElement(element);
            lastElement = store.addElement(convertToDbElement(file, element, lastElement));
            if (element.getType().equals(ElementType.START_ELEMENT)) {
                addAttributes(element, lastElement);
            }

            element = commandParser.getNextElement();
        }

        store.endTransaction();
        log.debug("Import process finished successfully.");
    }

    private void validateElement(XmlElement element) throws InvalidContentException {
        switch (element.getType()) {
            case START_ELEMENT:
                if (!element.getAttributes().isEmpty()) {
                    for (Map.Entry<String, String> attribute : element.getAttributes().entrySet()) {
                        if (!validContent(attribute.getValue())) {
                            throw new InvalidContentException(MessageFormat.format("Validation error on element {0}. Attribute {1} has invalid value: {2}", element, attribute.getKey(),
                                                                                   attribute.getValue()));
                        }
                    }
                }
                break;
            case DATA:
                if (!validContent(element.getData())) {
                    throw new InvalidContentException(MessageFormat.format("Validation error on element {0}. Data has invalid value: {1}", element, element.getData()));
                }
                break;
        }
    }

    private boolean validContent(String value) {
        // TODO sergiu.indrie - externalize/generalize
        return !value.equals("TheFatalErr0r");
    }

    private void addAttributes(XmlElement element, Element dbElement) throws StoreException {
        if (!element.getAttributes().isEmpty()) {
            int nr = 0;
            for (Map.Entry<String, String> attribute : element.getAttributes().entrySet()) {
                store.addAttribute(new Attribute(-1, dbElement.getId(), nr, attribute.getKey(), attribute.getValue()));
                nr++;
            }
        }
    }

    private Element convertToDbElement(File file, XmlElement element, Element lastElement) {
        Element result = new Element();
        result.setData(element.getData());
        result.setEncoding(element.getEncoding());
        result.setFileId(file.getId());
        result.setLocalname(element.getLocalName());
        result.setNr(lastElement != null ? lastElement.getNr() + 1 : 0);
        result.setPrefix(element.getPrefix());
        result.setType(element.getType().name());
        result.setVersion(element.getVersion());
        return result;
    }

    public static void main(String[] args) throws InvalidContentException, FileNotFoundException {
        BasicConfigurator.configure();
        Store store = new MySQLStore();
        StoreService service = new StoreServiceImpl(store);
        // will work
//        service.importXml("d:\\down\\temp\\books.xml");
        // will fail
        service.importXml("d:\\down\\temp\\books_invalid.xml");
    }
}
