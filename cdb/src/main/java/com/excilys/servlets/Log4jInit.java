package com.excilys.servlets;


import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;

public class Log4jInit extends HttpServlet {

    @Override
    public void init() {
        Properties properties = new Properties();
        properties.setProperty("log4j.rootLogger", "TRACE,stdout,MyFile");
        properties.setProperty("log4j.rootCategory", "TRACE");

        properties.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        properties.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        properties.setProperty("log4j.appender.stdout.layout.ConversionPattern",
            "%d{yyyy/MM/dd HH:mm:ss.SSS} [%5p] %t (%F) - %m%n");

        properties.setProperty("log4j.appender.MyFile", "org.apache.log4j.RollingFileAppender");
        properties.setProperty("log4j.appender.MyFile.File", "my_example.log");
        properties.setProperty("log4j.appender.MyFile.MaxFileSize", "100KB");
        properties.setProperty("log4j.appender.MyFile.MaxBackupIndex", "1");
        properties.setProperty("log4j.appender.MyFile.layout", "org.apache.log4j.PatternLayout");
        properties.setProperty("log4j.appender.MyFile.layout.ConversionPattern",
            "%d{yyyy/MM/dd HH:mm:ss.SSS} [%5p] %t (%F) - %m%n");

        PropertyConfigurator.configure(properties);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
    }
}
