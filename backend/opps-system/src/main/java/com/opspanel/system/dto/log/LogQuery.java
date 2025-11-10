package com.opspanel.system.dto.log;

import lombok.Data;

/** Unified log query. */
@Data
public class LogQuery {
    public String usernameOrTitle;
    public Integer status;
    public String dateFrom; // yyyy-MM-dd
    public String dateTo;   // yyyy-MM-dd
}
