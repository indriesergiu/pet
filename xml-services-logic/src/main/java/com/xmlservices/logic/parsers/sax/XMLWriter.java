package com.xmlservices.logic.parsers.sax;

import com.xmlservices.logic.config.Config;
import com.xmlservices.logic.parsers.sax.elements.XMLElement;
import com.xmlservices.logic.parsers.sax.elements.EndElement;
import com.xmlservices.logic.parsers.sax.elements.StartElement;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * Writer of XML files.
 *
 * @author sergiu.indrie
 */
public class XMLWriter {

    private Class lastType;
    private int level = 0;

    public void write(Iterator<XMLElement> iterator, OutputStream outputStream) {
        while (iterator.hasNext()) {
            XMLElement element = iterator.next();
            String text = element.getText();

            if (lastType != null) {
                if (element instanceof StartElement && lastType.equals(StartElement.class)) {
                    level++;
                    text = formatText(text);
                } else if (element instanceof StartElement && lastType.equals(EndElement.class)) {
                    text = formatText(text);
                } else if (element instanceof EndElement && lastType.equals(EndElement.class)) {
                    level--;
                    text = formatText(text);
                }
            }
            lastType = element.getClass();

            try {
                outputStream.write(text.getBytes(Config.ENCODING));
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private String formatText(String text) {
        text = addIndent(text);
        text = "\n" + text;
        return text;
    }

    private String addIndent(String text) {
        for (int i = 0; i < level; i++) {
            text = "  " + text;
        }
        return text;
    }
}
