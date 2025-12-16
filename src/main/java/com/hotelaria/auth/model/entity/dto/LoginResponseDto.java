package com.hotelaria.auth.model.entity.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String token;
    private String email;
    private String senha;
}
