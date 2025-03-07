package xmlprojeto.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import xmlprojeto.models.Record;

@SuppressWarnings("deprecation")
@ManagedBean(name = "MBconsultaPlanilha")
@ViewScoped

public class consultateste {

	public static void main(String[] args) {

		try (FileInputStream fis = new FileInputStream(
				new File("C:\\projetoxml\\Extração SIAFI-Web Dedução DDF025.xlsx"));
				Workbook workbook = new XSSFWorkbook(fis)) {

			Sheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				if (row.getRowNum() == 0) {
					continue;
				}
				String situacao = getStringValue(row.getCell(5)).trim();
				if (situacao.equals("DDF025")) {
					Record record = new Record(
							getStringValue(row.getCell(6)), 
							getStringValue(row.getCell(8)),
							getDateValue(row.getCell(7)),
							getNumericValue(row.getCell(9)),
							getNumericValue(row.getCell(9)),
							getNumericValue(row.getCell(10)),
							getStringValue(row.getCell(14)));

					List<Record> records = new ArrayList<>();
					records.add(record);
					System.out.println(record.getcnpj10());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return records;
	}
	private static String getStringValue(Cell cell) {
		if (cell == null) {
			return "";
		} else if (cell.getCellType() == CellType.NUMERIC) {
			return String.valueOf((long) cell.getNumericCellValue());
		} else {
			return cell.getStringCellValue();
		}
	}

	private static double getNumericValue(Cell cell) {
		if (cell == null || cell.getCellType() != CellType.NUMERIC) {
			return 0;
		}
		return cell.getNumericCellValue();
	}

	private static String getDateValue(Cell cell) {
		if (cell == null) {
			return "";
		} else if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
			Date date = cell.getDateCellValue();
			SimpleDateFormat dateFormatddmmaaaa = new SimpleDateFormat("dd/MM/yyyy");
			return dateFormatddmmaaaa.format(date);
		} else {
			return cell.getStringCellValue();
		}
	}

}


