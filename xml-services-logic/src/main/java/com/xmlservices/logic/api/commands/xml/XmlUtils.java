package com.xmlservices.logic.api.commands.xml;

public class XmlUtils {// todo consider adding to abstract class

    public static String getFullName(String prefix, String local) {
        StringBuilder qName = new StringBuilder();
        qName.append(prefix != null ? prefix : "");
        if (prefix != null && !prefix.equals("") && local != null && !local.equals("")) {
            qName.append(":");
        }
        qName.append(local != null ? local : "");
        return qName.toString();
    }
}