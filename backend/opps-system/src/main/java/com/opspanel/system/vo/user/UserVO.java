package com.opspanel.system.vo.user;

import lombok.Data;

/**
 * View object used for returning user information to the frontend.
 */
@Data
public class UserVO {
    private Long userId;
    private String username;
    private String nickName;
    private String email;
    private String phone;
    private Integer status;
    private Long deptId;
}

