package com.opspanel.common.util.poi;

import com.opspanel.common.annotation.Excel;
import com.opspanel.common.annotation.ExcelHandlerAdapter;
import com.opspanel.common.annotation.Excels;
import com.opspanel.common.exception.BusinessException;
import com.opspanel.common.exception.GlobalErrorCode;
import com.opspanel.common.util.file.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Excel Utility for exporting and importing data using Apache POI with annotation support.
 *
 * Provides methods for:
 * - Exporting List<T> objects into Excel (.xlsx) using @Excel annotations
 * - Importing Excel into List<T> using @Excel annotations
 *
 * This class delegates filesystem I/O to {@link com.opspanel.common.util.file.FileUtils}.
 */
public final class ExcelUtil {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    private ExcelUtil() {
        throw new UnsupportedOperationException("ExcelUtil is a utility class and cannot be instantiated.");
    }

    // =====================================================
    // Export Section
    // =====================================================

    /**
     * Export a list of objects to Excel (.xlsx) with @Excel annotation support.
     *
     * @param dataList  list of objects to write
     * @param sheetName Excel sheet name
     * @param clazz     object type
     * @param output    output stream
     * @param <T>       data type
     */
    public static <T> void exportToExcel(List<T> dataList, String sheetName, Class<T> clazz, OutputStream output) {
        Objects.requireNonNull(dataList, "Data list cannot be null");
        Objects.requireNonNull(clazz, "Class type cannot be null");
        Objects.requireNonNull(output, "Output stream cannot be null");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName != null ? sheetName : "Sheet1");

            // Get all exportable fields with @Excel annotation
            List<ExcelFieldInfo> fieldInfos = getExcelFields(clazz, Excel.Type.EXPORT);

            if (fieldInfos.isEmpty()) {
                log.warn("[ExcelUtil] No @Excel annotated fields found for export in class {}", clazz.getName());
            }

            // Create styles
            Map<String, CellStyle> styles = createStyles(workbook);

            // Create header row
            createHeaderRow(sheet, fieldInfos, styles, workbook);

            // Fill data rows
            fillDataRows(sheet, dataList, fieldInfos, styles, workbook);

            // Auto-size columns
            autoSizeColumns(sheet, fieldInfos);

            // Apply merging if needed
            applyMerging(sheet, dataList, fieldInfos);

            workbook.write(output);
            output.flush();

            log.info("[ExcelUtil] Exported {} rows successfully to sheet '{}'", dataList.size(), sheetName);
        } catch (Exception e) {
            log.error("[ExcelUtil] Excel export failed: {}", e.getMessage(), e);
            throw BusinessException.of(GlobalErrorCode.EXCEL_EXPORT_ERROR,
                    "Excel export failed: " + e.getMessage());
        }
    }

    /**
     * Export data to a physical Excel file path.
     *
     * This method uses FileUtils.newOutputStream(filePath) to create the output stream.
     * Make sure FileUtils provides newOutputStream(String).
     */
    public static <T> Path exportToFile(List<T> dataList, String filePath, Class<T> clazz) {
        try (OutputStream os = FileUtils.newOutputStream(filePath)) {
            exportToExcel(dataList, "Sheet1", clazz, os);
            return Path.of(filePath);
        } catch (IOException e) {
            log.error("[ExcelUtil] File export failed: {}", e.getMessage(), e);
            throw BusinessException.of(GlobalErrorCode.EXCEL_EXPORT_ERROR,
                    "Failed to write Excel file: " + e.getMessage());
        }
    }

    // =====================================================
    // Field discovery and helpers
    // =====================================================

    /**
     * Get all fields with @Excel annotation, sorted by sort order
     */
    private static List<ExcelFieldInfo> getExcelFields(Class<?> clazz, Excel.Type operationType) {
        List<ExcelFieldInfo> fieldInfos = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            // Check for single @Excel annotation
            Excel excel = field.getAnnotation(Excel.class);
            if (excel != null && isFieldExportable(excel, operationType)) {
                fieldInfos.add(new ExcelFieldInfo(field, excel));
            }

            // Check for multiple @Excel annotations
            Excels excels = field.getAnnotation(Excels.class);
            if (excels != null) {
                for (Excel excelAnno : excels.value()) {
                    if (isFieldExportable(excelAnno, operationType)) {
                        fieldInfos.add(new ExcelFieldInfo(field, excelAnno));
                    }
                }
            }
        }

        // Sort by sort order
        fieldInfos.sort(Comparator.comparingInt(f -> f.excel.sort()));
        return fieldInfos;
    }

    private static boolean isFieldExportable(Excel excel, Excel.Type operationType) {
        return excel.isExport() &&
                (excel.type() == Excel.Type.ALL || excel.type() == operationType);
    }

    // =====================================================
    // Styles and header creation
    // =====================================================

    /**
     * Create cell styles for header and data
     */
    private static Map<String, CellStyle> createStyles(Workbook workbook) {
        Map<String, CellStyle> styles = new HashMap<>();

        // Default header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setBorders(headerStyle);
        styles.put("header", headerStyle);

        // Default data style
        CellStyle dataStyle = workbook.createCellStyle();
        setBorders(dataStyle);
        styles.put("data", dataStyle);

        return styles;
    }

    private static void setBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    /**
     * Create header row with @Excel annotation settings
     */
    private static void createHeaderRow(Sheet sheet, List<ExcelFieldInfo> fieldInfos,
                                        Map<String, CellStyle> styles, Workbook workbook) {
        Row headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(25);

        for (int i = 0; i < fieldInfos.size(); i++) {
            ExcelFieldInfo fieldInfo = fieldInfos.get(i);
            Excel excel = fieldInfo.excel;

            Cell cell = headerRow.createCell(i);
            cell.setCellValue(excel.name().isEmpty() ? fieldInfo.field.getName() : excel.name());

            // Create custom header style
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.cloneStyleFrom(styles.get("header"));

            Font font = workbook.createFont();
            font.setBold(true);
            font.setColor(excel.headerColor().getIndex());
            headerStyle.setFont(font);
            headerStyle.setFillForegroundColor(excel.backgroundColor().getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            cell.setCellStyle(headerStyle);
        }
    }

    // =====================================================
    // Data writing
    // =====================================================

    /**
     * Fill data rows with field values
     */
    private static <T> void fillDataRows(Sheet sheet, List<T> dataList,
                                         List<ExcelFieldInfo> fieldInfos,
                                         Map<String, CellStyle> styles, Workbook workbook)
            throws IllegalAccessException {

        int rowNum = 1;
        Map<String, Double> statistics = new HashMap<>();

        for (T data : dataList) {
            Row row = sheet.createRow(rowNum++);

            for (int i = 0; i < fieldInfos.size(); i++) {
                ExcelFieldInfo fieldInfo = fieldInfos.get(i);
                Excel excel = fieldInfo.excel;
                Field field = fieldInfo.field;

                Cell cell = row.createCell(i);
                Object value = field.get(data);

                // Handle nested property
                if (!excel.targetAttr().isEmpty() && value != null) {
                    value = getNestedProperty(value, excel.targetAttr());
                }

                // Apply custom handler
                if (excel.handler() != ExcelHandlerAdapter.class) {
                    try {
                        ExcelHandlerAdapter handler = (ExcelHandlerAdapter) excel.handler().getDeclaredConstructor().newInstance();
                        value = handler.format(value, excel.args(), cell, workbook);
                    } catch (Exception e) {
                        log.warn("[ExcelUtil] Failed to apply custom handler: {}", e.getMessage());
                    }
                }

                // Set cell value with formatting
                setCellValue(cell, value, excel, workbook);

                // Apply cell style
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.cloneStyleFrom(styles.get("data"));
                cellStyle.setAlignment(excel.align());
                if (excel.wrapText()) {
                    cellStyle.setWrapText(true);
                }
                cell.setCellStyle(cellStyle);

                // Collect statistics
                if (excel.isStatistics() && value instanceof Number) {
                    String key = excel.name();
                    statistics.put(key, statistics.getOrDefault(key, 0.0) + ((Number) value).doubleValue());
                }
            }
        }

        // Add statistics row if needed
        if (!statistics.isEmpty()) {
            addStatisticsRow(sheet, rowNum, fieldInfos, statistics, styles);
        }
    }

    private static void addStatisticsRow(Sheet sheet, int rowNum, List<ExcelFieldInfo> fieldInfos,
                                         Map<String, Double> statistics, Map<String, CellStyle> styles) {
        Row statRow = sheet.createRow(rowNum);

        for (int i = 0; i < fieldInfos.size(); i++) {
            ExcelFieldInfo fieldInfo = fieldInfos.get(i);
            Cell cell = statRow.createCell(i);

            if (i == 0) {
                cell.setCellValue("Total");
            } else if (statistics.containsKey(fieldInfo.excel.name())) {
                cell.setCellValue(statistics.get(fieldInfo.excel.name()));
            }

            CellStyle style = sheet.getWorkbook().createCellStyle();
            style.cloneStyleFrom(styles.get("data"));
            Font font = sheet.getWorkbook().createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }
    }

    // =====================================================
    // Formatting helpers
    // =====================================================

    /**
     * Set cell value based on field type and Excel annotation
     */
    private static void setCellValue(Cell cell, Object value, Excel excel, Workbook workbook) {
        if (value == null) {
            cell.setCellValue(excel.defaultValue());
            return;
        }

        // Apply conversion expression
        if (!excel.readConverterExp().isEmpty()) {
            value = applyConverter(value, excel.readConverterExp());
        }

        // Handle different data types
        if (value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat(excel.dateFormat());
            cell.setCellValue(sdf.format(value));
        } else if (value instanceof Number) {
            double numValue = ((Number) value).doubleValue();

            // Apply scale and rounding
            if (excel.scale() >= 0) {
                BigDecimal bd = new BigDecimal(numValue);
                bd = bd.setScale(excel.scale(), excel.roundingMode());
                numValue = bd.doubleValue();
            }

            cell.setCellValue(numValue);
        } else if (excel.cellType() == Excel.ColumnType.IMAGE && value instanceof byte[]) {
            // Handle image (simplified)
            cell.setCellValue("[IMAGE]");
        } else {
            String strValue = value.toString();
            if (!excel.suffix().isEmpty()) {
                strValue += excel.suffix();
            }
            cell.setCellValue(strValue);
        }
    }

    /**
     * Apply converter expression (e.g., "0=Male,1=Female")
     */
    private static Object applyConverter(Object value, String converterExp) {
        String[] pairs = converterExp.split(",");
        String valueStr = value.toString();

        for (String pair : pairs) {
            String[] kv = pair.split("=");
            if (kv.length == 2 && kv[0].trim().equals(valueStr)) {
                return kv[1].trim();
            }
        }
        return value;
    }

    /**
     * Get nested property value using dot notation
     */
    private static Object getNestedProperty(Object obj, String propertyPath) {
        try {
            String[] parts = propertyPath.split("\\.");
            Object current = obj;
            for (String part : parts) {
                Field field = current.getClass().getDeclaredField(part);
                field.setAccessible(true);
                current = field.get(current);
                if (current == null) break;
            }
            return current;
        } catch (Exception e) {
            log.warn("[ExcelUtil] Failed to get nested property: {}", e.getMessage());
            return null;
        }
    }

    // =====================================================
    // Column sizing and merging
    // =====================================================

    /**
     * Auto-size columns based on @Excel width setting
     */
    private static void autoSizeColumns(Sheet sheet, List<ExcelFieldInfo> fieldInfos) {
        for (int i = 0; i < fieldInfos.size(); i++) {
            Excel excel = fieldInfos.get(i).excel;
            int width = (int) (excel.width() * 256);
            sheet.setColumnWidth(i, width);
        }
    }

    /**
     * Apply cell merging for same values if needed
     */
    private static <T> void applyMerging(Sheet sheet, List<T> dataList, List<ExcelFieldInfo> fieldInfos) {
        for (int i = 0; i < fieldInfos.size(); i++) {
            Excel excel = fieldInfos.get(i).excel;
            if (!excel.needMerge()) continue;

            int startRow = 1;
            Object prevValue = null;

            for (int row = 1; row <= dataList.size(); row++) {
                Row currentRow = sheet.getRow(row);
                if (currentRow == null) continue;

                Cell cell = currentRow.getCell(i);
                Object currentValue = getCellValueAsObject(cell);

                if (prevValue != null && !Objects.equals(prevValue, currentValue)) {
                    if (row - startRow > 1) {
                        sheet.addMergedRegion(new CellRangeAddress(startRow, row - 1, i, i));
                    }
                    startRow = row;
                }
                prevValue = currentValue;
            }

            // Merge last group
            if (dataList.size() - startRow > 0) {
                sheet.addMergedRegion(new CellRangeAddress(startRow, dataList.size(), i, i));
            }
        }
    }

    private static Object getCellValueAsObject(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> cell.getNumericCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            default -> null;
        };
    }

    // =====================================================
    // Import Section
    // =====================================================

    /**
     * Import Excel file into list of objects with @Excel annotation support (from InputStream)
     */
    public static <T> List<T> importFromExcel(InputStream input, Class<T> clazz) {
        Objects.requireNonNull(input, "Excel input stream cannot be null");
        Objects.requireNonNull(clazz, "Class type cannot be null");

        try (Workbook workbook = new XSSFWorkbook(input)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null || sheet.getPhysicalNumberOfRows() == 0) {
                throw BusinessException.of(GlobalErrorCode.EXCEL_IMPORT_ERROR, "Excel sheet is empty");
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw BusinessException.of(GlobalErrorCode.EXCEL_IMPORT_ERROR, "Missing header row");
            }

            // Get field mappings
            List<ExcelFieldInfo> fieldInfos = getExcelFields(clazz, Excel.Type.IMPORT);
            Map<String, ExcelFieldInfo> headerMap = fieldInfos.stream()
                    .collect(Collectors.toMap(f -> f.excel.name().isEmpty() ?
                            f.field.getName() : f.excel.name(), f -> f));

            // Map headers to column indices
            Map<Integer, ExcelFieldInfo> columnMapping = new HashMap<>();
            for (Cell cell : headerRow) {
                String headerName = cell.getStringCellValue();
                if (headerMap.containsKey(headerName)) {
                    columnMapping.put(cell.getColumnIndex(), headerMap.get(headerName));
                }
            }

            // Read data rows
            List<T> result = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                T instance = clazz.getDeclaredConstructor().newInstance();

                for (Map.Entry<Integer, ExcelFieldInfo> entry : columnMapping.entrySet()) {
                    Cell cell = row.getCell(entry.getKey());
                    ExcelFieldInfo fieldInfo = entry.getValue();
                    Object value = getCellValue(cell);

                    // Convert and set value
                    setFieldValue(instance, fieldInfo, value);
                }

                result.add(instance);
            }

            log.info("[ExcelUtil] Imported {} rows from Excel", result.size());
            return result;

        } catch (Exception e) {
            log.error("[ExcelUtil] Excel import failed: {}", e.getMessage(), e);
            throw BusinessException.of(GlobalErrorCode.EXCEL_IMPORT_ERROR,
                    "Excel import failed: " + e.getMessage());
        }
    }

    /**
     * Convenience: import from a file path using FileUtils.newInputStream(filePath).
     * Make sure FileUtils provides newInputStream(String).
     */
    public static <T> List<T> importFromExcel(String filePath, Class<T> clazz) {
        try (InputStream is = FileUtils.newInputStream(filePath)) {
            return importFromExcel(is, clazz);
        } catch (IOException e) {
            log.error("[ExcelUtil] File import failed: {}", e.getMessage(), e);
            throw BusinessException.of(GlobalErrorCode.EXCEL_IMPORT_ERROR,
                    "Failed to read Excel file: " + e.getMessage());
        }
    }

    private static Object getCellValue(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell) ?
                    cell.getDateCellValue() : cell.getNumericCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            case FORMULA -> cell.getCellFormula();
            default -> null;
        };
    }

    private static void setFieldValue(Object instance, ExcelFieldInfo fieldInfo, Object value)
            throws IllegalAccessException {
        if (value == null) return;

        Field field = fieldInfo.field;
        Class<?> fieldType = field.getType();

        try {
            if (fieldType == String.class) {
                field.set(instance, value.toString());
            } else if (fieldType == Integer.class || fieldType == int.class) {
                field.set(instance, value instanceof Number ? ((Number) value).intValue() :
                        Integer.parseInt(value.toString()));
            } else if (fieldType == Long.class || fieldType == long.class) {
                field.set(instance, value instanceof Number ? ((Number) value).longValue() :
                        Long.parseLong(value.toString()));
            } else if (fieldType == Double.class || fieldType == double.class) {
                field.set(instance, value instanceof Number ? ((Number) value).doubleValue() :
                        Double.parseDouble(value.toString()));
            } else if (fieldType == Date.class) {
                SimpleDateFormat sdf = new SimpleDateFormat(fieldInfo.excel.dateFormat());
                field.set(instance, sdf.parse(value.toString()));
            } else {
                field.set(instance, value);
            }
        } catch (Exception e) {
            log.warn("[ExcelUtil] Failed to set field value: {} = {}", field.getName(), value);
        }
    }

    /**
     * Internal class to hold field and Excel annotation info
     */
    private static class ExcelFieldInfo {
        Field field;
        Excel excel;

        ExcelFieldInfo(Field field, Excel excel) {
            this.field = field;
            this.excel = excel;
        }
    }
}
