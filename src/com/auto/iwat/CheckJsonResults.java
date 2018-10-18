package com.auto.iwat;
import java.util.List;
import java.util.Properties;

public class CheckJsonResults {
	public static boolean checkJsonResults(String[][] keyValue, int excelRowNo, String reqFilePath,
			String resFilePath , int looSize) throws Exception {
		boolean flag = true;
		Properties props = PropertyFile.propertyFile();
		String[] reqPathSplitter = keyValue[excelRowNo][5].split(",");
		String[] resPathSplitter = keyValue[excelRowNo][11].split(",");
		String reqData = "";
		String resData = "";
		String printResponse = "";
		
		if (reqPathSplitter.length == resPathSplitter.length) {
			List<String[]> csvAllRows = CSVParser.csvParser(keyValue[excelRowNo][7]);
			for (int rowIterator = 0; rowIterator <= looSize; rowIterator++) {
				for (int i = 0; i < reqPathSplitter.length; i++) {
					
					reqData = JsonParser.getJsonPathResult(props.getProperty("outputJsonFilePath") + "testCase_"
							+ keyValue[excelRowNo][0] + "_" + (rowIterator + 1) + ".json", reqPathSplitter[i]);
					resData = JsonParser.getJsonPathResult(props.getProperty("responseJsonFilePath") + "testResponse_"
							+ keyValue[excelRowNo][0] + "_" + (rowIterator + 1) + ".json", resPathSplitter[i]);
					
					

					if (!reqData.equalsIgnoreCase(resData) && !reqData.equalsIgnoreCase("\"" + resData + "\"")) {
						flag = false;
						break;
					}else {
						printResponse = printResponse + resData + " ";
					}								

				}
				printResponse = printResponse + "\n";		
				
			}
			XpathTest.setCellValue(excelRowNo, 9 + 3, printResponse,
					props.getProperty("jPathExcel"));
			
		} else {
			System.out.println(
					" *** Your Request Path and Response Path's count does not match, please ensure the counts of the two should be same ");
		}

		return flag;
	}
}
