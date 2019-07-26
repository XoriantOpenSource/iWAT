package com.auto.iwat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.io.IOException;

public class RegexImplementation {
	public static List searchString(String fileName, String regex) throws IOException {
		Scanner fileScanner = new Scanner(new File(fileName));
		int lineID = 0;
		List lineNumbers = new ArrayList();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = null;
		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			lineID++;
			matcher = pattern.matcher(line);
			if (matcher.find()) {
				lineNumbers.add(lineID);

			}

		}
		return lineNumbers;
	}


}
