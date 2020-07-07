package com.excilys.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UrlEncoding {
    private static final String CHARSET = "utf-8";

    private static Logger LOG = LoggerFactory.getLogger(UrlEncoding.class);

    public static String encode(final String message) {
        try {
            return URLEncoder.encode(message, CHARSET);
        } catch (UnsupportedEncodingException e) {
            LOG.info("Unsupported encoding");
            return URLEncoder.encode(message);
        }
    }
}
