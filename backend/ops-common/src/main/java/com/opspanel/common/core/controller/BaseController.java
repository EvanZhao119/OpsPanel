package com.opspanel.common.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opspanel.common.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Base controller providing unified pagination and response utilities.
 *
 * <p>This class simplifies pagination setup using MyBatis-Plus and
 * standardizes API responses with {@link ApiResponse}.</p>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * @RestController
 * @RequestMapping("/api/system/user")
 * public class SysUserController extends BaseController {
 *
 *     @GetMapping("/list")
 *     public ApiResponse<IPage<SysUser>> list(UserQuery query,
 *                                             @RequestParam(defaultValue = "1") int pageNum,
 *                                             @RequestParam(defaultValue = "10") int pageSize) {
 *         Page<SysUser> page = buildPage(pageNum, pageSize, request);
 *         IPage<SysUser> result = userService.page(page, query.toWrapper());
 *         return success(result);
 *     }
 * }
 * }
 * </pre>
 *
 * @author OpsPanel
 */
public abstract class BaseController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Build a MyBatis-Plus {@link Page} object from HTTP request parameters.
     *
     * <p>Parameters supported:
     * <ul>
     *   <li>pageNum — current page number (default: 1)</li>
     *   <li>pageSize — number of records per page (default: 10)</li>
     *   <li>orderByColumn — optional column name for ordering</li>
     *   <li>isAsc — asc / desc (default: asc)</li>
     * </ul>
     * </p>
     *
     * @param request current HTTP request
     * @param <T> entity type
     * @return configured Page object
     */
    protected <T> Page<T> buildPage(HttpServletRequest request) {
        int pageNum = parseInt(request.getParameter("pageNum"), 1);
        int pageSize = parseInt(request.getParameter("pageSize"), 10);
        String orderBy = request.getParameter("orderByColumn");
        String isAsc = request.getParameter("isAsc");

        Page<T> page = new Page<>(pageNum, pageSize);

        if (StringUtils.hasText(orderBy)) {
            boolean asc = !"desc".equalsIgnoreCase(isAsc);
            page.addOrder(asc
                    ? com.baomidou.mybatisplus.core.metadata.OrderItem.asc(orderBy)
                    : com.baomidou.mybatisplus.core.metadata.OrderItem.desc(orderBy));
        }

        return page;
    }

    /**
     * Build Page object directly with parameters.
     *
     * @param pageNum  current page number
     * @param pageSize page size
     * @param <T> entity type
     * @return Page object
     */
    protected <T> Page<T> buildPage(int pageNum, int pageSize) {
        return new Page<>(pageNum, pageSize);
    }

    /**
     * Wrap paginated data into a standard {@link ApiResponse}.
     *
     * @param page MyBatis-Plus page result
     * @param <T>  entity type
     * @return standardized API response
     */
    protected <T> ApiResponse<IPage<T>> success(IPage<T> page) {
        return ApiResponse.ok(page);
    }

    /**
     * Wrap arbitrary data into success response.
     *
     * @param data data payload
     * @param <T> data type
     * @return success response
     */
    protected <T> ApiResponse<T> success(T data) {
        return ApiResponse.ok(data);
    }

    /**
     * Return an error response with custom message.
     *
     * @param message error message
     * @return error response
     */
    protected ApiResponse<Void> error(String message) {
        return ApiResponse.error(message);
    }

    /**
     * Parse integer with fallback.
     *
     * @param value input string
     * @param defaultVal default value
     * @return parsed integer or default value
     */
    private int parseInt(String value, int defaultVal) {
        try {
            return StringUtils.hasText(value) ? Integer.parseInt(value) : defaultVal;
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
}
