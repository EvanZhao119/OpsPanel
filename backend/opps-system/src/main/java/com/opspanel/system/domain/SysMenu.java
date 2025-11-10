package com.opspanel.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/** System menu entity. */
@Data
@TableName("sys_menu")
public class SysMenu {

    @TableId(type = IdType.AUTO)
    private Long menuId;

    private String menuName;
    private String path;
    private String component;
    private String icon;
    private Long parentId;
    private Integer orderNum;
    private Integer visible;          // 0=visible, 1=hidden
    private Integer status;           // 1=enabled, 0=disabled

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
