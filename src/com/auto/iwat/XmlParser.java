package com.auto.iwat;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

//import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class XmlParser {
	public void xmlParser() throws Exception {

		Properties props = PropertyFile.propertyFile();
		Properties mandatoryProp = PropertyFile.xmlMandatoryPropFile();
		XpathTest xPathTestObj = new XpathTest();
		HttpHandler sendReqObj = new HttpHandler();

		Logger logger = WriteLogEntriesToLogFile
				.WriteLogEntriesToLogFile(props.getProperty("logFileXML") + "xmlLog" + ".log", 1);

		String[][] keyValue = xPathTestObj.getPath(props.getProperty("xPathXml"));

		int rowCount = keyValue.length;
		int colCount = keyValue[1].length;
		int cnt = 0;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		Document doc = null;
		Document docTemp = null;
		Document docFetch = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(props.getProperty("actualXml"));
			// Create XPathFactory object
			XPathFactory xpathFactory = XPathFactory.newInstance();

			// Create XPath object
			XPath xpath = xpathFactory.newInstance().newXPath();

			UniversalNamespaceCache setNamespace = new UniversalNamespaceCache(doc, false);
			xpath.setNamespaceContext(setNamespace);

			boolean flag = true;
			XpathTest setCelVal = new XpathTest();
			String name = "";
			Random randomObj = new Random();
			int random = 0;
			String ranStr = "";
			String resCode = "";
			String resMsg = "";
			int size = 0;
			//String fileType = "application/soap+xml";
			String fileType = "text/xml";
//			int select = 0;
//			Scanner scan = new Scanner(System.in);
//			System.out.println(
//					"\n Please select from the below options : \n 1 --> To run each xPath as separate Test Case \n 2 --> To run all xPath as single Test Case \n");
//			select = scan.nextInt();

			int select = Integer.parseInt(props.getProperty("testType"));
			switch (select) {
			case 1: {
				for (int i = 0; i < rowCount; i++) {
					Timestamp startTime = new Timestamp(System.currentTimeMillis());
					cnt = 1;
					if (keyValue[i][3] != null && keyValue[i][4] != null) {
						if (keyValue[i][3].equals("Yes")) {
							String logPath = (props.getProperty("logFileXML") + "TC_" + keyValue[i][0] + "_"
									+ startTime.toGMTString().replace(':', '-') + ".log");
							logger = WriteLogEntriesToLogFile.WriteLogEntriesToLogFile(logPath, i);
							Set<Object> keys = mandatoryProp.keySet();
							if (keys.size() > 0) {
								random = randomObj.nextInt(100000);
								ranStr = random + "";
								for (Object k : keys) {
									String key = (String) k;
									System.out.println(" * MandotoryValue_" + (cnt++) + " = " + ranStr);
									logger.info(" * MandotoryValue_" + (cnt++) + " = " + ranStr);
									getXpathResult(doc, xpath, mandatoryProp.getProperty(key), ranStr, keyValue[i][0],
											props.getProperty("tempMandatoryXml"));
								}
							} else {
								FileSystem system = FileSystems.getDefault();
								Path original = system.getPath(props.getProperty("actualXml"));
								Path target = system.getPath(props.getProperty("tempMandatoryXml"));
								CopyOption replace;
								Files.copy(original, target, REPLACE_EXISTING);
							}

							if (flag) {
								System.out.println("\n  Connecting to server, please wait......... \n");
								logger.info("\n  Connecting to server, please wait......... \n");
								flag = false;
							}
							docTemp = builder.parse(props.getProperty("tempMandatoryXml"));
							getXpathResult(docTemp, xpath, keyValue[i][2], keyValue[i][4], keyValue[i][0],
									(props.getProperty("tempActualXml") + "testCase_" + keyValue[i][0] + ".xml"));
							HashMap<Integer, String> response = sendReqObj.getResponse(props.getProperty("soapUrl"),
									(props.getProperty("tempActualXml") + "testCase_" + keyValue[i][0] + ".xml"),
									(props.getProperty("responseXml") + "testResponse_" + keyValue[i][0] + ".xml"),
									keyValue[i][5], fileType);
							Iterator it = response.entrySet().iterator();
							size = response.size();

							while (it.hasNext()) {
								Map.Entry pair = (Map.Entry) it.next();
								resCode = pair.getKey().toString();
								resMsg = pair.getValue().toString();
							}

							if ((resCode).equals(keyValue[i][6].toString())) {
								docFetch = builder.parse(props.getProperty("responseXml") + "testResponse_" + keyValue[i][0] + ".xml");
								if (keyValue[i][9].equalsIgnoreCase(getXpathResponse(docTemp, xpath, keyValue[i][8]))) {
									XpathTest.setCellValue(i, 7, resCode, props.getProperty("xPathXml"));
									XpathTest.setCellValue(i, 10, keyValue[i][9], props.getProperty("xPathXml"));
									XpathTest.setCellValue(i, 11, "Pass", props.getProperty("xPathXml"));
									System.out.println(" * Test case " + keyValue[i][0]
											+ " Executed successfully with Response  " + resCode + " " + resMsg
											+ " And Successful Response outbound fetched "
											+ "\n ****************************************************************** \n");
									logger.info(" * Test case " + keyValue[i][0]
											+ " Executed successfully with Response  " + resCode + " " + resMsg
											+ " And Successful Response outbound fetched "
											+ "\n ****************************************************************** \n");
								}else {
									XpathTest.setCellValue(i, 7, resCode, props.getProperty("xPathXml"));
									//XpathTest.setCellValue(i, 10, getXpathResponse(docTemp, xpath, keyValue[i][8]), props.getProperty("xPathXml"));
									XpathTest.setCellValue(i, 11, "Fail", props.getProperty("xPathXml"));
									System.out.println(" * Test case " + keyValue[i][0]
											+ " Executed successfully with Response  " + resCode + " " + resMsg
											+ " But was unsuccessful to fetch the response outbound"
											+ "\n ****************************************************************** \n");
									logger.info(" * Test case " + keyValue[i][0]
											+ " Executed successfully with Response  " + resCode + " " + resMsg
											+ " But was unsuccessful to fetch the response outbound"
											+ "\n ****************************************************************** \n");
								}
								
							} else {
								XpathTest.setCellValue(i, 7, resCode + "", props.getProperty("xPathXml"));
								XpathTest.setCellValue(i, 9, resMsg + "", props.getProperty("xPathXml"));
								XpathTest.setCellValue(i, 10, "Fail", props.getProperty("xPathXml"));
								System.out.println(" * Test Case " + keyValue[i][0] + " failed with Response " + resCode
										+ " " + resMsg
										+ "\n ****************************************************************** \n");
								logger.info(" * Test Case " + keyValue[i][0] + " failed with Response " + resCode + " "
										+ resMsg
										+ "\n ****************************************************************** \n");
							}

						}
					}
				}
				break;
			}

			case 2: {
				Set<Object> keys = mandatoryProp.keySet();
				System.out.println("Size of mandatory property = " + keys.size());
				logger.info("Size of mandatory property = " + keys.size());

				if (keys.size() > 0) {
					random = randomObj.nextInt(100000);
					ranStr = random + "";
					for (Object k : keys) {
						String key = (String) k;
						System.out.println("MandatoryID = " + ranStr);
						logger.info("MandatoryID = " + ranStr);
						getXpathResult(doc, xpath, mandatoryProp.getProperty(key), ranStr, " ",
								props.getProperty("tempMandatoryXml"));
					}
					docTemp = builder.parse(props.getProperty("tempMandatoryXml"));
				}

				System.out.println("\n  Connecting to server, please wait......... \n");
				logger.info("\n  Connecting to server, please wait......... \n");
				for (int i = 0; i < rowCount; i++) {
					if (keyValue[i][3] != null && keyValue[i][4] != null) {
						if (keyValue[i][3].equals("Yes")) {
							getXpathResult(docTemp, xpath, keyValue[i][2], keyValue[i][4], keyValue[i][0],
									(props.getProperty("tempActualXml") + "testCase_" + "SingleXml" + ".xml"));
						}
					}
				}

//				XpathTest.setCellValue(1, 4, ranStr, props.getProperty("modifyXmlSheet")); // to update uniqueId to modify sheet
//				XpathTest.setCellValue(1, 4, ranStr, props.getProperty("cancelXmlSheet")); // to update uniqueId to cancel sheet

				HashMap<Integer, String> response = sendReqObj.getResponse(props.getProperty("soapUrl"),
						(props.getProperty("tempActualXml") + "testCase_" + "SingleXml" + ".xml"),
						(props.getProperty("responseXml") + "testResponse_" + "SingleXml" + ".xml"), "POST", fileType);
				Iterator it = response.entrySet().iterator();
				size = response.size();

				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					resCode = pair.getKey().toString();
					resMsg = pair.getValue().toString();
				}
				if (resCode.equals("200")) {
					System.out.println("\n Test case Executed successfully with Response  " + resCode + " " + resMsg
							+ "\n  For results please check the response xml....Excell sheet won't be updated in this case   \n");
					logger.info("\n Test case Executed successfully with Response  " + resCode + " " + resMsg
							+ "\n  For results please check the response xml....Excell sheet won't be updated in this case   \n");
				} else {
					System.out.println("\n Test Case failed with Response " + resCode + " " + resMsg
							+ "\n For results please check the response xml.Excell sheet won't be updated in this case");
					logger.info("\n Test Case failed with Response " + resCode + " " + resMsg
							+ "\n For results please check the response xml.Excell sheet won't be updated in this case");
				}
				break;
			}
			default: {
				System.out.println("Please enter the correct choice");
				logger.info("Please enter the correct choice");
				break;
			}

			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	public static HashMap<String, String> getNameSpace() throws Exception {
		Properties props = PropertyFile.propertyFile();
		String filePath = props.getProperty("actualXml");
		BufferedReader br;
		String line = "";
		HashMap<String, String> namespace = new HashMap<>();

		try {
			br = new BufferedReader(new FileReader(filePath));
			try {
				while ((line = br.readLine()) != null) {
					String[] words = line.split(" ");

					// ---------------------*** Fetching Prefix and namespaceUrl into hashmap
					// ***------------------------
					for (String w : words) {
						if (w.length() >= 5) {
							if (w.substring(0, 5).equalsIgnoreCase("xmlns")) {

								String str[] = w.split("\"");
								// System.out.println("Arry Length = "+ str.length);
								String nameUrl = str[1];
								String prefix = "";
								try {
									prefix = str[0].split(":|=")[1];
								} catch (ArrayIndexOutOfBoundsException e) {
									prefix = "@noPrefix";
								}
								// System.out.println("Prefix = "+prefix+" NameUrl = "+ nameUrl);
								namespace.put(prefix, nameUrl);

							}
						}
					}

				}
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return namespace;

	}

	private static void getXpathResult(Document doc, XPath xpath, String xPath, String newValue, String testCaseNo,
			String outputFilePath) throws Exception {
		try {

			// ------------------------***Setting NodeList to update
			// xml***--------------------------------------

			NodeList myNodeList = new NodeList() {
				@Override
				public Node item(int index) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public int getLength() {
					// TODO Auto-generated method stub
					return 0;
				}
			};
			if (!newValue.isEmpty())
				myNodeList = (NodeList) xpath.compile(xPath).evaluate(doc, XPathConstants.NODESET);
			// System.out.println(xPath);
			for (int i = 0; i < myNodeList.getLength(); i++) {
				myNodeList.item(i).setNodeValue(newValue);
			}

			// ------------------------***Xpath to fetch data from xml***--------------------------------------
			String name;
			 XPathExpression expr = xpath.compile(xPath);
			 name = expr.evaluate(doc, XPathConstants.STRING).toString();

			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.transform(new DOMSource(doc), new StreamResult(new File(outputFilePath)));

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	private static String getXpathResponse(Document doc, XPath xpath, String xPath) throws Exception {
		String response = "";
		try {

			// ------------------------***Setting NodeList to update xml***-------------------

			NodeList myNodeList = new NodeList() {
				@Override
				public Node item(int index) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public int getLength() {
					// TODO Auto-generated method stub
					return 0;
				}
			};
			myNodeList = (NodeList) xpath.compile(xPath).evaluate(doc, XPathConstants.NODESET);
			// System.out.println(xPath);
			
			// ------------------------***Xpath to fetch data from xml***--------------------------------------
			
			 XPathExpression expr = xpath.compile(xPath);
			 response = expr.evaluate(doc, XPathConstants.STRING).toString();

			

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}
	
	
}
