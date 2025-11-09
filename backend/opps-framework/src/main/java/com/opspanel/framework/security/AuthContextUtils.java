package com.opspanel.framework.security;

import com.opspanel.framework.security.model.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for accessing authentication context.
 */
public class AuthContextUtils {

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static AuthenticatedUser getCurrentUser() {
        Authentication auth = getAuthentication();
        if (auth == null || auth.getPrincipal() == null) return null;

        Object principal = auth.getPrincipal();
        if (principal instanceof AuthenticatedUser user) {
            return user;
        }
        if (principal instanceof UserDetails userDetails) {
            AuthenticatedUser user = new AuthenticatedUser();
            user.setUsername(userDetails.getUsername());
            return user;
        }
        return null;
    }

    public static Long getUserId() {
        AuthenticatedUser user = getCurrentUser();
        return user != null ? user.getUserId() : null;
    }

    public static String getUsername() {
        AuthenticatedUser user = getCurrentUser();
        return user != null ? user.getUsername() : null;
    }

    public static boolean isAuthenticated() {
        Authentication auth = getAuthentication();
        return auth != null && auth.isAuthenticated();
    }
}
