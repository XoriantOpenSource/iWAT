package com.auto.iwat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.ss.examples.html.ToHtml;
import org.dom4j.Document;
import org.w3c.dom.Node;

public class Excel2Html {
	public static String toHTML(String[][] data,HashMap<Integer,String> logPath) throws Exception {
		Properties props = PropertyFile.propertyFile();
		StringBuilder sb = new StringBuilder();
		int cntPass=0,cntFail=0,cntTot=0;
		sb.append("<table style =\"border: 3px solid black;\">\n");
		sb.append("          <caption style =\"text-align:left;\">\r\n" + 
				"               <h2>Detailed Report</h2>\r\n" + 
				"            </caption>");
		for (int row = 0; row < data.length; row++) {
			if (row == 0) {
				sb.append("\t<tr style=\"background-color:#FFBC21;\">\n");
			} else if (data[row][13].equalsIgnoreCase("Pass")) {
				sb.append("\t<tr style=\"background-color:lightgreen;\">\n");
				cntPass++;
			} else if (data[row][13].equalsIgnoreCase("Fail")) {
				sb.append("\t<tr style=\"background-color:FF4738;\">\n");
				cntFail++;
			} else
				sb.append("\t<tr>\n");
			for (int col = 0; col < data[0].length; col++) {
				if (row == 0) {
					sb.append("\t\t<th style =\"border: 3.5px solid black;\">" + data[row][col] + "</th>\n");
				} else {
					if(col == 3) {
						sb.append("\t\t<td style =\"border: 1.5px solid black;\">\r\n" + 
								"			<a href = "+data[row][col]+">"+ data[row][col] +"</a>\r\n" + 
								"		</td>\n");
					}else if(col == 7) {
						sb.append("\t\t<td style =\"border: 1.5px solid black;\">\r\n" + 
								"			<a href = "+data[row][col]+">"+ data[row][col] +"</a>\r\n" + 
								"		</td>\n");
					}else if(col == 14) {
						sb.append("\t\t<td style =\"border: 1.5px solid black;\">\r\n" + 
								"			<a href = "+props.getProperty("responseJsonFilePath")+">"+ data[row][col] +"</a>\r\n" + 
								"		</td>\n");
					}else if(col == 15) {
						sb.append("\t\t<td style =\"border: 1.5px solid black;\">\r\n" + 
								"			<a href = "+logPath.get(row)+">"+ data[row][col] +"</a>\r\n" + 
								"		</td>\n");
					}else sb.append("\t\t<td style =\"border: 1.5px solid black;\">" + data[row][col] + "</td>\n");
				}
			}
			sb.append("\t</tr>\n");
		}
		sb.append("</table>");
		cntTot = cntPass + cntFail;
		
		StringBuilder piechart = new StringBuilder();
		piechart.append("<html lang=\"en-US\">\r\n" + 
				"   <body>\r\n" + 
				"      <style>\r\n" + 
				"         body{\r\n" + 
				"         background-color: #f1f1c1;\r\n" + 
				"         }\r\n" + 
				"         table, th, td {\r\n" + 
				"         border: 2px solid black;\r\n" + 
				"         border-collapse: collapse;\r\n" + 
				"         }\r\n" + 
				"         div#piechart{\r\n" + 
				"         background-color: #f1f1c1;\r\n" + 
				"         }\r\n" + 
				"         th, td {\r\n" + 
				"         padding: 15px;\r\n" + 
				"         text-align: left;\r\n" + 
				"         }\r\n" + 
				"         table#t01 {\r\n" + 
				"         width: 45%;    \r\n" + 
				"         background-color: lightgrey;\r\n" + 
				"         }\r\n" + 
				"      </style>\r\n" + 
				"      <h1 align = center>iWAT</h1>\r\n" + 
				"      <h2 align = center>Report Generation</h2>\r\n" + 
				"      <div id=\"piechart\" style =\"width: 50%;margin-left: auto\"></div>\r\n" + 
				"      <div id = \"table\">\r\n" + 
				"         <table id = \"t01\" align = left style =\"margin-top: -295; border-spacing: 20px;border-collapse: collapse;\"  >\r\n" + 
				"            <caption>\r\n" + 
				"               <h4>Tabular report analysis</h4>\r\n" + 
				"            </caption>\r\n" +
				"			 <tr>\r\n" + 
				"               <th > Domain URL </th>\r\n" + 
				"               <td > <a href =" +props.getProperty("restUrl") +">"+ props.getProperty("restUrl") +"</a></td>\r\n" + 
				"            </tr>\r\n" + 
				"			 <tr>\r\n" + 
				"               <th > Test Suite Excel </th>\r\n" + 
				"               <td ><a href =" +props.getProperty("jPathExcel") +">"+ props.getProperty("jPathExcel") +"</a></td>\r\n" + 
				"            </tr>\r\n" +
				"            <tr>\r\n" + 
				"               <th > Total no. of Test Cases executed </th>\r\n" + 
				"               <td >"+cntTot+"</td>\r\n" + 
				"            </tr>\r\n" + 
				"            <tr>\r\n" + 
				"               <th > Total no. of Test Cases passed </th>\r\n" + 
				"               <td >"+cntPass+"</td>\r\n" + 
				"            </tr>\r\n" + 
				"            <tr>\r\n" + 
				"               <th > Total no. of Test Cases failed </th>\r\n" + 
				"               <td >"+cntFail+"</td>\r\n" + 
				"            </tr>\r\n" + 
				"         </table>\r\n" + 
				"      </div>\r\n" + 
				"      <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\r\n" + 
				"      <script type=\"text/javascript\">\r\n" + 
				"         // Load google charts\r\n" + 
				"         google.charts.load('current', {'packages':['corechart']});\r\n" + 
				"         google.charts.setOnLoadCallback(drawChart);\r\n" + 
				"         \r\n" + 
				"         // Draw the chart and set the chart values\r\n" + 
				"         function drawChart() {\r\n" + 
				"           var data = google.visualization.arrayToDataTable([\r\n" + 
				"           ['Task', 'Hours'],\r\n" + 
				"           ['Pass', "+cntPass+"],\r\n" + 
				"           ['Fail',"+cntFail+"],\r\n" + 
				"         \r\n" + 
				"         ]);\r\n" + 
				"         \r\n" + 
				"           // Optional; add a title and set the width and height of the chart\r\n" + 
				"           var options = {'title':'Test Case Report Anylysis','backgroundColor': '#f1f1c1','width':700, 'height':300};\r\n" + 
				"         \r\n" + 
				"           // Display the chart inside the <div> element with id=\"piechart\"\r\n" + 
				"           var chart = new google.visualization.PieChart(document.getElementById('piechart'));\r\n" + 
				"           chart.draw(data, options);\r\n" + 
				"         }\r\n" + 
				"      </script>\r\n" + sb.toString()+
				"   </body>\r\n" + 
				"</html>");
		return piechart.toString();
	}
	
	
	public static void saveHtml(String[][] keyValue,HashMap<Integer,String> logPath) throws Exception {
		String fileContent = toHTML(keyValue,logPath);
		try (PrintWriter out = new PrintWriter("ReportGeneration.html")) {
		    out.println(fileContent);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
		

}
