package com.snakegame.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    @NotBlank
    @Size(min = 1, max = 20)
    private String username;

    @NotBlank
    @Size(min = 1, max = 20)
    private String password;
}
