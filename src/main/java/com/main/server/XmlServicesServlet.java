package com.main.server;

import com.main.xmlfilter.service.XmlService;
import com.main.xmlfilter.service.XmlServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Sergiu Indrie
 */
public class XmlServicesServlet extends HttpServlet {

//    Conclusion: More servlets ....unless see how filter are applied

    // and if you use more servlets then you got a Command pattern going on ... unnecessary
    // but more servlets means each servlet does not know about view URIs etc. -> cleaner



    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {

        String requestURI = req.getRequestURI();


//        try {
//            InputStream inputStream = req.getServletContext().getResourceAsStream(DMOZ_DATA_FILE);
//            // consider the need for thread safety
//            resp.getOutputStream().print(XmlService.getPage(2, inputStream));
//        } catch (XmlServiceException e) {
//            // todo handle exception and send user an error (http error status code etc.)
//        }
    }
}
