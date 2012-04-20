package com.main.htmlclient;

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

    public void contextInitialized(ServletContextEvent event) {
        ServletContext ctx = event.getServletContext();

        // set property needed for output log file
        System.setProperty("appRootPath", ctx.getRealPath("/"));

        String prefix = ctx.getRealPath("/");
        String file = "WEB-INF" + System.getProperty("file.separator") + "logging.properties";

        PropertyConfigurator.configure(prefix + file);
        System.out.println("Log4J Logging started for application: " + prefix + file);
    }

    public void contextDestroyed(ServletContextEvent event) {

    }
}
