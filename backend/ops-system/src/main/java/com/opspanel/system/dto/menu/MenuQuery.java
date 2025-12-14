package com.opspanel.system.dto.menu;

import lombok.Data;

/**
 * Query parameters for menu search.
 */
@Data
public class MenuQuery {

    /** Menu name keyword */
    private String menuName;

    /** Parent menu ID for hierarchical filtering */
    private Long parentId;

    /** Menu visibility flag */
    private Integer visible;

    /** Menu status */
    private Integer status;
}
