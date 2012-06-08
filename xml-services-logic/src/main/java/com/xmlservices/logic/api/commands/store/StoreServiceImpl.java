package com.xmlservices.logic.api.commands.store;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author Sergiu Indrie
 */
public class StoreServiceImpl implements StoreService {

    private static final Logger log = Logger.getLogger(StoreServiceImpl.class);

    @Override
    public void importXml(String filePath) throws FileNotFoundException, InvalidContentException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        importXml(fileInputStream);
    }

    @Override
    public void importXml(InputStream xmlInputStream) throws InvalidContentException{

    }
}
