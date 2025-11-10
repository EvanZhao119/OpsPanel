package com.opspanel.system.dto.dept;

import lombok.Data;

/** Query filter for departments. */
@Data
public class DeptQuery {
    public String deptName;
    public Integer status;
}

