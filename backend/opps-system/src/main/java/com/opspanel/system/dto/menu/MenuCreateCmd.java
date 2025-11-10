package com.opspanel.system.dto.menu;

import lombok.Data;

/** Create command for menu. */
@Data
public class MenuCreateCmd {
    public Long parentId;
    public String menuName;
    public String path;
    public String component;
    public String perms;
    public Integer type;
    public String icon;
    public Integer sort;
    public Integer visible;
    public Integer status;
}
