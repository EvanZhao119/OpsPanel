package com.opspanel.system.dto.dept;

import lombok.Data;

/**
 * Command object for updating a department.
 */
@Data
public class DeptUpdateCmd {

    /** Department ID */
    private Long deptId;

    /** Department name */
    private String deptName;

    /** Parent department ID */
    private Long parentId;

    /** Display order */
    private Integer orderNum;

    /** Department status: 1 = enabled, 0 = disabled */
    private Integer status;
}
