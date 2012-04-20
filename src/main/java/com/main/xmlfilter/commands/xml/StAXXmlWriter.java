package com.main.xmlfilter.commands.xml;

import com.main.xmlfilter.config.Config;
import com.main.xmlfilter.parsers.stax.elements.XmlElement;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Stack;

/**
 * Writer of XML files.
 *
 * @author Sergiu Indrie
 * @see XmlElement
 */
public class StAXXmlWriter implements XmlWriter {

    @Override
    public void write(Stack<XmlElement> elements, OutputStream outputStream) throws Exception {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, Config.ENCODING);

        // try to resolve indentation problem
        XMLStreamWriter writer = outputFactory.createXMLStreamWriter(outputStreamWriter);

        writer = new javanet.staxutils.IndentingXMLStreamWriter(writer);

        for (XmlElement element : elements) {
            switch (element.getType()) {
                case START_DOCUMENT:
                    writer.writeStartDocument(element.getEncoding(), element.getVersion());
                    break;
                case START_ELEMENT:
                    writer.writeStartElement(element.getPrefix() != null ? element.getPrefix() : "", element.getLocalName(), "");
                    Map<String, String> attributes = element.getAttributes();
                    if (attributes != null && attributes.size() > 0) {
                        for (String attribute : attributes.keySet()) {
                            writer.writeAttribute(attribute, attributes.get(attribute));
                        }
                    }
                    break;
                case DATA:
                    writer.writeCharacters(element.getData());
                    break;
                case COMMENT:
                    writer.writeComment(element.getData());
                    break;
                case END_ELEMENT:
                    writer.writeEndElement();
                    break;
            }
        }

        writer.flush();
        writer.close();
    }
}
