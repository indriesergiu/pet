package com.xmlservices.logic.service.commands.xml;

import com.xmlservices.logic.parsers.stax.elements.XmlElement;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Sergiu Indrie
 */
public interface CommandParser {

    public XmlElement getNextElement() throws Exception;

    public void close();
}
