package com.example.movietheatersystem.tool;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BCryptUtil {

    // 使用 Spring Security 提供的 BCryptPasswordEncoder
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 加密密码（生成带 salt 的 BCrypt 哈希）
     *
     * @param rawPassword 明文密码
     * @return 加密后的密码（含 salt）
     */
    public static String encryptPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 验证密码是否匹配
     *
     * @param rawPassword 明文密码
     * @param encodedPassword 数据库中存储的加密密码
     * @return 是否匹配
     */
    public static boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    // 可选：测试用例
    public static void main(String[] args) {
        String rawPassword = "111111";

        // 加密
        String encrypted = encryptPassword(rawPassword);
        System.out.println("加密后的密码: " + encrypted);

        // 验证
        boolean match = isPasswordMatch(rawPassword, encrypted);
        System.out.println("密码是否匹配: " + match); // 应输出 true

        // 测试错误密码
        boolean wrongMatch = isPasswordMatch("wrongpassword", encrypted);
        System.out.println("错误密码是否匹配: " + wrongMatch); // 应输出 false
    }
}