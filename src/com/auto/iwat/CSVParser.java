package com.auto.iwat;
import java.io.File;
import java.util.List;

import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.CsvParser;
import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.CsvParserSettings;

public class CSVParser {
	/**
	 * 
	 * @param path Path of the csv file
	 * @return returns the list of the values in the csv file
	 */
	public static List<String[]> csvParser(String path){
		CsvParserSettings settings = new CsvParserSettings(); // you'll find many options here, check the tutorial.
		CsvParser parser = new CsvParser(settings);
		String[][] csvValue;		
		List<String[]> allRows = parser.parseAll(new File(path));		
		return allRows;		
		
	}
}
