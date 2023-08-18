package com.onlinebookstore.dto.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class UserLoginResponseDto {
    private final String token;
}
//public record UserLoginResponseDto(String token) {
//}
