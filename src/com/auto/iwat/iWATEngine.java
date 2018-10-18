package com.auto.iwat;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

import javax.xml.transform.TransformerException;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.io.SocketOutputBuffer;


/**
 * @author Indroneel Sengupta 
 * {@code This is the main class of the application	}
*/
public class iWATEngine {
	
	static String configPath;

	public static void main(String args[]) throws Exception {
//		Class c = Class.forName("iWATEngine");
//		System.out.println("PathName = "+c.getName()+"Package = "+c.getPackage());
		String path = args[0];
		configPath = path;
		Properties props = PropertyFile.propertyFile();
		int select = Integer.parseInt(props.getProperty("serviceType"));
		switch(select){
		case 1 : {
			XmlParser xmlParserObj = new XmlParser();
			try {
				xmlParserObj.xmlParser();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		
		case 2 : {
			JsonParser j = new JsonParser();
			j.jsonParser();
			break;
			
		}
		
		default :{
			System.out.println("It seems you have entered an invalid choice, Please Enter a valid choice");
		}
		
		}
		
		 

	}
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}
