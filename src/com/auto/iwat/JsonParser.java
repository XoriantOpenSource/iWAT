package com.auto.iwat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.io.SocketOutputBuffer;
import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.json.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.awt.print.Book;
/**
 * 
 * @author Indroneel Sengupta
 *
 */

public class JsonParser {

	private static final Configuration configuration = Configuration.builder()
			.jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();
	/**
	 * jsonParser This is the method in which all the json related activities are taken place
	 * @author Indroneel Sengupta
	  
	 * @throws Exception
	 */
	public void jsonParser() throws Exception {
		
		Properties props = PropertyFile.propertyFile();
		Properties mandatoryProp = PropertyFile.jsonMandatoryPropFile();
		XpathTest xPathTestObj = new XpathTest();
		String[][] keyValue = xPathTestObj.getPath(props.getProperty("jPathExcel"));
		int rowCount = keyValue.length;
		int colCount = keyValue[1].length;
		HttpHandler sendReqObj = new HttpHandler();
		String fileType = "application\\json";
		int select = Integer.parseInt(props.getProperty("testType"));
		boolean flag = true;
		boolean flag1 = true;
		XpathTest setCelVal = new XpathTest();
		String name = "";
		Random randomObj = new Random();
		int random = 0;
		String ranStr = "";
		String resCode = "";
		String resMsg = "";
		int size = 0;
		String jsonStr = "";
		String jsonResponseStr = "";
		String checkResponse = "";
		int cnt = 1;
		String responseContent = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		boolean resFlag = false;
		HashMap<Integer, String> logPathForHtmlLink = new HashMap<>();
		
		List<ExcelSetter> excelSetterList = new ArrayList<ExcelSetter>();
		ExcelSetter eList = null;
		

		switch (select) {
		case 1: {

			//try {
				for (int i = 0; i < rowCount; i++) {
					Timestamp startTime = new Timestamp(System.currentTimeMillis());
					cnt = 1;
					if (keyValue[i][0] != null && keyValue[i][0] != "") {
						if(keyValue[i][4] == null || keyValue[i][4].equalsIgnoreCase("")) {
							keyValue[i][4] = "NoAuth";
						}
						if (keyValue[i][8].equalsIgnoreCase("POST") || keyValue[i][8].equalsIgnoreCase("PUT")) {
							if (keyValue[i][3 + 3] != null && keyValue[i][4 + 3] != null) {
								if (keyValue[i][3 + 3].equalsIgnoreCase("Yes")) {
									
									String logPath = (props.getProperty("logFileJson") + "TC_" + keyValue[i][0] +"_"+startTime.toGMTString().replace(':', '-').replace(' ', '_')
											+ ".log");
									logPathForHtmlLink.put(i, logPath);
									Logger logger = WriteLogEntriesToLogFile.WriteLogEntriesToLogFile(logPath, i);
									if (flag) {
										System.out.println("\n  Connecting to server, please wait......... \n");
										logger.info("\n  Connecting to server, please wait......... \n");
										flag = false;
									}

									List<String[]> csvAllRows = CSVParser.csvParser(keyValue[i][7]);

									for (int rowIterator = 0; rowIterator < csvAllRows.size(); rowIterator++) {
										String jPathSplitter[] = keyValue[i][5].split(",");
										boolean check = true;
										String data = "";
										for (int colIterator = 0; colIterator < jPathSplitter.length; colIterator++) {
											// mandatory fields
											Set<Object> keys = mandatoryProp.keySet();
											flag1 = true;
											if (keys.size() > 0) {
												random = randomObj.nextInt(100000);
												ranStr = random + "";
												for (Object k : keys) {
													String key = (String) k;
													System.out.println(" * MandotoryValue_" + (cnt++) + " = " + ranStr);
													logger.info(" * MandotoryValue_" + (cnt++) + " = " + ranStr);
													if (flag1) {
														setJsonPathResult(keyValue[i][3],
																props.getProperty("tempMandatoryJson"),
																mandatoryProp.getProperty(key), ranStr);
														flag1 = false;
													}
													setJsonPathResult(props.getProperty("tempMandatoryJson"),
															props.getProperty("tempMandatoryJson"),
															mandatoryProp.getProperty(key), ranStr);
												}
											} else {

												FileSystem system = FileSystems.getDefault();
												Path original = system.getPath(keyValue[i][3]);
												Path target = system.getPath(props.getProperty("tempMandatoryJson"));
												//CopyOption replace;
												Files.copy(original, target, REPLACE_EXISTING);

											}
											// end of mandatory fields

											System.out.println(" * Path-" + (colIterator + 1) + "= "
													+ jPathSplitter[colIterator] + "\n * Request Data = "
													+ csvAllRows.get(rowIterator)[colIterator]);
											logger.info(" * Path-" + (colIterator + 1) + "= "
													+ jPathSplitter[colIterator] + "\n * Data = "
													+ csvAllRows.get(rowIterator)[colIterator]);

											if (check) {
												setJsonPathResult(props.getProperty("tempMandatoryJson"),
														props.getProperty("outputJsonFilePath") + "testCase_"
																+ keyValue[i][0] + "_" + (rowIterator + 1) + ".json",
														jPathSplitter[colIterator],
														csvAllRows.get(rowIterator)[colIterator]);
												check = false;
											}
												setJsonPathResult(
														props.getProperty("outputJsonFilePath") + "testCase_"
																+ keyValue[i][0] + "_" + (rowIterator + 1) + ".json",
														props.getProperty("outputJsonFilePath") + "testCase_"
																+ keyValue[i][0] + "_" + (rowIterator + 1) + ".json",
														jPathSplitter[colIterator],
														csvAllRows.get(rowIterator)[colIterator]);
											
											
											data = csvAllRows.get(rowIterator)[colIterator];
								

										}

										jsonStr = readFile(props.getProperty("outputJsonFilePath") + "testCase_"
												+ keyValue[i][0] + "_" + (rowIterator + 1) + ".json",
												Charset.defaultCharset());

										String testUrl = (props.getProperty("restUrl") + keyValue[i][2]);
										HashMap<Integer, String> response = null;

										if (keyValue[i][8].equalsIgnoreCase("Put")) {
											System.out.println(" * URL = " + testUrl);
											logger.info(" * URL = " + testUrl);
											response = HttpHandler.putToURL(testUrl, jsonStr, httpClient,keyValue[i][4]);

										} else if (keyValue[i][8].equalsIgnoreCase("Post")) {
											System.out.println(" * URL = " + testUrl);
											logger.info(" * URL = " + testUrl);
											response = HttpHandler.postToURL(testUrl, jsonStr, httpClient,keyValue[i][4]);
										}
										
										
										Iterator it = response.entrySet().iterator();
										size = response.size();

										while (it.hasNext()) {
											Map.Entry pair = (Map.Entry) it.next();
											resCode = pair.getKey().toString();
											jsonResponseStr = pair.getValue().toString();
										}
										ObjectNode json = new ObjectMapper().readValue(jsonResponseStr,
												ObjectNode.class);
										ObjectMapper mapper = new ObjectMapper();
										ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
										writer.writeValue(new File(props.getProperty("responseJsonFilePath")
												+ "testResponse_" + keyValue[i][0] + "_" + (rowIterator + 1) + ".json"),
												json);
										responseContent = readFile(
												props.getProperty("responseJsonFilePath") + "testResponse_"
														+ keyValue[i][0] + "_" + (rowIterator + 1) + ".json",
												Charset.defaultCharset());
//									}catch (Exception e) {
//										e.getStackTrace();
//										System.out.println(" * Found Exception = " + e.getMessage());
//										System.out.println(" **********************End Of Execution****************************** \n");
//									}
										logger.info("\n\n\t\tFind the Response Below\n" + responseContent + "\n\n");
										if ((resCode).equalsIgnoreCase(keyValue[i][6 + 3].toString())) {
//											checkResponse = getJsonPathResult(
//													props.getProperty("responseJsonFilePath") + "testResponse_"
//															+ keyValue[i][0] + "_" + (rowIterator + 1) + ".json",
//													keyValue[i][8 + 3]);

											resFlag = CheckJsonResults.checkJsonResults(keyValue, i,
													props.getProperty("outputJsonFilePath") + "testCase_"
															+ keyValue[i][0] + "_" + (rowIterator + 1) + ".json",
													props.getProperty("responseJsonFilePath") + "testResponse_"
															+ keyValue[i][0] + "_" + (rowIterator + 1) + ".json", rowIterator,logger);
											//System.out.println("Response Flag = "+ resFlag);
											
											if (resFlag) {
												eList = new ExcelSetter(i,10,resCode);
												excelSetterList.add(eList);
//												XpathTest.setCellValue(i, 7 + 3, resCode,
//														props.getProperty("jPathExcel"));
												Timestamp endTime = new Timestamp(System.currentTimeMillis());
											    long milliseconds = endTime.getTime() - startTime.getTime();
											    float seconds = (float) milliseconds / 1000;
											    
											    String executionTime = Float.toString(seconds);
											    
											    eList = new ExcelSetter(i,16,executionTime);
												excelSetterList.add(eList);												
//												XpathTest.setCellValue(i, 16, executionTime,
//														props.getProperty("jPathExcel"));
												
												eList = new ExcelSetter(i,13,"Pass");
												excelSetterList.add(eList);		
//												XpathTest.setCellValue(i, 10 + 3, "Pass",
//														props.getProperty("jPathExcel"));
												//System.out.println(" * Response Data = " + checkResponse);
												System.out.println(" * Test case " + keyValue[i][0] + "_"
														+ (rowIterator + 1)
														+ " Executed successfully with Http Response  " + resCode
														+ " And Successful Response outbound fetched " + resMsg
														+ "\n ****************************************************************** \n");
												logger.info(" * Test case " + keyValue[i][0] + "_" + (rowIterator + 1)
														+ " Executed successfully with Http Response  " + resCode
														+ " And Successful Response outbound fetched " + resMsg
														+ "\n ****************************************************************** \n");
											} else {
												
												eList = new ExcelSetter(i,10,resCode);
												excelSetterList.add(eList);	
//												XpathTest.setCellValue(i, 7 + 3, resCode,
//														props.getProperty("jPathExcel"));
												

//												XpathTest.setCellValue(i, 9 + 3, checkResponse,
//														props.getProperty("jPathExcel"));
												
												eList = new ExcelSetter(i,13,"Fail");
												excelSetterList.add(eList);	
//												XpathTest.setCellValue(i, 10 + 3, "Fail",
//														props.getProperty("jPathExcel"));
												
												System.out.println(" * Test case " + keyValue[i][0] + "_"
														+ (rowIterator + 1)
														+ " Executed successfully with Http Response  " + resCode
														+ " But failed to fetch outbound response " + resMsg
														+ "\n ****************************************************************** \n");
												logger.info(" * Test case " + keyValue[i][0] + "_" + (rowIterator + 1)
														+ " Executed successfully with Http Response  " + resCode
														+ " But failed to fetch outbound response " + resMsg
														+ "\n ****************************************************************** \n");
											}

										} else {
											
											eList = new ExcelSetter(i,10,resCode + "");
											excelSetterList.add(eList);	
//											XpathTest.setCellValue(i, 7 + 3, resCode + "",
//													props.getProperty("jPathExcel"));
											
											eList = new ExcelSetter(i, 10 + 3, "Fail");
											excelSetterList.add(eList);
											//XpathTest.setCellValue(i, 10 + 3, "Fail", props.getProperty("jPathExcel"));
											System.out.println(" * Test Case " + keyValue[i][0] + "_"
													+ (rowIterator + 1) + " failed with Response " + resCode + " "
													+ resMsg
													+ "\n ****************************************************************** \n");
											logger.info(" * Test Case_" + keyValue[i][0] + (rowIterator + 1)
													+ " failed with Response " + resCode + " " + resMsg
													+ "\n ****************************************************************** \n");
										}

										XpathTest.setCellValueAsHyperlink(i, 14,
												props.getProperty("responseJsonFilePath"),
												props.getProperty("jPathExcel"), "View Response");
										XpathTest.setCellValueAsHyperlink(i, 15, logPath,
												props.getProperty("jPathExcel"), "View Log File");

									}

								}
							}

							// GET & DELETE METHOD
						} else if (keyValue[i][8].equalsIgnoreCase("GET")
								|| keyValue[i][8].equalsIgnoreCase("DELETE")) {
							int subTestCaseCnt = 0;
							String headerParameter[];
							if (keyValue[i][3 + 3].equalsIgnoreCase("Yes")) {
								String logPath = (props.getProperty("logFileJson") + "TC_" + keyValue[i][0] +"_"+startTime.toLocaleString().replace(':', '-').replace(' ', '_')
								+ ".log");
								
								logPathForHtmlLink.put(i, logPath);
								Logger logger = WriteLogEntriesToLogFile.WriteLogEntriesToLogFile(logPath, i);
								if (flag) {
									System.out.println("\n  Connecting to server, please wait......... \n");
									logger.info("\n  Connecting to server, please wait......... \n");
									flag = false;
								}

								headerParameter = keyValue[i][2].split(",");
								subTestCaseCnt = 0;
								for (String headerValue : headerParameter) {
									String testUrl = (props.getProperty("restUrl") + headerValue.replace("\n", ""));
									subTestCaseCnt++;
									HashMap<Integer, String> response = null;
									if (keyValue[i][8].equalsIgnoreCase("GET")) {
										System.out.println(" * URL = " + testUrl);
										logger.info(" * URL = " + testUrl);
										response = sendReqObj.getToURL(testUrl, httpClient,keyValue[i][4]);

									} else if (keyValue[i][8].equalsIgnoreCase("DELETE")) {
										System.out.println(" * URL = " + testUrl);

										logger.info(" * URL = " + testUrl);
										response = sendReqObj.deleteToURL(testUrl, httpClient,keyValue[i][4]);
									}
									Iterator it = response.entrySet().iterator();
									size = response.size();

									while (it.hasNext()) {
										Map.Entry pair = (Map.Entry) it.next();
										resCode = pair.getKey().toString();
										jsonResponseStr = pair.getValue().toString();
								

									}
									
									if(jsonResponseStr.substring(0).contains("[")) {
										ObjectMapper mapper = new ObjectMapper();
										ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
										writer.writeValue(new File(props.getProperty("responseJsonFilePath")
												+ "testResponse_" + keyValue[i][0] + "_" + subTestCaseCnt + ".json"), jsonResponseStr);
										
									}else {
										JsonNode json = new ObjectMapper().readValue(jsonResponseStr, ObjectNode.class);
										ObjectMapper mapper = new ObjectMapper();
										ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
										writer.writeValue(new File(props.getProperty("responseJsonFilePath")
												+ "testResponse_" + keyValue[i][0] + "_" + subTestCaseCnt + ".json"), json);
									}
					
									responseContent = readFile(props.getProperty("responseJsonFilePath")
											+ "testResponse_" + keyValue[i][0] + "_" + subTestCaseCnt + ".json",
											Charset.defaultCharset());

									logger.info("\n\n\t\tFind the Response Below\n" + responseContent + "\n\n");

									if ((resCode).equalsIgnoreCase(keyValue[i][6 + 3].toString())) {
										
										Timestamp endTime = new Timestamp(System.currentTimeMillis());
									    long milliseconds = endTime.getTime() - startTime.getTime();
									    float seconds = (float) milliseconds / 1000;
									    
									    String executionTime = Float.toString(seconds);
										
									    eList = new ExcelSetter(i, 16, executionTime);
										excelSetterList.add(eList);
//										XpathTest.setCellValue(i, 16, executionTime,
//												props.getProperty("jPathExcel"));

										eList = new ExcelSetter(i, 7 + 3, resCode);
										excelSetterList.add(eList);
										//XpathTest.setCellValue(i, 7 + 3, resCode, props.getProperty("jPathExcel"));
										
										eList = new ExcelSetter(i, 10 + 3, "Pass");
										excelSetterList.add(eList);
										//XpathTest.setCellValue(i, 10 + 3, "Pass", props.getProperty("jPathExcel"));
										System.out.println(" * Test case " + keyValue[i][0] + "_" + subTestCaseCnt
												+ " Executed successfully with Http Response  " + resCode
												+ "\n ****************************************************************** \n");
										logger.info(" * Test case " + keyValue[i][0] + "_" + subTestCaseCnt
												+ " Executed successfully with Http Response  " + resCode

												+ "\n ****************************************************************** \n");

									} else {
										eList = new ExcelSetter(i, 7 + 3, resCode + "");
										excelSetterList.add(eList);
										XpathTest.setCellValue(i, 7 + 3, resCode + "", props.getProperty("jPathExcel"));
										
										eList = new ExcelSetter(i, 10 + 3, "Fail");
										excelSetterList.add(eList);
										//XpathTest.setCellValue(i, 10 + 3, "Fail", props.getProperty("jPathExcel"));
										System.out.println(" * Test Case " + keyValue[i][0] + "_" + subTestCaseCnt
												+ " failed with Response " + resCode + " " + resMsg
												+ "\n ****************************************************************** \n");
										logger.info(" * Test Case_" + keyValue[i][0] +"_"+ subTestCaseCnt
												+ " failed with Response " + resCode + " " + resMsg
												+ "\n ****************************************************************** \n");
									}
								}

								XpathTest.setCellValueAsHyperlink(i, 14, props.getProperty("responseJsonFilePath"),
										props.getProperty("jPathExcel"), "View Response");
								XpathTest.setCellValueAsHyperlink(i, 15, logPath, props.getProperty("jPathExcel"),
										"View Log File");
							}

						}
					} else {
						System.out.println(" **********************End Of Execution****************************** \n");
						break;
					}
				}
				for (int i=0; i < excelSetterList.size(); i++)
				{
					XpathTest.setCellValue(excelSetterList.get(i).row,excelSetterList.get(i).column,excelSetterList.get(i).cellValue,
							props.getProperty("jPathExcel"));
//				    System.out.println(excelSetterList.get(i).row);
//				    System.out.println(excelSetterList.get(i).column);
//				    System.out.println(excelSetterList.get(i).cellValue);
				}
				String[][] keyValueLatest = xPathTestObj.getPath(props.getProperty("jPathExcel"));
				Excel2Html e2html = new Excel2Html();
				e2html.saveHtml(keyValueLatest, logPathForHtmlLink);
				
//			}catch (Exception e) {
//				e.getStackTrace();
//				System.out.println(" * Found Exception = " + e.getMessage());
//				System.out.println(" **********************End Of Execution****************************** \n");
//			}
			break;
		}

		default: {
			System.out.println("Please enter the correct choice");
			break;
		}

		}

		// ------------------------------------------------------------------

	}
	/**
	 * This mehtod maps all the json path provided in the excel with all the test data provided in the csv.
	 * @author Indroneel Sengupta
	 * @param i This the row number of the excel sheet
	 * @param j This is the column number of the excel sheet
	 * @param keyValue This is an 2*2 array storing all the excel sheet data. 
	 */
	public void mapJpathWithTestData(int i/* row */, int j/* column */, String[][] keyValue) {
		String jPath[];
		jPath = keyValue[i][j].split(",");

		for (String jpath : jPath) {

		}
	}

	public void setJsonPathResult(String oriJsonFilePath, String outputJsonFilePath, String jsonPath, Object data)
			throws Exception {
		
		try {
			File f1 = new File(oriJsonFilePath);
			ObjectNode jsonContext = JsonPath.using(configuration).parse(f1).set(jsonPath, data).json();

//		JsonNode jsonNode = JsonPath.using(configuration).parse(f1).set(jsonPath, data).json();
//		ObjectNode jsonContext = (ObjectNode) jsonNode.deepCopy();

			ObjectMapper mapper = new ObjectMapper();
			ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
			writer.writeValue(new File(outputJsonFilePath), jsonContext);
		} catch (com.jayway.jsonpath.PathNotFoundException pathNotFound) {
			pathNotFound.getStackTrace();
			System.out.println(" * Found Exception = " + " com.jayway.jsonpath.PathNotFoundException :: "
					+ pathNotFound.getMessage());
			System.out.println(" **********************End Of Execution****************************** \n");
		}

	}

	public static String getJsonPathResult(String oriJsonFilePath, String jsonPath) throws Exception {
		String jsonContext = "";
		try {
			File f1 = new File(oriJsonFilePath);
			jsonContext = JsonPath.using(configuration).parse(f1).read(jsonPath).toString();
			
		} catch (com.jayway.jsonpath.PathNotFoundException pathNotFound) {
			pathNotFound.getStackTrace();
			System.out.println(" * Found Exception = " + " com.jayway.jsonpath.PathNotFoundException :: "
					+ pathNotFound.getMessage());
			System.out.println(" **********************End Of Execution****************************** \n");
		}
		return jsonContext;

	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
