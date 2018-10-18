package com.auto.iwat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyFile {
	public static Properties propertyFile() throws Exception {
		File configFile = new File(iWATEngine.configPath+"\\"+"config.properties");
		//System.out.println("Attempting to read from file in: "+configFile.getCanonicalPath());
		FileReader reader = new FileReader(configFile);
		Properties props = new Properties();
		props.load(reader);
		
		String jPathExcel =  (props.getProperty("workspace"))+(props.getProperty("jPathExcel"));
		props.setProperty("jPathExcel", jPathExcel);
		
		String oriJsonFilePath =  (props.getProperty("workspace"))+(props.getProperty("oriJsonFilePath"));
		props.setProperty("oriJsonFilePath", oriJsonFilePath);
		
		String outputJsonFilePath =  (props.getProperty("workspace"))+(props.getProperty("outputJsonFilePath"));
		props.setProperty("outputJsonFilePath", outputJsonFilePath);
		
		String responseJsonFilePath =  (props.getProperty("workspace"))+(props.getProperty("responseJsonFilePath"));
		props.setProperty("responseJsonFilePath", responseJsonFilePath);
		
		String tempMandatoryJson =  (props.getProperty("workspace"))+(props.getProperty("tempMandatoryJson"));
		props.setProperty("tempMandatoryJson", tempMandatoryJson);
		
		String logFileJson =  (props.getProperty("workspace"))+(props.getProperty("logFileJson"));
		props.setProperty("logFileJson", logFileJson);
		
		String logFileXML =  (props.getProperty("workspace"))+(props.getProperty("logFileXML"));
		props.setProperty("logFileXML", logFileXML);
		//System.out.println(logFileXML);

		String xPathXml =  (props.getProperty("workspace"))+(props.getProperty("xPathXml"));
		props.setProperty("xPathXml", xPathXml);
		
		String actualXml =  (props.getProperty("workspace"))+(props.getProperty("actualXml"));
		props.setProperty("actualXml", actualXml);
		
		String responseXml =  (props.getProperty("workspace"))+(props.getProperty("responseXml"));
		props.setProperty("responseXml", responseXml);
		
		String tempMandatoryXml =  (props.getProperty("workspace"))+(props.getProperty("tempMandatoryXml"));
		props.setProperty("tempMandatoryXml", tempMandatoryXml);
		
		String tempActualXml =  (props.getProperty("workspace"))+(props.getProperty("tempActualXml"));
		props.setProperty("tempActualXml", tempActualXml);
		
		

		return props;
	}

	public static Properties xmlMandatoryPropFile() throws Exception {
		File configFile = new File(iWATEngine.configPath+"\\"+"XmlMandatoryField.properties");
		//System.out.println("Attempting to read from file in: "+configFile.getCanonicalPath());
		FileReader reader = new FileReader(configFile);
		Properties props = new Properties();
		props.load(reader);

		return props;
	}
	
	public static Properties jsonMandatoryPropFile() throws Exception {
		File configFile = new File(iWATEngine.configPath+"\\"+"JsonMandatoryField.properties");
		// System.out.println("Attempting to read from file in:
		// "+configFile.getCanonicalPath());
		FileReader reader = new FileReader(configFile);
		Properties props = new Properties();
		props.load(reader);
		
		
		return props;
	}
}
