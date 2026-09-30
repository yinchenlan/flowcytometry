package com.lansoft.flowcytometry.ui.nextgen;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader implements Serializable{
	
	private static XSSFWorkbook workbook;
	private static TreeMap < String, ArrayList <Antibody>> antibodies;
	
	public ExcelReader() {
		
	}
	
	public static TreeMap < String, ArrayList <Antibody>> readfile ( String infile ) {
		try {
			//Open FileInputStrem, and create new workbook. Create evaluator 
			//and corresponding sheet
			FileInputStream file = new FileInputStream( new File(infile) );
			workbook = new XSSFWorkbook( file );
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			//Iterate through the rows of the sheet. Create Antibody for database,
			//not including the first row (header).
			Iterator<Row> rowIterator = sheet.iterator();
			antibodies = new TreeMap < String, ArrayList <Antibody>> ();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (row.getRowNum() > 1 ) {
					String conjugate = row.getCell(0).getStringCellValue();
					String target = row.getCell(1).getStringCellValue();
					String species = row.getCell(2).getStringCellValue();
					String company = row.getCell(3).getStringCellValue();
					String catalog = row.getCell(4).getStringCellValue();
					String lot = row.getCell(5).getStringCellValue();
					String clone = row.getCell(6).getStringCellValue();
					String iso = row.getCell(7).getStringCellValue();
					Integer aups = (int) row.getCell(8).getNumericCellValue();
					Antibody antibody = new Antibody (conjugate, target, species, company, catalog, lot, clone,iso, aups);
					
					if ( antibodies.containsKey(target)) {
						antibodies.get(target).add( antibody);
					} else {
						ArrayList<Antibody> antibodyArray = new ArrayList <Antibody> ();
						antibodyArray.add( antibody );
						antibodies.put(target, antibodyArray);
					}
				}
				
			}
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return antibodies;
	}

}
