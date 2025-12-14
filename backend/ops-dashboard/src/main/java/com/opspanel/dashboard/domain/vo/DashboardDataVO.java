package com.opspanel.dashboard.domain.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Standard response structure for widget data.
 */
@Data
public class DashboardDataVO {

    /** Data type: STAT / TIMESERIES / TABLE / CUSTOM */
    private String type;

    /** Title suggested by backend, frontend may override */
    private String title;

    /** Main value for STAT widgets */
    private Object value;

    /** Extra metadata, such as unit or trend */
    private Map<String, Object> meta;

    /** Data payload for TIMESERIES / TABLE / CUSTOM */
    private Object data;

    /* ---------- Helper factory methods ---------- */

    public static DashboardDataVO stat(String title, Object value) {
        DashboardDataVO vo = new DashboardDataVO();
        vo.setType("STAT");
        vo.setTitle(title);
        vo.setValue(value);
        return vo;
    }

    public static DashboardDataVO percentage(String title, double rate) {
        DashboardDataVO vo = stat(title, rate);
        Map<String, Object> meta = new HashMap<>();
        meta.put("unit", "%");
        vo.setMeta(meta);
        return vo;
    }

    public static DashboardDataVO timeSeries(String title, Object series) {
        DashboardDataVO vo = new DashboardDataVO();
        vo.setType("TIMESERIES");
        vo.setTitle(title);
        vo.setData(series);
        return vo;
    }

    public static DashboardDataVO table(String title, Object rows) {
        DashboardDataVO vo = new DashboardDataVO();
        vo.setType("TABLE");
        vo.setTitle(title);
        vo.setData(rows);
        return vo;
    }
}
