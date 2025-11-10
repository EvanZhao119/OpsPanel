package com.opspanel.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("sys_dept")
public class SysDept {
    @TableId(type = IdType.AUTO)
    private Long deptId;
    private Long parentId;
    private String deptName;
    private Integer sort;
    private Integer status;
    @TableLogic
    private Integer deleted;
}
