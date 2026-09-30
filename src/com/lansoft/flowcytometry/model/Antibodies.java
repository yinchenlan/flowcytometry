package com.lansoft.flowcytometry.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import jxl.Cell;
import jxl.CellType;
import jxl.CellView;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@XmlRootElement(name = "antibodies")
@XmlType(propOrder = { "name", "description", "antibodiesList" })
public class Antibodies {
	private List<Antibody> antibodiesList;
	
	private String name;
	
	private String description;

	public Antibodies() {
		antibodiesList = new ArrayList<Antibody>();
	}

	@XmlElementWrapper( name="antibodiesList" )
	@XmlElement(name = "antibody")
	public List<Antibody> getAntibodiesList() {
		return antibodiesList;
	}

	public void setAntiBodiesList(List<Antibody> antibodies) {
		this.antibodiesList = antibodies;
	}
	
	
	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@XmlAttribute(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static Antibodies getAntibodies() throws JAXBException,
			FileNotFoundException, InstantiationException, IllegalAccessException {
		return (Antibodies) ModelDAO.loadObject(Antibodies.class);
	}

	public static void saveAntibodies(Antibodies antibodies)
			throws JAXBException, FileNotFoundException, IOException {
		ModelDAO.saveObject(antibodies);
	}

	public static Antibodies importCsvFile(File f) {
		Antibodies antibodies = new Antibodies();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(f));
			String line = null;
			// skip header
			in.readLine();
			while ((line = in.readLine()) != null) {
				Antibody ab = new Antibody();
				String[] items = line.split(",");
				ab.setConjugateName(items[0]);
				ab.setTargetName(items[1]);
				ab.setTargetSpecies(items[2]);
				ab.setCompany(items[3]);
				ab.setCatalogNum(items[4]);
				ab.setLotNum(items[5]);
				ab.setClone(items[6]);
				if (ab.getClone() == null || 
						ab.getClone().trim().isEmpty()) {
					ab.setClone(ab.getTargetName());
				}
				ab.setIsotype(items[7]);
				ab.setAups(items[8]);
				antibodies.getAntibodiesList().add(ab);
			}
			return antibodies;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e2) {
				throw new RuntimeException(e2);
			}
		}
	}

	public static Antibodies importExcelFile(File f) {
		Antibodies antibodies = new Antibodies();
		Workbook w;
		boolean foundFirstCell = false;
		boolean foundLastCellOnFirstLine = false;
		int row = -1;
		int startCol = -1;
		int endCol = -1;
		try {
			w = Workbook.getWorkbook(f);
			Sheet sheet = w.getSheet(0);
			for (int j = 0; j < sheet.getRows(); j++) {
				for (int i = 0; i < sheet.getColumns(); i++) {
					Cell cell = sheet.getCell(i, j);
					if (!foundFirstCell && cell.getType() != CellType.EMPTY) {
						foundFirstCell = true;
						row = j;
						startCol = i;
						continue;
					}
					if (!foundLastCellOnFirstLine && foundFirstCell
							&& cell.getType() == CellType.EMPTY) {
						foundLastCellOnFirstLine = true;
						endCol = i - 1;
						break;
					}
				}				
			}

			for (int r = row + 1; r < sheet.getRows(); r++) {
				Cell cell = sheet.getCell(startCol, r);
				Antibody ab = new Antibody();
				ab.setConjugateName(cell.getContents().trim());
				cell = sheet.getCell(startCol + 1, r);
				ab.setTargetName(cell.getContents().trim());
				cell = sheet.getCell(startCol + 2, r);
				ab.setTargetSpecies(cell.getContents().trim());
				cell = sheet.getCell(startCol + 3, r);
				ab.setCompany(cell.getContents().trim());
				cell = sheet.getCell(startCol + 4, r);
				ab.setCatalogNum(cell.getContents().trim());
				cell = sheet.getCell(startCol + 5, r);
				ab.setLotNum(cell.getContents().trim());
				cell = sheet.getCell(startCol + 6, r);
				ab.setClone(cell.getContents().trim());
				cell = sheet.getCell(startCol + 7, r);
				ab.setIsotype(cell.getContents().trim());
				cell = sheet.getCell(startCol + 8, r);
				ab.setAups(cell.getContents().trim());
				antibodies.getAntibodiesList().add(ab);
			}
		} catch (BiffException | IOException e) {
			throw new RuntimeException(e);
		}
		return antibodies;
	}
	
	public static void exportToExcel(
			File excelFile, 
			List<List<List<Antibody>>> results) 
			throws IOException, WriteException {
	    WorkbookSettings wbSettings = new WorkbookSettings();

	    wbSettings.setLocale(new Locale("en", "EN"));
	    WritableWorkbook workbook = Workbook.createWorkbook(excelFile, wbSettings);
	    for (int resultSetIdx = 0; resultSetIdx < results.size(); resultSetIdx++) {
	    	workbook.createSheet("Result Set " + (resultSetIdx + 1), resultSetIdx);
	        WritableSheet excelSheet = workbook.getSheet(resultSetIdx);
	        List<List<Antibody>> solnSet = results.get(resultSetIdx);
	        int rowIndex = 0;
	        for (int solnSetIdx = 0; solnSetIdx < solnSet.size(); solnSetIdx++) {
	        	addHeaders(excelSheet, rowIndex);
	        	rowIndex = rowIndex + 2;
	        	List<Antibody> solnPart = solnSet.get(solnSetIdx);
	        	for (int solnPartIdx = 0; solnPartIdx < solnPart.size(); solnPartIdx++) {
	        		addResult(excelSheet, solnPart.get(solnPartIdx), rowIndex++);
	        	}
	        	rowIndex = rowIndex + 2;
	        }
	    }

	    workbook.write();
	    workbook.close();
	}

	private static void addResult(WritableSheet excelSheet, Antibody antibody,
			int rowIndex) throws RowsExceededException, WriteException {
		Label label;
		int colIdx = 0;
	    label = new Label(colIdx++, rowIndex, antibody.getConjugateName());
	    excelSheet.addCell(label);	
	    label = new Label(colIdx++, rowIndex, antibody.getTargetName());
	    excelSheet.addCell(label);
	    label = new Label(colIdx++, rowIndex, antibody.getTargetSpecies());
	    excelSheet.addCell(label);
	    label = new Label(colIdx++, rowIndex, antibody.getCompany());
	    excelSheet.addCell(label);
	    label = new Label(colIdx++, rowIndex, antibody.getCatalogNum());
	    excelSheet.addCell(label);
	    label = new Label(colIdx++, rowIndex, antibody.getLotNum());
	    excelSheet.addCell(label);
	    label = new Label(colIdx++, rowIndex, antibody.getClone());
	    excelSheet.addCell(label);
	    label = new Label(colIdx++, rowIndex, antibody.getIsotype());
	    excelSheet.addCell(label);
	    label = new Label(colIdx++, rowIndex, antibody.getAups());
	    excelSheet.addCell(label);
	}

	private static void addHeaders(WritableSheet excelSheet, int rowIndex) throws WriteException {
	    // Lets create a times font
	    WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
	    // Define the cell format
	    WritableCellFormat times = new WritableCellFormat(times10pt);
	    // Lets automatically wrap the cells
	    times.setWrap(true);

	    // create create a bold font with underlines
	    WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,
	        UnderlineStyle.SINGLE);
	    WritableCellFormat timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
	    // Lets automatically wrap the cells
	    timesBoldUnderline.setWrap(true);

	    CellView cv = new CellView();
	    cv.setFormat(times);
	    cv.setFormat(timesBoldUnderline);
	    cv.setAutosize(true);

	    // Write a few headers
	    // "conjugateName", "targetName", "targetSpecies", "company", "catalogNum", "lotNum", "clone", "isotype", "aups"
	    int colIdx = 0;
	    addCaption(excelSheet, colIdx++, rowIndex, "Conjugate", timesBoldUnderline);
	    addCaption(excelSheet, colIdx++, rowIndex, "Target", timesBoldUnderline);
	    addCaption(excelSheet, colIdx++, rowIndex, "Species", timesBoldUnderline);
	    addCaption(excelSheet, colIdx++, rowIndex, "Company", timesBoldUnderline);
	    addCaption(excelSheet, colIdx++, rowIndex, "Catalog #", timesBoldUnderline);
	    addCaption(excelSheet, colIdx++, rowIndex, "Lot #", timesBoldUnderline);
	    addCaption(excelSheet, colIdx++, rowIndex, "Clone", timesBoldUnderline);
	    addCaption(excelSheet, colIdx++, rowIndex, "Isotype", timesBoldUnderline);
	    addCaption(excelSheet, colIdx++, rowIndex, "AUPS", timesBoldUnderline);
	}
	
	private static void addCaption(WritableSheet sheet, int column, int row, String s, WritableCellFormat cellFormat)
		      throws RowsExceededException, WriteException {
		    Label label;
		    label = new Label(column, row, s, cellFormat);
		    sheet.addCell(label);
		  }
}
