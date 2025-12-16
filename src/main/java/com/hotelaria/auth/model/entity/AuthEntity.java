package com.hotelaria.auth.model.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class AuthEntity {
    private String email;
    private String senha;
}
