package com.auto.iwat;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class CheckJsonResults {
	public static boolean checkJsonResults(String[][] keyValue, int excelRowNo, String reqFilePath, String resFilePath,
			int looSize, Logger logger) throws Exception {
		boolean flag = true;
		Properties props = PropertyFile.propertyFile();
		String[] reqPathSplitter = keyValue[excelRowNo][12].split(",");
		String[] resPathSplitter = keyValue[excelRowNo][11].split(",");
		String reqData = "";
		String resData = "";
		String printResponse = "";

		if (reqPathSplitter.length == resPathSplitter.length) {
			// if (keyValue[excelRowNo][5] == keyValue[excelRowNo][11]) {
			List<String[]> csvAllRows = CSVParser.csvParser(keyValue[excelRowNo][7]);
			for (int rowIterator = 0; rowIterator <= looSize; rowIterator++) {
				for (int i = 0; i < resPathSplitter.length; i++) {

//					reqData = JsonParser.getJsonPathResult(props.getProperty("outputJsonFilePath") + "testCase_"
//							+ keyValue[excelRowNo][0] + "_" + (rowIterator + 1) + ".json", reqPathSplitter[i]);

					reqData = reqPathSplitter[i];
					logger.info(" * Expected Data = " + reqData);
					resData = JsonParser.getJsonPathResult(props.getProperty("responseJsonFilePath") + "testResponse_"
							+ keyValue[excelRowNo][0] + "_" + (rowIterator + 1) + ".json", resPathSplitter[i]);
					logger.info(" * Response Data = " + resData);
					if (!reqData.trim().equalsIgnoreCase(resData.trim())
							&& !resData.equalsIgnoreCase("\"" + reqData + "\"")) {
						flag = false;
						printResponse = keyValue[excelRowNo][11];
						// printResponse = printResponse + resData + " ";
						//break;
					} else {
						printResponse = printResponse + resData + " ";
					}

				}
				printResponse = printResponse + "\n";

//				String regex = keyValue[excelRowNo][17];
//				if (regex != null && !regex.equalsIgnoreCase("")) {
//					List regexList = RegexImplementation.searchString((props.getProperty("responseJsonFilePath")
//							+ "testResponse_" + keyValue[excelRowNo][0] + "_" + (rowIterator + 1) + ".json"), regex);
//					System.out.println("For RegEx " + regex + "Below are the line numbers");
//					regexList.stream().forEach(System.out::println);
//				}

			}
			//XpathTest.setCellValue(excelRowNo, 9 + 3, printResponse, props.getProperty("jPathExcel"));

		} else {
			System.out.println(
					" *** Your Request Path and Response Path's count does not match, please ensure the counts of the two are same ");
			flag = false;
		}

		return flag;
	}
}
