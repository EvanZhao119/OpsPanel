package com.opspanel.system.dto.dept;

import lombok.Data;

/**
 * Query parameters for department search.
 */
@Data
public class DeptQuery {

    /** Department name keyword */
    private String deptName;

    /** Parent department ID for hierarchical filtering */
    private Long parentId;

    /** Department status */
    private Integer status;
}
