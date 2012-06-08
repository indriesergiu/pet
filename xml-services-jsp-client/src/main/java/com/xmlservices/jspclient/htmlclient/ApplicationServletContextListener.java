package com.xmlservices.jspclient.htmlclient;

import com.xmlservices.jspclient.htmlclient.beans.restclient.XmlServicesClient;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Initializes logging.
 *
 * @author Sergiu Indrie
 */
public class ApplicationServletContextListener implements ServletContextListener {

    // no logging since this is where logging is initialized

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext ctx = event.getServletContext();

        // set property needed for output log file
        System.setProperty("appRootPath", ctx.getRealPath("/"));

        String prefix = ctx.getRealPath("/");
        String webinfDir = "WEB-INF" + System.getProperty("file.separator");
        String file = webinfDir + "logging.properties";

        PropertyConfigurator.configure(prefix + file);

        String configFilePath = prefix + webinfDir + "application.properties";
        Properties properties = new Properties();
        try {
            FileInputStream inStream = new FileInputStream(configFilePath);
            properties.load(inStream);
            inStream.close();
        } catch (IOException e) {
            throw new IllegalStateException("Application properties file could not be loaded.");
        }

        XmlServicesClient.getClient().setServerUrl(properties.getProperty("xml-services-server.url"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }
}
