package com.teo.mpspedidosback.util;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class ExcelUtils {

    public static double getNumericCellValue(Cell cell) {
        if (cell != null) {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getNumericCellValue();
            }
        }
        return 0.0; // Valor predeterminado si no es numérico o la celda es nula
    }

    public static long getLongValue(Cell cell) {
        double numericValue = getNumericCellValue(cell);
        return (long) numericValue;
    }

    public static Integer getIntegerValue(Cell cell) {
        if (cell != null) {
            if (cell.getCellType() == CellType.NUMERIC) {
                // Convierte el valor numérico a Integer
                return (int) cell.getNumericCellValue();
            }
        }
        return null; // Valor p
    }
}