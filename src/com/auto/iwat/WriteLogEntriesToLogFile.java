package com.auto.iwat;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.SimpleFormatter;

import org.apache.log4j.Appender;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.DailyRollingFileAppender;

public class WriteLogEntriesToLogFile {
	
	static { System.setProperty("java.util.logging.SimpleFormatter.format",
            "[%1$tF %1$tT] [%4$-7s] %5$s %n");
	}

	public static Logger WriteLogEntriesToLogFile(String logFilePath,int i) throws Exception {
		Properties props = PropertyFile.propertyFile();
		
		 // creates pattern layout
        PatternLayout layout = new PatternLayout();
        String conversionPattern = "[%p] %d %c %M - %m%n";
        layout.setConversionPattern(conversionPattern);
 
        // creates daily rolling file appender
        DailyRollingFileAppender rollingAppender = new DailyRollingFileAppender();
        rollingAppender.setFile("app.log");
        rollingAppender.setDatePattern("'.'yyyy-MM-dd");
        rollingAppender.setLayout(layout);
        rollingAppender.activateOptions();
        
        // configures the root logger
        org.apache.log4j.Logger rootLogger = org.apache.log4j.Logger.getRootLogger();
        rootLogger.addAppender(rollingAppender);
		
		boolean append = true;
		FileHandler handler = new FileHandler(logFilePath, append);
		
	     
		
		//handler.setFormatter(new MyFormatter());
		SimpleFormatter formatter = new SimpleFormatter();
		//formatter.format('%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n');

		handler.setFormatter(formatter);
		Logger logger = Logger.getLogger("Automation"+i);
		
		//SimpleFormatter.format="%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n";
		
		logger.addHandler(handler);
		logger.setUseParentHandlers(false);
		
//		logger.severe("severe message");
//		logger.warning("warning message");
//		logger.info("info message");
//		logger.config("config message");
//		logger.fine("fine message");
//		logger.finer("finer message");
//		logger.finest("finest message");

		return logger;
	}

}
