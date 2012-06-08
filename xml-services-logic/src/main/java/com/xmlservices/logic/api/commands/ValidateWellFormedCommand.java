package com.xmlservices.logic.api.commands;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.Reader;

/**
 * Validates that a given XML file is well-formed.
 *
 * @author Sergiu Indrie
 */
public class ValidateWellFormedCommand implements Command {

    private Reader reader;
    private boolean wellFormed = true;

    public ValidateWellFormedCommand(Reader reader) {
        this.reader = reader;
    }

    @Override
    public void execute() throws Exception {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(reader);
        try {
            while (xmlStreamReader.hasNext()) {
                xmlStreamReader.next();
            }
        } catch (XMLStreamException e) {
            wellFormed = false;
        }
    }

    public boolean isWellFormed() {
        return wellFormed;
    }
}
