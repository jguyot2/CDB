package com.excilys.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;

/**
 * Servlet permettant l'initialisation de log4j
 *
 * @author jguyot2
 *
 */
public class Log4jInitServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse res) {

    }

    @Override
    public void init() throws ServletException {
        try (InputStream os = getServletContext().getResourceAsStream("/WEB-INF/classes/log4j.properties");) {
            Properties properties = new Properties();
            properties.load(os);
            PropertyConfigurator.configure(properties);
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }
}
