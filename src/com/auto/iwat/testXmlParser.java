package com.auto.iwat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author Indroneel Sengupta
 *
 */
public class testXmlParser {
	public static void test() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse("demo.xml");

			// Create XPathFactory object
			XPathFactory xpathFactory = XPathFactory.newInstance();

			// Create XPath object
			XPath xpath = xpathFactory.newXPath();

			String name1 = getXmlParserResult(doc, xpath);
			System.out.println("Employee Name with No Id: " + name1);

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * This method uses xpath libraries and provides the necessary changes in the xml file using its provided xPath
	 * @author Indroneel Sengupta
	 * @param doc Document object
	 * @param xpath XPath object
	 * @return Returns the updated string with all the changes done to it.
	 */
	private static String getXmlParserResult(Document doc, XPath xpath) {
		String name = null;
		try {
			XPathExpression expr =
			// xpath.compile("/Employees/Employee/name/text()");
			xpath.compile("//name/text()");
			name = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return name;
	}
}
