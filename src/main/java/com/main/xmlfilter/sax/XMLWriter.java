package com.main.xmlfilter.sax;

import com.main.xmlfilter.sax.elements.EndElement;
import com.main.xmlfilter.sax.elements.StartElement;
import com.main.xmlfilter.sax.elements.XMLElement;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Stack;

/**
 * Writer of XML files.
 *
 * @author sergiu.indrie
 */
public class XMLWriter {

    private static final String ENCODING = "UTF-8";
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
                outputStream.write(text.getBytes(ENCODING));
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
            text = "\t" + text;
        }
        return text;
    }
}
