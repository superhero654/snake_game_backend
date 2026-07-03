package com.snakegame.service;

import com.snakegame.dto.AuthResponse;
import com.snakegame.dto.UserDto;
import com.snakegame.entity.User;
import com.snakegame.repository.UserRepository;
import com.snakegame.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), Collections.emptyList());
    }

    public AuthResponse register(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            return AuthResponse.builder()
                    .message("用户名已存在")
                    .build();
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .highScore(0)
                .build();
        userRepository.save(user);

        String token = jwtUtil.generateToken(username);
        return AuthResponse.builder()
                .token(token)
                .message("注册成功")
                .user(toDto(user))
                .build();
    }

    public AuthResponse login(String username, String rawPassword) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return AuthResponse.builder().message("用户名或密码错误").build();
        }
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            return AuthResponse.builder().message("用户名或密码错误").build();
        }

        String token = jwtUtil.generateToken(username);
        return AuthResponse.builder()
                .token(token)
                .message("登录成功")
                .user(toDto(user))
                .build();
    }

    public UserDto updateHighScore(String username, int score) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        if (score > user.getHighScore()) {
            user.setHighScore(score);
            userRepository.save(user);
        }
        return toDto(user);
    }

    public List<UserDto> getLeaderboard() {
        return userRepository.findTop10ByOrderByHighScoreDesc()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .highScore(user.getHighScore())
                .build();
    }
}
