package com.auto.iwat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.hssf.usermodel.HSSFWorkbook; 
import org.apache.poi.openxml4j.exceptions.InvalidFormatException; 
import org.apache.poi.openxml4j.opc.OPCPackage; 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row; 
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook; 
import org.apache.poi.ss.usermodel.WorkbookFactory; 
import org.apache.poi.xssf.usermodel.XSSFCell; 
import org.apache.poi.xssf.usermodel.XSSFRow; 
import org.apache.poi.xssf.usermodel.XSSFSheet; 
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 

public class XpathTest implements CellStyle{
	public String[][] getPath(String xPathXml) throws Exception {
		DataFormatter formatter = new DataFormatter();
		XSSFWorkbook workbook1 = new XSSFWorkbook(new FileInputStream(new File(xPathXml)));
		XSSFSheet ConfigurationSheet = workbook1.getSheetAt(0);
		workbook1.close();

		int rowcount1 = ConfigurationSheet.getLastRowNum() + 1;
		//System.out.println("row:" + rowcount1);
		int colcount1 = ConfigurationSheet.getRow(0).getLastCellNum();
		//System.out.println("column:" + colcount1);
		String nullCheck = "";

		String[][] keyValue = new String[rowcount1][colcount1];

		int i = 0, j = 0;
		int cnt = 0;		  
		  
		 //--------------------***using Iterator class***------------------
		  Iterator rowItr = ConfigurationSheet.rowIterator();

		    while ( rowItr.hasNext() ) {
		    	j=0;
		        XSSFRow row = (XSSFRow) rowItr.next();
		        Iterator cellItr = row.cellIterator();

		        while ( cellItr.hasNext() ) {		        	
		            XSSFCell cell = (XSSFCell) cellItr.next();
		            keyValue[i][j] = formatter.formatCellValue(cell).toString();
		            j++;		        
		        }
		        i++;
		    }		 
		  return keyValue;
	}
	
	public static void setCellValue(int row, int col , String cellValue, String filePath) throws Exception {
		FileInputStream file = new FileInputStream(filePath);

        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Cell cell = null;
        
       
        //Retrieve the row and check for null
          XSSFRow sheetrow = sheet.getRow(row);
          if(sheetrow == null){
              sheetrow = sheet.createRow(row);
          }
          //Update the value of cell
          cell = sheetrow.getCell(col);
          if(cell == null){
              cell = sheetrow.createCell(col);
          }
          cell.setCellValue(cellValue);

          file.close();
          FileOutputStream outFile =new FileOutputStream(new File(filePath));
        workbook.write(outFile);
        outFile.close();
	}
	
	public static void setCellValueAsHyperlink(int row, int col , String hyperLinkValue, String filePath, String cellMessage) throws Exception {
		FileInputStream file = new FileInputStream(filePath);
		File responseFile = new File(hyperLinkValue);
		
        @SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        CreationHelper createHelper = workbook.getCreationHelper();
        
        CellStyle hlink_style = workbook.createCellStyle();
		Font hlink_font = workbook.createFont();
		hlink_font.setUnderline(Font.U_SINGLE);
		hlink_font.setColor(Font.COLOR_RED);
		hlink_style.setFont(hlink_font);
		hlink_style.setAlignment(ALIGN_CENTER);
		hlink_style.setVerticalAlignment(VERTICAL_CENTER);
		hlink_style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		hlink_style.setFillPattern(SOLID_FOREGROUND);
		hlink_style.setBorderBottom(BORDER_THIN);
		hlink_style.setBorderRight(BORDER_THIN);
		
		org.apache.poi.ss.usermodel.Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_FILE);
		Cell cell = null;
		
		
		XSSFRow sheetrow = sheet.getRow(row);
        if(sheetrow == null){
            sheetrow = sheet.createRow(row);
        }
        //Update the value of cell
        cell = sheetrow.getCell(col);
        if(cell == null){
            cell = sheetrow.createCell(col);
        }

		
		//cell=row.createCell((short) 1);
		cell.setCellValue(cellMessage);
		//path_f="D://Result.xls";
		link.setAddress(responseFile.toURI().toString());  
		cell.setHyperlink((org.apache.poi.ss.usermodel.Hyperlink) link);
		//cell.setHyperlink(link);    
		cell.setCellStyle(hlink_style);
		
        file.close();
        FileOutputStream outFile =new FileOutputStream(new File(filePath));
        workbook.write(outFile);
        outFile.close();
        

	}

	@Override
	public short getIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDataFormat(short fmt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getDataFormat() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDataFormatString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFont(Font font) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getFontIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setHidden(boolean hidden) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getHidden() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setLocked(boolean locked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAlignment(short align) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getAlignment() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setWrapText(boolean wrapped) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getWrapText() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setVerticalAlignment(short align) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getVerticalAlignment() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setRotation(short rotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getRotation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setIndention(short indent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getIndention() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBorderLeft(short border) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getBorderLeft() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBorderRight(short border) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getBorderRight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBorderTop(short border) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getBorderTop() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBorderBottom(short border) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getBorderBottom() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLeftBorderColor(short color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getLeftBorderColor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setRightBorderColor(short color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getRightBorderColor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTopBorderColor(short color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getTopBorderColor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBottomBorderColor(short color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getBottomBorderColor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setFillPattern(short fp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getFillPattern() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setFillBackgroundColor(short bg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getFillBackgroundColor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Color getFillBackgroundColorColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFillForegroundColor(short bg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getFillForegroundColor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Color getFillForegroundColorColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cloneStyleFrom(CellStyle source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setShrinkToFit(boolean shrinkToFit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getShrinkToFit() {
		// TODO Auto-generated method stub
		return false;
	}
}
