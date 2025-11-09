package com.opspanel.common.constant;

/**
 * Global user-related constants used across modules.
 */
public final class UserConstants {

    private UserConstants() {
        // Prevent instantiation
    }

    /* ==========================================================
       System User Identifiers
       ========================================================== */

    /** Marker for internal system users */
    public static final String SYSTEM_USER = "SYS_USER";

    /** Default user type: internal system user */
    public static final String SYSTEM_USER_TYPE = "00";

    /** Default user type: registered (external) user */
    public static final String REGISTER_USER_TYPE = "01";


    /* ==========================================================
       Account & Role Status
       ========================================================== */

    /** Normal / active state */
    public static final String STATUS_NORMAL = "0";

    /** Disabled / exception state */
    public static final String STATUS_DISABLED = "1";

    /** Yes / No flags */
    public static final String YES = "Y";
    public static final String NO = "N";

    /** Uniqueness indicators */
    public static final boolean UNIQUE = true;
    public static final boolean NOT_UNIQUE = false;


    /* ==========================================================
       Validation Rules
       ========================================================== */

    /** Username length boundaries */
    public static final int USERNAME_MIN_LENGTH = 2;
    public static final int USERNAME_MAX_LENGTH = 20;

    /** Password length boundaries */
    public static final int PASSWORD_MIN_LENGTH = 6;
    public static final int PASSWORD_MAX_LENGTH = 20;

    /** Regular expression for mobile phone number validation */
    public static final String MOBILE_PATTERN =
            "^\\+?[1-9]\\d{1,14}$";

    /** Regular expression for email validation */
    public static final String EMAIL_PATTERN =
            "^[\\p{L}0-9._%+!#$&'*/=?`{|}~^-]+@[\\p{L}0-9.-]+\\.[\\p{L}]{2,63}$";

}
