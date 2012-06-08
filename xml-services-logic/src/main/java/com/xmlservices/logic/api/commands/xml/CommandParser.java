package com.xmlservices.logic.api.commands.xml;

import com.xmlservices.logic.api.commands.xml.elements.XmlElement;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Sergiu Indrie
 */
public interface CommandParser {

    public XmlElement getNextElement() throws Exception;

    public void close();
}
