package com.opspanel.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("sys_menu")
public class SysMenu {
    @TableId(type = IdType.AUTO)
    private Long menuId;
    private Long parentId;
    private String menuName;
    private String path;
    private String component;
    private String perms;
    private Integer type;   // 0=dir,1=menu,2=button
    private Integer sort;
    private Integer visible;
    private Integer status;
    @TableLogic
    private Integer deleted;
}
