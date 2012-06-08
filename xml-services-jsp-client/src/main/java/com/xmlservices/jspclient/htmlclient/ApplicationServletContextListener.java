package com.xmlservices.jspclient.htmlclient;

import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Initializes logging.
 *
 * @author Sergiu Indrie
 */
public class ApplicationServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext ctx = event.getServletContext();

        // set property needed for output log file
        System.setProperty("appRootPath", ctx.getRealPath("/"));

        String prefix = ctx.getRealPath("/");
        String file = "WEB-INF" + System.getProperty("file.separator") + "logging.properties";

        PropertyConfigurator.configure(prefix + file);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }
}
