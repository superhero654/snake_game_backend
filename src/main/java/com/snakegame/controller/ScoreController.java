package com.snakegame.controller;

import com.snakegame.dto.ApiResponse;
import com.snakegame.dto.ScoreRequest;
import com.snakegame.dto.UserDto;
import com.snakegame.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
public class ScoreController {

    private final UserService userService;

    @PutMapping
    public ApiResponse<UserDto> submitScore(@RequestBody ScoreRequest request,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        UserDto updated = userService.updateHighScore(userDetails.getUsername(), request.getScore());
        return ApiResponse.ok("分数已更新", updated);
    }
}
