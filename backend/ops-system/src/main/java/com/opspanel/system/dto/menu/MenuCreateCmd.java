package com.opspanel.system.dto.menu;

import lombok.Data;

/**
 * Command object for creating a menu.
 */
@Data
public class MenuCreateCmd {

    /** Menu name */
    private String menuName;

    /** Parent menu ID */
    private Long parentId;

    /** Frontend route path */
    private String path;

    /** Frontend component path */
    private String component;

    /** Menu icon */
    private String icon;

    /** Display order */
    private Integer orderNum;

    /** Visibility flag: 0 = visible, 1 = hidden */
    private Integer visible;

    /** Menu status: 1 = enabled, 0 = disabled */
    private Integer status;
}
