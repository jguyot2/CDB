package com.excilys.main;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
		//TODO RELECTURE ET CORRECTION DES NOMS DE MERDE QUE J'AI MIS
	//TODO Refaire la javadoc
	public static void main(String... strings) {
		BasicConfigurator.configure();
		Logger logger = LoggerFactory.getLogger(Main.class);
	    logger.trace("Hello World");
	    logger.debug("Hello World");
	    logger.info("Hello World");
	    logger.warn("Hello World");
	    logger.error("Hello World");
	}
	
	public static void initLog4() {
	}
}
