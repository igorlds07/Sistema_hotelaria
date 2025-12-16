package com.hotelaria.auth.model.entity.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String senha;
}
