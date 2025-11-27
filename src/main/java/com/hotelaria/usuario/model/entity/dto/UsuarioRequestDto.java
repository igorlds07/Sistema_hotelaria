package com.hotelaria.usuario.model.entity.dto;

import lombok.Data;

@Data
public class UsuarioRequestDto {
    private String nome;
    private String cpf;
    private String email;
    private String senha;
}
