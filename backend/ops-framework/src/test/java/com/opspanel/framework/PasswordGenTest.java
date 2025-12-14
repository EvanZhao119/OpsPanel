package com.opspanel.framework;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordGenTest {
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String raw = "123456"; // 你想要的登录密码
        String encoded = encoder.encode(raw);
        System.out.println("Encoded password = " + encoded);
    }
}

