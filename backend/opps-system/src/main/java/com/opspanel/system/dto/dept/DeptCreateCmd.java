package com.opspanel.system.dto.dept;

import lombok.Data;

/** Create command for department. */
@Data
public class DeptCreateCmd {
    public Long parentId;
    public String deptName;
    public Integer sort;
    public Integer status;
}

