package com.opspanel.common.core.controller;

import com.github.pagehelper.PageInfo;
import com.opspanel.common.core.domain.PageDomain;
import com.opspanel.common.core.domain.TableDataInfo;
import com.opspanel.common.core.domain.TableSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Base controller providing common pagination utilities.
 *
 * <p>All API controllers should extend this class to leverage
 * automatic pagination support and unified response formatting.</p>
 *
 * <p>Example usage in a controller:
 * <pre>
 * {@code
 * @RestController
 * @RequestMapping("/api/users")
 * public class UserController extends BaseController {
 *
 *     @GetMapping("/list")
 *     public TableDataInfo<User> list(UserQuery query) {
 *         startPage();  // Auto-parse pagination from request
 *         List<User> list = userService.selectUserList(query);
 *         return getDataTable(list);
 *     }
 * }
 * }
 * </pre>
 *
 * @author OpsPanel
 */
public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Start pagination for current request.
     *
     * <p>Automatically parses pagination parameters from request
     * (pageNum, pageSize, orderByColumn, isAsc) and initializes
     * PageHelper for MyBatis queries.</p>
     *
     * <p>Must be called BEFORE executing the query.</p>
     */
    protected void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = pageDomain.getOrderBy();

        // Use PageHelper to start pagination
        com.github.pagehelper.PageHelper.startPage(pageNum, pageSize, orderBy);
    }

    /**
     * Start pagination with reasonable defaults if parameters are missing.
     *
     * @param pageNum default page number if not provided
     * @param pageSize default page size if not provided
     */
    protected void startPage(Integer pageNum, Integer pageSize) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (pageDomain.getPageNum() == null) {
            pageDomain.setPageNum(pageNum);
        }
        if (pageDomain.getPageSize() == null) {
            pageDomain.setPageSize(pageSize);
        }

        String orderBy = pageDomain.getOrderBy();
        com.github.pagehelper.PageHelper.startPage(
                pageDomain.getPageNum(),
                pageDomain.getPageSize(),
                orderBy
        );
    }

    /**
     * Wrap query results into paginated response.
     *
     * <p>Automatically extracts total count from PageHelper and
     * constructs a standard {@link TableDataInfo} response.</p>
     *
     * @param list the query result list (from MyBatis after startPage)
     * @param <T> type of list elements
     * @return paginated table data
     */
    protected <T> TableDataInfo<T> getDataTable(List<T> list) {
        if (list == null || list.isEmpty()) {
            return TableDataInfo.empty();
        }

        PageInfo<T> pageInfo = new PageInfo<>(list);
        return TableDataInfo.success(list, pageInfo.getTotal());
    }

    /**
     * Wrap query results with custom message.
     *
     * @param list query result list
     * @param message custom message
     * @param <T> type of list elements
     * @return paginated table data with message
     */
    protected <T> TableDataInfo<T> getDataTable(List<T> list, String message) {
        TableDataInfo<T> dataInfo = getDataTable(list);
        dataInfo.setMessage(message);
        return dataInfo;
    }

    /**
     * Return successful response without pagination.
     *
     * @param list data list
     * @param <T> type of list elements
     * @return table data info (total = list size)
     */
    protected <T> TableDataInfo<T> success(List<T> list) {
        return new TableDataInfo<>(list, list == null ? 0 : list.size());
    }

    /**
     * Return error response.
     *
     * @param message error message
     * @param <T> type placeholder
     * @return error table data info
     */
    protected <T> TableDataInfo<T> error(String message) {
        return new TableDataInfo<>(null, 0, 500, message);
    }

    /**
     * Get current page domain from request context.
     *
     * @return current page domain
     */
    protected PageDomain getPageDomain() {
        return TableSupport.getPageDomain();
    }

    /**
     * Clear pagination thread local to prevent memory leak.
     *
     * <p>PageHelper automatically clears after query execution,
     * but this can be used for manual cleanup in exceptional cases.</p>
     */
    protected void clearPage() {
        com.github.pagehelper.PageHelper.clearPage();
    }
}