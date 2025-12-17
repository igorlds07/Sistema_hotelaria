package com.hotelaria.usuario.model.entity.dto;

import com.hotelaria.usuario.model.entity.Perfil;
import lombok.Data;

@Data
public class UsuarioRequestDto {
    private String nome;
    private String cpf;
    private String email;
    private Perfil perfil;
    private String senha;

}
