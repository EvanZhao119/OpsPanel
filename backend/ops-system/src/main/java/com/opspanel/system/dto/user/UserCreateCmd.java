package com.opspanel.system.dto.user;

import lombok.Data;

/** Command to create a user. */
@Data
public class UserCreateCmd {
    private String username;
    private String password;
    private String nickName;
    private String email;
    private String phone;
    private Integer status;
    private Long deptId;
}
