package com.snakegame.controller;

import com.snakegame.dto.ApiResponse;
import com.snakegame.dto.UserDto;
import com.snakegame.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<List<UserDto>> getLeaderboard() {
        List<UserDto> leaderboard = userService.getLeaderboard();
        return ApiResponse.ok("成功", leaderboard);
    }
}
