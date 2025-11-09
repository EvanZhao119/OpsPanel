package com.opspanel.common.core.domain;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Table pagination support utility.
 *
 * <p>Extracts pagination parameters from HTTP request and constructs
 * {@link PageDomain} objects for query execution.</p>
 *
 * <p>This class is thread-safe as it uses Spring's RequestContextHolder.</p>
 *
 * @author OpsPanel
 */
public class TableSupport {

    /** Default page number */
    private static final int DEFAULT_PAGE_NUM = 1;

    /** Default page size */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /** Maximum allowed page size to prevent OOM */
    private static final int MAX_PAGE_SIZE = 500;

    /** Request parameter names */
    private static final String PAGE_NUM = "pageNum";
    private static final String PAGE_SIZE = "pageSize";
    private static final String ORDER_BY_COLUMN = "orderByColumn";
    private static final String IS_ASC = "isAsc";

    /**
     * Build PageDomain from current HTTP request.
     *
     * <p>Extracts pagination parameters and applies validation:
     * <ul>
     *   <li>pageNum: defaults to 1, minimum 1</li>
     *   <li>pageSize: defaults to 10, capped at 500</li>
     *   <li>orderByColumn: SQL-injection protected</li>
     *   <li>isAsc: "asc" or "desc"</li>
     * </ul>
     *
     * @return PageDomain with parsed parameters
     */
    public static PageDomain buildPageRequest() {
        PageDomain pageDomain = new PageDomain();

        try {
            HttpServletRequest request = getRequest();
            if (request == null) {
                return getDefaultPageDomain();
            }

            // Parse page number
            String pageNumStr = request.getParameter(PAGE_NUM);
            Integer pageNum = parsePageNum(pageNumStr);
            pageDomain.setPageNum(pageNum);

            // Parse page size
            String pageSizeStr = request.getParameter(PAGE_SIZE);
            Integer pageSize = parsePageSize(pageSizeStr);
            pageDomain.setPageSize(pageSize);

            // Parse order by column
            String orderByColumn = request.getParameter(ORDER_BY_COLUMN);
            if (orderByColumn != null && !orderByColumn.isBlank()) {
                pageDomain.setOrderByColumn(sanitizeColumnName(orderByColumn));
            }

            // Parse sort direction
            String isAsc = request.getParameter(IS_ASC);
            if (isAsc != null && !isAsc.isBlank()) {
                pageDomain.setIsAsc(normalizeSortDirection(isAsc));
            }

        } catch (Exception e) {
            // Fallback to defaults on any parsing error
            return getDefaultPageDomain();
        }

        return pageDomain;
    }

    /**
     * Get current HTTP servlet request.
     *
     * @return current request or null if not in web context
     */
    private static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    /**
     * Get PageDomain stored in request attribute (if previously stored).
     *
     * @return stored PageDomain or null
     */
    public static PageDomain getPageDomain() {
        HttpServletRequest request = getRequest();
        if (request != null) {
            Object attr = request.getAttribute("pageDomain");
            if (attr instanceof PageDomain) {
                return (PageDomain) attr;
            }
        }
        return buildPageRequest();
    }

    /**
     * Parse page number with validation.
     */
    private static Integer parsePageNum(String pageNumStr) {
        if (pageNumStr == null || pageNumStr.isBlank()) {
            return DEFAULT_PAGE_NUM;
        }
        try {
            int pageNum = Integer.parseInt(pageNumStr);
            return Math.max(pageNum, 1); // Minimum page 1
        } catch (NumberFormatException e) {
            return DEFAULT_PAGE_NUM;
        }
    }

    /**
     * Parse page size with validation and cap.
     */
    private static Integer parsePageSize(String pageSizeStr) {
        if (pageSizeStr == null || pageSizeStr.isBlank()) {
            return DEFAULT_PAGE_SIZE;
        }
        try {
            int pageSize = Integer.parseInt(pageSizeStr);
            if (pageSize < 1) return DEFAULT_PAGE_SIZE;
            return Math.min(pageSize, MAX_PAGE_SIZE); // Cap at max
        } catch (NumberFormatException e) {
            return DEFAULT_PAGE_SIZE;
        }
    }

    /**
     * Sanitize column name to prevent SQL injection.
     *
     * <p>Allows only alphanumeric characters, underscores, and dots
     * (for table.column notation). Converts camelCase to snake_case.</p>
     */
    private static String sanitizeColumnName(String column) {
        if (column == null) return null;

        // Convert camelCase to snake_case
        String snakeCase = column.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();

        // Remove any non-safe characters
        return snakeCase.replaceAll("[^a-zA-Z0-9_.]", "");
    }

    /**
     * Normalize sort direction to "asc" or "desc".
     */
    private static String normalizeSortDirection(String direction) {
        if (direction == null) return "asc";

        String lower = direction.toLowerCase().trim();
        if ("desc".equals(lower) || "descending".equals(lower)) {
            return "desc";
        }
        return "asc";
    }

    /**
     * Get default PageDomain (page 1, size 10, no sorting).
     */
    private static PageDomain getDefaultPageDomain() {
        return new PageDomain(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
    }

    /**
     * Store PageDomain in request attribute for later retrieval.
     */
    public static void setPageDomain(PageDomain pageDomain) {
        HttpServletRequest request = getRequest();
        if (request != null) {
            request.setAttribute("pageDomain", pageDomain);
        }
    }
}