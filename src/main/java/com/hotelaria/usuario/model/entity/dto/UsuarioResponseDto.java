package com.hotelaria.usuario.model.entity.dto;

import com.hotelaria.usuario.model.entity.Perfil;
import lombok.Data;

@Data
public class UsuarioResponseDto {
    private Integer id;
    private String nome;
    private String cpf;
    private String email;
    private Perfil perfil;
}
