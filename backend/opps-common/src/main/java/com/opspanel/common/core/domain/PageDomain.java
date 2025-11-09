package com.opspanel.common.core.domain;

import java.io.Serial;
import java.io.Serializable;

/**
 * Pagination and sorting parameter encapsulation for API queries.
 *
 * <p>Handles page number, page size, sorting column, and direction.
 * Prevents SQL injection through sanitization.</p>
 *
 * <p>Example:
 * <pre>
 * PageDomain page = new PageDomain(1, 20, "create_time", "desc");
 * </pre>
 *
 * @author OpsPanel
 */
public class PageDomain implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Current page number (starts at 1) */
    private Integer pageNum = 1;

    /** Number of items per page */
    private Integer pageSize = 10;

    /** Column name to sort by */
    private String orderByColumn;

    /** Sorting direction: "asc" or "desc" */
    private String isAsc = "asc";

    /** Optional manual ORDER BY override */
    private String orderBy;

    public PageDomain() {}

    public PageDomain(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public PageDomain(Integer pageNum, Integer pageSize, String orderByColumn, String isAsc) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.orderByColumn = orderByColumn;
        this.isAsc = isAsc;
    }

    /** Build SQL ORDER BY clause safely */
    public String getOrderBy() {
        if (orderBy != null) return orderBy;
        if (orderByColumn == null || orderByColumn.isBlank()) return "";
        String safeColumn = orderByColumn.replaceAll("[^a-zA-Z0-9_]", "");
        return safeColumn + " " + (isAsc == null ? "asc" : isAsc);
    }

    // Getters & Setters (chainable)
    public Integer getPageNum() {
        return pageNum;
    }

    public PageDomain setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public PageDomain setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public String getOrderByColumn() {
        return orderByColumn;
    }

    public PageDomain setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
        return this;
    }

    public String getIsAsc() {
        return isAsc;
    }

    public PageDomain setIsAsc(String isAsc) {
        this.isAsc = isAsc;
        return this;
    }

    public String getOrderByClause() {
        return getOrderBy();
    }

    @Override
    public String toString() {
        return "PageDomain{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", orderByColumn='" + orderByColumn + '\'' +
                ", isAsc='" + isAsc + '\'' +
                '}';
    }
}
