package com.main.xmlfilter.commands.xml;

import com.main.xmlfilter.parsers.stax.elements.XmlElement;

import java.io.OutputStream;
import java.util.Stack;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Sergiu Indrie
 */
public interface XmlWriter {

    void write(Stack<XmlElement> elements, OutputStream outputStream) throws Exception;
}
