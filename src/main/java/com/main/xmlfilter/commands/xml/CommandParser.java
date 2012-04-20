package com.main.xmlfilter.commands.xml;

import com.main.xmlfilter.parsers.stax.elements.XmlElement;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Sergiu Indrie
 */
public interface CommandParser {

    public XmlElement getNextElement() throws Exception;

    public void close();
}
