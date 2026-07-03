package com.snakegame.controller;

import com.snakegame.dto.ApiResponse;
import com.snakegame.dto.AuthRequest;
import com.snakegame.dto.AuthResponse;
import com.snakegame.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody AuthRequest request) {
        AuthResponse resp = userService.register(request.getUsername(), request.getPassword());
        if (resp.getToken() != null) {
            return ApiResponse.ok("注册成功", resp);
        }
        return ApiResponse.fail(resp.getMessage());
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse resp = userService.login(request.getUsername(), request.getPassword());
        if (resp.getToken() != null) {
            return ApiResponse.ok("登录成功", resp);
        }
        return ApiResponse.fail(resp.getMessage());
    }
}
