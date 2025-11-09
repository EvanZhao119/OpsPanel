package com.opspanel.common.core.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Generic pagination data container.
 *
 * <p>Used to encapsulate paginated list results and metadata (total count, current page, etc.)
 * and return it to frontend in a unified structure.</p>
 *
 * @param <T> type of list elements
 *
 * @author OpsPanel
 */
public class TableDataInfo<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Total number of records */
    private long total;

    /** Current page data list */
    private List<T> rows;

    /** Optional message */
    private String message;

    /** HTTP-like status code (e.g., 200 for success) */
    private int code = 200;

    public TableDataInfo() {}

    public TableDataInfo(List<T> rows, long total) {
        this.rows = rows;
        this.total = total;
    }

    public TableDataInfo(List<T> rows, long total, String message) {
        this.rows = rows;
        this.total = total;
        this.message = message;
    }

    public TableDataInfo(List<T> rows, long total, int code, String message) {
        this.rows = rows;
        this.total = total;
        this.code = code;
        this.message = message;
    }

    // Getters & Setters
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /** Static builder for success response */
    public static <T> TableDataInfo<T> success(List<T> list, long total) {
        return new TableDataInfo<>(list, total, 200, "Query successful");
    }

    /** Static builder for empty data */
    public static <T> TableDataInfo<T> empty() {
        return new TableDataInfo<>(List.of(), 0L, 200, "No data");
    }

    @Override
    public String toString() {
        return "TableDataInfo{" +
                "total=" + total +
                ", rows=" + (rows == null ? "null" : rows.size()) +
                ", message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}
