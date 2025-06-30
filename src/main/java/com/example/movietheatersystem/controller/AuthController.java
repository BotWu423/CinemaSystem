package com.example.movietheatersystem.controller;

import com.example.movietheatersystem.dto.LoginRequest;
import com.example.movietheatersystem.dto.LoginResponse;
import com.example.movietheatersystem.security.JwtTokenProvider;
import com.example.movietheatersystem.security.UserPrinciple;
import com.example.movietheatersystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        // 执行认证（用户名 + 密码）
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // 将认证对象放入安全上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成 JWT Token
        String token = jwtTokenProvider.generateToken(authentication);

        // 获取用户信息
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        Long userId = userPrinciple.getId();

        // 返回 Token 和 userId 给前端
        return ResponseEntity.ok(new LoginResponse(token, userId));
    }
}