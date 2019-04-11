package poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PoiUtil {
    public static Workbook getReadWorkbookType(String filepath) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filepath);
            //xlsx---2007
            if (filepath.toLowerCase().endsWith("xlsx")) {
                return new XSSFWorkbook(inputStream);
            } else if (filepath.toLowerCase().endsWith("xls")) {
                //xls---2003
                return new HSSFWorkbook(inputStream);
            } else {
                return null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getCellStringVal(Cell cell) {
        /*CellType cellType = cell.getCellTypeEnum();

        switch (cellType) {
            case NUMERIC:
                return cell.getStringCellValue();
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            case ERROR:
                return String.valueOf(cell.getErrorCellValue());
            default:
                return "";
        }*/
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }
}
