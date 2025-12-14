package com.opspanel.system.dto.dept;

import lombok.Data;

/**
 * Command object for creating a department.
 */
@Data
public class DeptCreateCmd {

    /** Department name */
    private String deptName;

    /** Parent department ID */
    private Long parentId;

    /** Display order */
    private Integer orderNum;

    /** Department status: 1 = enabled, 0 = disabled */
    private Integer status;
}
