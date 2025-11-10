package com.opspanel.system.dto.menu;

import lombok.Data;

/** Query filter for menus. */
@Data
public class MenuQuery {
    public String menuName;
    public Integer status;
    public Integer type;
}
