package xmlprojeto.beans;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
@ManagedBean
@ViewScoped
public class ExcelBean {

    private List<List<String>> data;

    @PostConstruct
    public void init() {
        data = new ArrayList<>();
        readExcel("c:\\projetoxml\\Extração SIAFI-Web Dedução DDF025.xlsx");
    }

    public void readExcel(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            rowData.add(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            rowData.add(String.valueOf(cell.getNumericCellValue()));
                            break;
                        default:
                            rowData.add("");
                    }
                }
                data.add(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<List<String>> getData() {
        return data;
    }
}
