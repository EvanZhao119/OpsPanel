package com.opspanel.system.dto.user;

import lombok.Data;

/** Command to update a user. */
@Data
public class UserUpdateCmd {
    private Long userId;
    private String nickName;
    private String email;
    private String phone;
    private Integer status;
    private Long deptId;
}
