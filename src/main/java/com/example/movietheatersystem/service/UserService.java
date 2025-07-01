package com.example.movietheatersystem.service;

import com.example.movietheatersystem.entity.User;
import com.example.movietheatersystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }
        user.setCreateTime(LocalDateTime.now());
        user.setRole(User.Role.NORMAL); // 默认注册为普通用户
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        // 校验原密码
        if (!com.example.movietheatersystem.tool.BCryptUtil.isPasswordMatch(oldPassword, user.getPassword())) {
            return false;
        }
        user.setPassword(com.example.movietheatersystem.tool.BCryptUtil.encryptPassword(newPassword));
        userRepository.save(user);
        return true;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}