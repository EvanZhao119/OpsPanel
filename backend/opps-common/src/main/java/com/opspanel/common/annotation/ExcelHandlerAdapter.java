package com.opspanel.common.annotation;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Custom data formatting adapter for Excel export
 */
public interface ExcelHandlerAdapter {
    /**
     * Format value before writing to Excel
     */
    Object format(Object value, String[] args, Cell cell, Workbook wb);
}
