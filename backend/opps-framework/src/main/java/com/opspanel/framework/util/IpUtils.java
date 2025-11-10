package com.opspanel.framework.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Utility class for retrieving client IP address.
 * Supports proxy and load balancer environments.
 */
public class IpUtils {

    public static String getClientIp(HttpServletRequest request) {
        if (request == null) return "unknown";
        String ip = getHeader(request, "X-Forwarded-For");
        if (isUnknown(ip)) ip = getHeader(request, "Proxy-Client-IP");
        if (isUnknown(ip)) ip = getHeader(request, "WL-Proxy-Client-IP");
        if (isUnknown(ip)) ip = request.getRemoteAddr();
        // handle multiple IPs (load balancer)
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private static String getHeader(HttpServletRequest request, String name) {
        return request.getHeader(name);
    }

    private static boolean isUnknown(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }
}
